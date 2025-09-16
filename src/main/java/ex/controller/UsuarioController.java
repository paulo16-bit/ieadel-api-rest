package ex.controller;

import ex.model.Congregacao;
import ex.model.Perfil;
import ex.model.Usuario;
import ex.model.UsuarioDTO;
import ex.model.repository.UsuarioRepository;
import ex.model.repository.CongregacaoRepository;
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
    @Autowired
    private CongregacaoRepository congregacaoRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    	Usuario user = authService.autenticar(request.getEmail(), request.getSenha());
        
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
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
            .map(usuario -> ResponseEntity.ok(usuario))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/novo")
    public ResponseEntity<?> criar(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(usuarioDTO.getNome());
            novoUsuario.setEmail(usuarioDTO.getEmail());
            novoUsuario.setSenha(usuarioDTO.getSenha());
            novoUsuario.setPerfil(Perfil.USER);

            Congregacao congregacao = congregacaoRepository.findById(usuarioDTO.getIdCongregacao())
                    .orElseThrow(() -> new IllegalArgumentException("Congregação não encontrada"));

            novoUsuario.setCongregacao(congregacao);

            Usuario salvo = authService.adicionar(novoUsuario);
            return ResponseEntity.ok(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(
    		@PathVariable Long id, 
    		@RequestBody Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
            .map(usuarioExistente -> {
                usuarioExistente.setNome(usuarioAtualizado.getNome());
                usuarioExistente.setEmail(usuarioAtualizado.getEmail());
                usuarioExistente.setSenha(usuarioAtualizado.getSenha());
                // atualize outros campos, se houver
                usuarioRepository.save(usuarioExistente);
                return ResponseEntity.ok(usuarioExistente);
            })
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuarioRepository.delete(usuario);
                return ResponseEntity.ok("Usuário deletado com sucesso");
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado"));
    }
}
class LoginRequest {
    private String email;
    private String senha;

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
}