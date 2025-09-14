package ex.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mov")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_congregacao", nullable = false)
    private Congregacao congregacao;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentacao tipo;

    @Column(name = "data_registro")
    private LocalDateTime dataRegistro = LocalDateTime.now();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Congregacao getCongregacao() { return congregacao; }
    public void setCongregacao(Congregacao congregacao) { this.congregacao = congregacao; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public TipoMovimentacao getTipo() { return tipo; }
    public void setTipo(TipoMovimentacao tipo) { this.tipo = tipo; }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
}
