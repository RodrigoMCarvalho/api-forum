package com.rodrigo.forum.controller;

import com.rodrigo.forum.model.Curso;
import com.rodrigo.forum.model.Topico;
import com.rodrigo.forum.model.dto.TopicoDTO;
import com.rodrigo.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @GetMapping
    public List<TopicoDTO> lista(String nomeCurso) {
        if(nomeCurso == null) {
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDTO.converter(topicos);
        }
        List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
        return TopicoDTO.converter(topicos);
    }
}
