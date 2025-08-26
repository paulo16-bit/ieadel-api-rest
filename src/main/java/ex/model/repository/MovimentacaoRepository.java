package ex.model.repository;


import ex.model.Movimentacao;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer> {
    List<Movimentacao> findByTipo(TipoMovimentacao tipo);
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimentacao m " +
    	       "WHERE m.tipo = :tipo ")
    BigDecimal getTotalPorTipo(@Param("tipo") TipoMovimentacao tipo);
    
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimentacao m " +
    	       "WHERE m.tipo = :tipo " +
    	       "AND TO_CHAR(m.data, 'MM') = :mes " +
    	       "AND TO_CHAR(m.data, 'YYYY') = :ano")
    	BigDecimal calcularTotalPorTipo(@Param("tipo") TipoMovimentacao tipo, @Param("mes") String mes, @Param("ano") String ano);
    
    @Query("SELECT m FROM Movimentacao m " +
    		   "WHERE m.tipo = :tipo " +
    	       "AND FUNCTION('TO_CHAR', m.data, 'MM') = :mesStr " +
    	       "AND FUNCTION('TO_CHAR', m.data, 'YYYY') = :anoStr " +
    	       "ORDER BY m.data ASC")
    List<Movimentacao> findByMesAndAno(@Param("tipo") TipoMovimentacao tipo, @Param("mesStr") String mesStr, @Param("anoStr") String anoStr);
    
    List<Movimentacao> findByUsuarioId(int id);
    List<Movimentacao> findAll();
}