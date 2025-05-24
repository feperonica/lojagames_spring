package com.generation.lojagames.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.lojagames.model.Usuario;
import com.generation.lojagames.model.UsuarioLogin;
import com.generation.lojagames.repository.UsuarioRepository;
import com.generation.lojagames.security.JwtService;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	// Cadastrar usuário
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

		// Verificar se já existe o usuário
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
			return Optional.empty();
		}

		// Verificar se é maior de 18 anos
		if (!checarMaiorDeIdade(usuario.getDataNascimento())) {
			return Optional.empty();
		}
		// Criptografa a senha antes de salvar no banco
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		// Salva o usuário no banco e retorna o usuário cadastrado
		return Optional.of(usuarioRepository.save(usuario));
	}
	// Método auxiliar para checar se o usuário é maior de 18 anos
	private boolean checarMaiorDeIdade(LocalDate dataNascimento) {
		// Calcula o período entre a data de nascimento e a data atual
		// Se for maior ou igual a 18, retorna true
		return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
	}
	// Atualizar usuário
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {
		if (usuarioRepository.findById(usuario.getId()).isPresent()) {

			if (!checarMaiorDeIdade(usuario.getDataNascimento())) {
				return Optional.empty();
			}

			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			return Optional.of(usuarioRepository.save(usuario));
		}

		return Optional.empty();
	}

	// Autenticar usuário
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(),
				usuarioLogin.get().getSenha());

		Authentication authentication = authenticationManager.authenticate(credenciais);

		if (authentication.isAuthenticated()) {
			Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());

			if (usuario.isPresent()) {
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setSenha("");
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));

				return usuarioLogin;
			}
		}

		return Optional.empty();
	}

	// Buscar usuário por ID
	public Optional<Usuario> buscarPorId(Long id) {
		return usuarioRepository.findById(id);
	}

	// Buscar usuário pelo e-mail (usuario)
	public Optional<Usuario> buscarPorUsuario(String usuario) {
		return usuarioRepository.findByUsuario(usuario);
	}

	// Listar todos os usuários
	public List<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}

	// Deletar usuário por ID
	public void deletarUsuario(Long id) {
		usuarioRepository.deleteById(id);
	}

	// Gerar token JWT
	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario);
	}

	// Criptografar senha usando BCrypt
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}
}