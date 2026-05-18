package com.example.config;

import com.example.service.WebFooterService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class FooterModelAdvice {

    private final WebFooterService webFooterService;

    public FooterModelAdvice(WebFooterService webFooterService) {
        this.webFooterService = webFooterService;
    }

    @ModelAttribute
    public void addFooterAttributes(Model model) {
        Map<String, List<String>> footerData = webFooterService.obtenerAgrupadoPorTipo();

        model.addAttribute("footerEmpresa", primerValor(footerData, "empresa"));
        model.addAttribute("footerDescripcion", primerValor(footerData, "descripcion"));
        model.addAttribute("footerDireccion", primerValor(footerData, "direccion"));
        model.addAttribute("footerCopyright", primerValor(footerData, "copyright"));
        model.addAttribute("footerEnlacesLegales", footerData.getOrDefault("enlace_legal", List.of()));
        model.addAttribute("footerEmpresaInfo", footerData.getOrDefault("empresa_info", List.of()));
        model.addAttribute("footerRedesSociales", footerData.getOrDefault("red_social", List.of()));
    }

    private String primerValor(Map<String, List<String>> footerData, String tipoInfo) {
        return footerData.getOrDefault(tipoInfo, List.of())
                .stream()
                .findFirst()
                .orElse("");
    }
}

