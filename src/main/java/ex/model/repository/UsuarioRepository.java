package ex.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ex.model.Usuario;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByEmail(String email);
    List<Usuario> findAll();
}