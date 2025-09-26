package ex.controller;

import ex.infra.security.TokenService;
import ex.model.*;
import ex.model.repository.UsuarioRepository;
import ex.model.repository.CongregacaoRepository;
import ex.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UsuarioController {
	@Autowired
	private UsuarioRepository usuarioRepository;
    @Autowired
    private CongregacaoRepository congregacaoRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

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

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data, HttpServletResponse response) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var usuario = (Usuario) auth.getPrincipal(); // Obtenha o objeto Usuario
        var token = tokenService.generateToken(usuario); // Gere o token com o objeto Usuario

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // true em produção (HTTPS)
                .path("/")
                .maxAge(24 * 60 * 60) // 1 dia
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new LoginResponseDTO(token, usuario)); // Retorne o DTO com token e usuário
    }


    @PostMapping("/novo")
    public ResponseEntity criar(@RequestBody @Valid RegisterDTO data) {
        if (this.usuarioRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        Usuario usuario = new Usuario(data.nome(), data.email(), encryptedPassword, data.perfil());
        usuario.setPerfil(Perfil.USER);
        Congregacao congregacao = congregacaoRepository.findById(data.idCongregacao())
                .orElseThrow(() -> new IllegalArgumentException("Congregação não encontrada"));;
        usuario.setCongregacao(congregacao);

        this.usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuario);
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