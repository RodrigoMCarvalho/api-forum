package com.rodrigo.forum.repository;

import com.rodrigo.forum.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    
    List<Topico> findByCursoNome(String nomeCurso);
}
