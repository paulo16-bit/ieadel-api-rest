package ex.model;

public record RegisterDTO(String nome, String email, String senha, Perfil perfil, Long idCongregacao) {
}
