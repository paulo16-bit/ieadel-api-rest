package ex.service;

import ex.model.Movimentacao;
import ex.model.TipoMovimentacao;
import ex.model.repository.MovimentacaoRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    public List<Movimentacao> listarTodas() {
        return movimentacaoRepository.findAll();
    }
    
    public List<Movimentacao> getByTipo(TipoMovimentacao tipo) {
        return movimentacaoRepository.findByTipo(tipo);
    }
    public List<Movimentacao> listarPorUsuario(int usuarioId) {
        return movimentacaoRepository.findByUsuarioId(usuarioId);
    }


    // Adicionar uma nova movimentação
    @Transactional
    public Movimentacao adicionar(Movimentacao movimentacao) {
        if (movimentacao.getTipo() == TipoMovimentacao.DIZIMO) {
            if (movimentacao.getUsuario() == null) {
                throw new IllegalArgumentException("Usuário não pode ser nulo para Dízimo.");
            }
        } else {
            movimentacao.setUsuario(null); // Evita o erro caso não seja dízimo
        }

        return movimentacaoRepository.save(movimentacao);
    }
    
    public BigDecimal calcularTotal(TipoMovimentacao tipo, String mes, String ano) {
        return movimentacaoRepository.calcularTotalPorTipo(tipo, mes, ano);
    }
    
    public BigDecimal calcularTotalGeral() {
    	BigDecimal Totaldizimos = movimentacaoRepository.getTotalPorTipo(TipoMovimentacao.DIZIMO);
    	BigDecimal Totaloferta = movimentacaoRepository.getTotalPorTipo(TipoMovimentacao.OFERTA);
    	BigDecimal Totaldespesa = movimentacaoRepository.getTotalPorTipo(TipoMovimentacao.DESPESA);
    	BigDecimal totalGeral = Totaldizimos.add(Totaloferta).subtract(Totaldespesa);
    	return totalGeral;
    	
    }
    
    public List<Movimentacao> listarPorMesAno(TipoMovimentacao tipo, String mes, String ano) {
        return movimentacaoRepository.findByMesAndAno(tipo,
            mes.length() == 1 ? "0" + mes : mes, // Garante 2 dígitos
            ano
        );
    }

    // Atualizar uma movimentação existente
    @Transactional
    public Movimentacao atualizar(Integer id, Movimentacao movimentacaoAtualizada) {
        Movimentacao movimentacaoExistente = movimentacaoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Movimentação não encontrada com ID: " + id));

        if (movimentacaoAtualizada.getTipo() == TipoMovimentacao.DIZIMO) {
            if (movimentacaoAtualizada.getUsuario() == null) {
                throw new IllegalArgumentException("Usuário não pode ser nulo para Dízimo.");
            }
        } else {
            movimentacaoAtualizada.setUsuario(null);
        }

        movimentacaoExistente.setDescricao(movimentacaoAtualizada.getDescricao());
        movimentacaoExistente.setValor(movimentacaoAtualizada.getValor());
        movimentacaoExistente.setData(movimentacaoAtualizada.getData());
        movimentacaoExistente.setTipo(movimentacaoAtualizada.getTipo());
        movimentacaoExistente.setUsuario(movimentacaoAtualizada.getUsuario());

        return movimentacaoRepository.save(movimentacaoExistente);
    }

    // Excluir uma movimentação
    public void excluir(int id) {
        movimentacaoRepository.deleteById(id);
    }
}
