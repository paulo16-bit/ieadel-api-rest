package ex.model;

import java.time.LocalDate;

public class MovimentacaoDTO {
    private String descricao;
    private double valor;
    private LocalDate data;
    private String tipo; // Pode ser TipoMovimentacao ou String dependendo da necessidade
    private Integer id_usuario; // Adicione o id do usuário

    // Getters e Setters
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getUsuarioId() {
        return id_usuario;
    }

    public void setUsuarioId(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }
}
