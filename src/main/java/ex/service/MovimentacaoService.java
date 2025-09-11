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

    // Listar todas movimentações (sem filtro de congregação)
    public List<Movimentacao> listarTodas() {
        return movimentacaoRepository.findAll();
    }

    // Listar por tipo
    public List<Movimentacao> getByTipo(TipoMovimentacao tipo) {
        return movimentacaoRepository.findByTipo(tipo);
    }

    // Listar por usuário
    public List<Movimentacao> listarPorUsuario(int usuarioId) {
        return movimentacaoRepository.findByUsuarioId(usuarioId);
    }

    // Listar por mês, ano e congregação
    public List<Movimentacao> listarPorMesAnoECongregacao(TipoMovimentacao tipo, String mes, String ano, int idCongregacao) {
        String mesStr = mes.length() == 1 ? "0" + mes : mes;
        return movimentacaoRepository.findByMesAnoECongregacao(tipo, mesStr, ano, idCongregacao);
    }

    // Adicionar movimentação
    @Transactional
    public Movimentacao adicionar(Movimentacao movimentacao) {
        if (movimentacao.getTipo() == TipoMovimentacao.DIZIMO && movimentacao.getUsuario() == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo para Dízimo.");
        }
        if (movimentacao.getTipo() != TipoMovimentacao.DIZIMO) {
            movimentacao.setUsuario(null);
        }
        return movimentacaoRepository.save(movimentacao);
    }

    // Calcular total por tipo e congregação (mensal)
    public BigDecimal calcularTotalECongregacao(TipoMovimentacao tipo, String mes, String ano, int idCongregacao) {
        return movimentacaoRepository.calcularTotalPorTipoECongregacao(tipo, mes.length() == 1 ? "0" + mes : mes, ano, idCongregacao);
    }

    // Calcular total geral por congregação
    public BigDecimal calcularTotalGeralPorCongregacao(int idCongregacao) {
        BigDecimal totalDizimos = movimentacaoRepository.getTotalPorTipoECongregacao(TipoMovimentacao.DIZIMO, idCongregacao);
        BigDecimal totalOfertas = movimentacaoRepository.getTotalPorTipoECongregacao(TipoMovimentacao.OFERTA, idCongregacao);
        BigDecimal totalDespesas = movimentacaoRepository.getTotalPorTipoECongregacao(TipoMovimentacao.DESPESA, idCongregacao);
        return totalDizimos.add(totalOfertas).subtract(totalDespesas);
    }

    // Atualizar movimentação
    @Transactional
    public Movimentacao atualizar(Integer id, Movimentacao movimentacaoAtualizada) {
        Movimentacao existente = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimentação não encontrada com ID: " + id));

        if (movimentacaoAtualizada.getTipo() == TipoMovimentacao.DIZIMO && movimentacaoAtualizada.getUsuario() == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo para Dízimo.");
        }
        if (movimentacaoAtualizada.getTipo() != TipoMovimentacao.DIZIMO) {
            movimentacaoAtualizada.setUsuario(null);
        }

        existente.setDescricao(movimentacaoAtualizada.getDescricao());
        existente.setValor(movimentacaoAtualizada.getValor());
        existente.setData(movimentacaoAtualizada.getData());
        existente.setTipo(movimentacaoAtualizada.getTipo());
        existente.setUsuario(movimentacaoAtualizada.getUsuario());

        return movimentacaoRepository.save(existente);
    }

    // Excluir movimentação
    public void excluir(int id) {
        movimentacaoRepository.deleteById(id);
    }
}
