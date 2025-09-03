package ex.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ex.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
    List<Usuario> findAll();
}