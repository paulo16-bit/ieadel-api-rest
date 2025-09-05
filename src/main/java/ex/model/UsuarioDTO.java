package ex.model;

public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private Long idCongregacao;
    private Perfil perfil;
    private Boolean ativo;

    // Getters e Setters
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Long getIdCongregacao() {
        return idCongregacao;
    }
    public void setIdCongregacao(Long idCongregacao) {
        this.idCongregacao = idCongregacao;
    }
    public Perfil getPerfil() {
        return perfil;
    }
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

}
