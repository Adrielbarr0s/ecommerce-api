package com.adrielbarros.ecommerce.service;

import com.adrielbarros.ecommerce.dto.AuthResponseDTO;
import com.adrielbarros.ecommerce.dto.LoginDTO;
import com.adrielbarros.ecommerce.dto.RegisterDTO;
import com.adrielbarros.ecommerce.model.Usuario;
import com.adrielbarros.ecommerce.repository.UsuarioRepository;
import com.adrielbarros.ecommerce.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDTO cadastrar(RegisterDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("O email informado já está em uso.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);
        return new AuthResponseDTO(token, usuario.getEmail(), usuario.getNome());
    }

    public AuthResponseDTO login(LoginDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        String token = jwtService.generateToken(usuario);
        return new AuthResponseDTO(token, usuario.getEmail(), usuario.getNome());
    }
}