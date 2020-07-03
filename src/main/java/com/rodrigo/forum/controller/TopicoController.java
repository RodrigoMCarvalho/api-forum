package com.rodrigo.forum.controller;

import com.rodrigo.forum.model.Topico;
import com.rodrigo.forum.model.dto.DetalhesDoTopicoDTO;
import com.rodrigo.forum.model.dto.TopicoDTO;
import com.rodrigo.forum.model.form.AtualizacapTopicoForm;
import com.rodrigo.forum.model.form.TopicoForm;
import com.rodrigo.forum.repository.CursoRepository;
import com.rodrigo.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDTO> lista(String nomeCurso) {
        if(nomeCurso == null) {
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDTO.converter(topicos);
        }
        List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
        return TopicoDTO.converter(topicos);
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
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacapTopicoForm form) {
        Optional<Topico> topicoOpt = topicoRepository.findById(id);
        if(!topicoOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Topico topico = form.atualizar(id, topicoRepository);
        topicoRepository.save(topico);
        return ResponseEntity.ok(new TopicoDTO(topico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Topico> topicoOpt = topicoRepository.findById(id);
        if(!topicoOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
