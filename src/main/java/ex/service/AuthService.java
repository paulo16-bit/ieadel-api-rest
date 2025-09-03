package ex.service;

import ex.model.Usuario;
import ex.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Usuario adicionar(Usuario usuario) {
    	usuario.setSenha(MD5(usuario.getSenha()));
    	return usuarioRepository.save(usuario);
    }

    public Usuario autenticar(String email, String senha) {
        Usuario user = usuarioRepository.findByEmail(email);
        if (user != null && user.getSenha().equals(MD5(senha))) {
            return user; 
        }
        return null;
    }

    private String MD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}