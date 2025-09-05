package ex.controller;

import ex.model.Congregacao;
import ex.model.repository.CongregacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/congregacoes")
public class CongregacaoController {
    @Autowired
    private CongregacaoRepository congregacaoRepository;

    @GetMapping
    public List<Congregacao> getCongregacao(){
        return congregacaoRepository.findAll();
    }
}
