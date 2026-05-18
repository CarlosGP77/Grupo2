package com.example.service;

import com.example.model.WebFooter;
import com.example.repository.WebFooterRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WebFooterService {

    private final WebFooterRepository webFooterRepository;

    public WebFooterService(WebFooterRepository webFooterRepository) {
        this.webFooterRepository = webFooterRepository;
    }

    public List<WebFooter> obtenerTodos() {
        return webFooterRepository.findAllByOrderByIdFooterAsc();
    }

    public Map<String, List<String>> obtenerAgrupadoPorTipo() {
        return webFooterRepository.findAllByOrderByIdFooterAsc()
                .stream()
                .collect(Collectors.groupingBy(
                        WebFooter::getTipoInfo,
                        LinkedHashMap::new,
                        Collectors.mapping(WebFooter::getContenido, Collectors.toList())
                ));
    }

    public List<String> obtenerContenidosPorTipo(String tipoInfo) {
        return obtenerAgrupadoPorTipo().getOrDefault(tipoInfo, List.of());
    }

    public String obtenerTextoPorTipo(String tipoInfo) {
        return obtenerContenidosPorTipo(tipoInfo)
                .stream()
                .findFirst()
                .orElse("");
    }
}

