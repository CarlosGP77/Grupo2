package com.example.repository;

import com.example.model.WebFooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebFooterRepository extends JpaRepository<WebFooter, Integer> {
    List<WebFooter> findAllByOrderByIdFooterAsc();
    List<WebFooter> findByTipoInfoOrderByIdFooterAsc(String tipoInfo);
}

