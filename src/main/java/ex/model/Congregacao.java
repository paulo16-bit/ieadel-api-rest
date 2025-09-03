package ex.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "congregacao")
public class Congregacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCongregacao;

    private String nome;
    private String endereco;

    @OneToMany(mappedBy = "congregacao")
    @JsonIgnore
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "congregacao")
    @JsonIgnore
    private List<Movimentacao> movimentacoes;

    public Long getIdCongregacao() {
        return idCongregacao;
    }

    public void setIdCongregacao(Long idCongregacao) {
        this.idCongregacao = idCongregacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Movimentacao> getMovimentacoes() {
        return movimentacoes;
    }

    public void setMovimentacoes(List<Movimentacao> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }
}
