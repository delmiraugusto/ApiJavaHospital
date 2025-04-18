package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.Infra.security.DadoTokenJWT;
import med.voll.api.Infra.security.TokenService;
import med.voll.api.domain.usuario.DadosAutenticacao;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        // Fizemos um DTO para na geracao do token, ele vir dentro de um JSON e nao solto
        return ResponseEntity.ok(new DadoTokenJWT(tokenJWT));
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity efetuarCadastro(@RequestBody @Valid DadosAutenticacao dados){

        if(usuarioRepository.findByLogin(dados.login()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Usuário já cadastrado");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(dados.senha());
        Usuario savedUser = usuarioRepository.save(new Usuario(dados.login(), encryptedPassword));
        var authenticationToken = new UsernamePasswordAuthenticationToken(savedUser.getLogin(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadoTokenJWT(tokenJWT));
    }
}
