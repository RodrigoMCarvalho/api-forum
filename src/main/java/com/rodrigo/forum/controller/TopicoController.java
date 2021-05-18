package com.rodrigo.forum.controller;

import com.rodrigo.forum.model.Topico;
import com.rodrigo.forum.model.dto.DetalhesDoTopicoDTO;
import com.rodrigo.forum.model.dto.TopicoDTO;
import com.rodrigo.forum.model.form.AtualizacaoTopicoForm;
import com.rodrigo.forum.model.form.TopicoForm;
import com.rodrigo.forum.repository.CursoRepository;
import com.rodrigo.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso,
                                 @PageableDefault(sort = "dataCriacao", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
        //http://localhost:8080/topicos?page=0&size=4&sort=titulo,desc
        if(nomeCurso == null) {
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDTO.converter(topicos);
        }
        Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
        return TopicoDTO.converter(topicos);
    }

    @GetMapping("listar")
    public ResponseEntity<List<TopicoDTO>> getAll() {
        List<Topico> topicos = topicoRepository.findAll();
        if(topicos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        topicos.forEach(topico -> {
            topico.add(linkTo(methodOn(TopicoController.class).getOneTopico(topico.getId())).withSelfRel());
        });
        return ResponseEntity.ok(topicos.stream().map(TopicoDTO::new).collect(Collectors.toList()));
    }
    
    @GetMapping("listar/{id}")
    public ResponseEntity<TopicoDTO> getOneTopico(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if(topico.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        topico.get().add(linkTo(methodOn(TopicoController.class).getAll()).withRel("Lista de tópicos"));
        return ResponseEntity.ok(new TopicoDTO(topico.get()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesDoTopicoDTO> detalhar(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if(!topico.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new DetalhesDoTopicoDTO(topico.get()));
    }

    @PostMapping
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
        Optional<Topico> topicoOpt = topicoRepository.findById(id);
        if(!topicoOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Topico topico = form.atualizar(id, topicoRepository);
        topicoRepository.save(topico);
        return ResponseEntity.ok(new TopicoDTO(topico));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Topico> topicoOpt = topicoRepository.findById(id);
        if(!topicoOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
