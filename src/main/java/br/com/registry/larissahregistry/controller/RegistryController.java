package br.com.registry.larissahregistry.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import br.com.registry.larissahregistry.component.Integration;
import br.com.registry.larissahregistry.entity.Registry;
import br.com.registry.larissahregistry.entity.dto.CertificateDto;
import br.com.registry.larissahregistry.repository.RegistryRepository;

@Controller
@RequestMapping(value = "/registries")
public class RegistryController {

    @Autowired
    private RegistryRepository registryRepository;

    @Autowired
    private Integration integration;

    @GetMapping("")
    public ModelAndView index() {
        List<Registry> registries = this.registryRepository.findAll();
        ModelAndView mv = new ModelAndView("registries/index");
        mv.addObject("registries", registries);
        return mv;
    }

    @GetMapping("/new")
    public ModelAndView newForm(Registry registry, Model model) throws MalformedURLException, IOException {
        ModelAndView mv = new ModelAndView("registries/new");
        List<String> options = new ArrayList<String>();
        List<CertificateDto> listCertificateDto = integration.getCertificate();
        for (CertificateDto certificate : listCertificateDto) {
            options.add(certificate.getNome());
        }
        model.addAttribute("options", options);
        return mv;
    }

    @PostMapping("")
    public ModelAndView create(@Valid Registry registry, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("registries/new");
            return mv;
        } else {
                registryRepository.save(registry);
                return new ModelAndView("redirect:/registries");
        }
    }

    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable Long id) {
        Optional<Registry> optional = this.registryRepository.findById(id);
        if (optional.isPresent()) {
            Registry registry = optional.get();
            ModelAndView mv = new ModelAndView("registries/show");
            mv.addObject("registry", registry);
            return mv;
        } else {
            return new ModelAndView("redirect:/registries");
        }
    }

    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, Registry registry, Model model)
            throws MalformedURLException, IOException {
        Optional<Registry> optional = this.registryRepository.findById(id);
        if (optional.isPresent()) {
            ModelAndView mv = new ModelAndView( "registries/edit");
            mv.addObject("registryId", registry.getId());
            List<String> options = new ArrayList<String>();
            List<CertificateDto> listCertificateDto = integration.getCertificate();
            for (CertificateDto certificate : listCertificateDto) {
                options.add(certificate.getNome());
            }
            model.addAttribute("options", options);
            return mv;
        } else {
            return new ModelAndView("redirect:/registries");
        }
    }

    @PostMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, @Valid Registry registry, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("registries/edit");
            mv.addObject("registry", id);
            return mv;
        } else {
            Optional<Registry> optional = this.registryRepository.findById(id);
            if (optional.isPresent()) {
                registryRepository.save(registry);
                return new ModelAndView("redirect:/registries");
            } else {
                return new ModelAndView("redirect:/registries");
            }
        }
    }

    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("redirect:/registries");
        try {
            this.registryRepository.deleteById(id);
            mv.addObject("mensagem", "Registry" + id + " deleted");
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e);
        }
        return mv;
    }

}
