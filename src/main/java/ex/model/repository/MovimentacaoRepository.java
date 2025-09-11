package ex.model.repository;

import ex.model.Movimentacao;
import ex.model.TipoMovimentacao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer> {

    // Listar por tipo
    List<Movimentacao> findByTipo(TipoMovimentacao tipo);

    // Listar por usuário
    List<Movimentacao> findByUsuarioId(int id);

    // Listar todas
    List<Movimentacao> findAll();

    // Totais por tipo
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimentacao m " +
            "WHERE m.tipo = :tipo")
    BigDecimal getTotalPorTipo(@Param("tipo") TipoMovimentacao tipo);

    // Totais por tipo e congregação
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimentacao m " +
            "WHERE m.tipo = :tipo AND m.congregacao.idCongregacao = :idCongregacao")
    BigDecimal getTotalPorTipoECongregacao(@Param("tipo") TipoMovimentacao tipo, @Param("idCongregacao") int idCongregacao);

    // Totais por tipo, mês, ano
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimentacao m " +
            "WHERE m.tipo = :tipo " +
            "AND FUNCTION('TO_CHAR', m.data, 'MM') = :mes " +
            "AND FUNCTION('TO_CHAR', m.data, 'YYYY') = :ano")
    BigDecimal calcularTotalPorTipo(@Param("tipo") TipoMovimentacao tipo, @Param("mes") String mes, @Param("ano") String ano);

    // Totais por tipo, mês, ano e congregação
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimentacao m " +
            "WHERE m.tipo = :tipo " +
            "AND FUNCTION('TO_CHAR', m.data, 'MM') = :mes " +
            "AND FUNCTION('TO_CHAR', m.data, 'YYYY') = :ano " +
            "AND m.congregacao.idCongregacao = :idCongregacao")
    BigDecimal calcularTotalPorTipoECongregacao(@Param("tipo") TipoMovimentacao tipo, @Param("mes") String mes, @Param("ano") String ano, @Param("idCongregacao") int idCongregacao);

    // Listar por mês e ano
    @Query("SELECT m FROM Movimentacao m " +
            "WHERE m.tipo = :tipo " +
            "AND FUNCTION('TO_CHAR', m.data, 'MM') = :mesStr " +
            "AND FUNCTION('TO_CHAR', m.data, 'YYYY') = :anoStr " +
            "ORDER BY m.data ASC")
    List<Movimentacao> findByMesAndAno(@Param("tipo") TipoMovimentacao tipo, @Param("mesStr") String mesStr, @Param("anoStr") String anoStr);

    // Listar por mês, ano e congregação
    @Query("SELECT m FROM Movimentacao m " +
            "WHERE m.tipo = :tipo " +
            "AND FUNCTION('TO_CHAR', m.data, 'MM') = :mesStr " +
            "AND FUNCTION('TO_CHAR', m.data, 'YYYY') = :anoStr " +
            "AND m.congregacao.idCongregacao = :idCongregacao " +
            "ORDER BY m.data ASC")
    List<Movimentacao> findByMesAnoECongregacao(
            @Param("tipo") TipoMovimentacao tipo,
            @Param("mesStr") String mesStr,
            @Param("anoStr") String anoStr,
            @Param("idCongregacao") int idCongregacao);
}
