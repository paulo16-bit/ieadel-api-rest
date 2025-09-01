package ex.controller;

import ex.model.Movimentacao;
import ex.model.MovimentacaoDTO;
import ex.model.Usuario;
import ex.model.repository.MovimentacaoRepository;
import ex.model.repository.TipoMovimentacao;
import ex.model.repository.UsuarioRepository;
import ex.service.MovimentacaoService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Rota para listar todas as movimentações (GET)
    @GetMapping
    public ResponseEntity<List<Movimentacao>> listarPorMesAno(
    	@RequestParam TipoMovimentacao tipo,
    	@RequestParam String mes,
        @RequestParam String ano) {
        
        // Validação dos parâmetros
        if (!mes.matches("\\d{2}") || !ano.matches("\\d{4}")) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        
        List<Movimentacao> movimentacoes = movimentacaoService.listarPorMesAno(tipo, mes, ano);
        return ResponseEntity.ok(movimentacoes);
    }
    
    @GetMapping("/totais")
    public ResponseEntity<Map<String, BigDecimal>> getTotaisPorMes(
        @RequestParam String mes,
        @RequestParam String ano) {
        
        Map<String, BigDecimal> totais = new HashMap<>();
        totais.put("dizimo", movimentacaoService.calcularTotal(TipoMovimentacao.dizimo, mes, ano));
        totais.put("oferta", movimentacaoService.calcularTotal(TipoMovimentacao.oferta, mes, ano));
        totais.put("despesa", movimentacaoService.calcularTotal(TipoMovimentacao.despesa, mes, ano));
        
        return ResponseEntity.ok(totais);
    }
    @GetMapping("/totalGeral")
    public ResponseEntity<Map<String, BigDecimal>> getTotalGerals(){
    	Map<String, BigDecimal> totais = new HashMap<>();
    	totais.put("total", movimentacaoService.calcularTotalGeral());
    	return ResponseEntity.ok(totais);
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<Movimentacao> buscarMovimentacaoPorId(@PathVariable int id) {
        return movimentacaoRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/dizimoByUsuario")
    public ResponseEntity<List<Movimentacao>> listarPorMesAno(
    	@RequestParam int id_usuario) {
        List<Movimentacao> movimentacoes = movimentacaoService.listarPorUsuario(id_usuario);
        return ResponseEntity.ok(movimentacoes);
    }

    // Rota para adicionar uma nova movimentação (POST)
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody MovimentacaoDTO movimentacaoDTO) {
        try {
            if (movimentacaoDTO.getTipo().equals("dizimo") && movimentacaoDTO.getUsuarioId() == null) {
                return ResponseEntity.badRequest().body("Dízimos requerem usuarioId");
            }

            // Crie a entidade Movimentacao a partir do DTO
            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setDescricao(movimentacaoDTO.getDescricao());
            movimentacao.setValor(movimentacaoDTO.getValor());
            movimentacao.setData(movimentacaoDTO.getData());
            movimentacao.setTipo(TipoMovimentacao.valueOf(movimentacaoDTO.getTipo()));

            // Busque o usuário se necessário
            if (movimentacaoDTO.getUsuarioId() != null) {
                Usuario usuario = usuarioRepository.findById(movimentacaoDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                movimentacao.setUsuario(usuario);
            }

            Movimentacao salva = movimentacaoService.adicionar(movimentacao);
            return ResponseEntity.ok(salva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Rota para atualizar uma movimentação (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarMovimentacao(
            @PathVariable Integer id,
            @RequestBody MovimentacaoDTO movimentacaoDTO) {
        
        try {
            // Validação para dízimos
            if (movimentacaoDTO.getTipo().equals("dizimo") && movimentacaoDTO.getUsuarioId() == null) {
                return ResponseEntity.badRequest().body("Dízimos requerem usuarioId");
            }

            // Converte DTO para entidade
            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setDescricao(movimentacaoDTO.getDescricao());
            movimentacao.setValor(movimentacaoDTO.getValor());
            movimentacao.setData(movimentacaoDTO.getData());
            movimentacao.setTipo(TipoMovimentacao.valueOf(movimentacaoDTO.getTipo()));

            // Vincula usuário se for dízimo
            if (movimentacaoDTO.getTipo().equals("dizimo")) {
                Usuario usuario = usuarioRepository.findById(movimentacaoDTO.getUsuarioId())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                movimentacao.setUsuario(usuario);
            }

            // CHAMADA DA FUNÇÃO ATUALIZAR DO SERVICE
            Movimentacao movimentacaoAtualizada = movimentacaoService.atualizar(id, movimentacao);
            
            return ResponseEntity.ok(movimentacaoAtualizada);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Rota para excluir uma movimentação (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirMovimentacao(@PathVariable int id) {
        movimentacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
