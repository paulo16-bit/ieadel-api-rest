package ex.controller;

import ex.model.Usuario;
import ex.model.repository.UsuarioRepository;
import ex.service.AuthService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UsuarioController {
	@Autowired
    private AuthService authService;
	@Autowired
	private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    	Usuario user = authService.autenticar(request.getUsuario(), request.getSenha());
        
    	if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos");
        }
    }
    
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable int id) {
        return usuarioRepository.findById(id)
            .map(usuario -> ResponseEntity.ok(usuario))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/novo")
    public ResponseEntity<?> criar(@RequestBody Usuario usuario) {
    	try {
    		Usuario salva = authService.adicionar(usuario);
            return ResponseEntity.ok(salva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(
    		@PathVariable int id, 
    		@RequestBody Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
            .map(usuarioExistente -> {
                usuarioExistente.setNome(usuarioAtualizado.getNome());
                usuarioExistente.setUsuario(usuarioAtualizado.getUsuario());
                usuarioExistente.setSenha(usuarioAtualizado.getSenha());
                // atualize outros campos, se houver
                usuarioRepository.save(usuarioExistente);
                return ResponseEntity.ok(usuarioExistente);
            })
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable int id) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuarioRepository.delete(usuario);
                return ResponseEntity.ok("Usuário deletado com sucesso");
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado"));
    }
}
class LoginRequest {
    private String usuario;
    private String senha;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}