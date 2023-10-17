package br.com.nataliafc.todolist.auth;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.nataliafc.todolist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TaskAuth extends OncePerRequestFilter {

	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var servletPath = request.getServletPath();
		if (servletPath.startsWith("/tasks/")) {

			// Pega a autenticação (usuário e senha)
			var authorization = request.getHeader("Authorization");
			System.out.println("Autorização: " + authorization);
			// vai printar a autorização, no caso "Basic shudiasgdoow38yhd";
			// a hash é decodificada em base64 com o nome e senha não criptografada do user

			var authEncoded = authorization.substring("Basic".length()).trim();
			System.out.println("Senha: " + authEncoded);
			// remove a primeira parte da hash onde está o nome "Basic", verifica o tamanho
			// (5 caracteres), corta, e remove os espaços.
			// o argumento da substring(0) é um número, portanto é o tamanho da palavra
			// "Basic"

			byte[] authDecode = Base64.getDecoder().decode(authEncoded);
			System.out.println("Decoded: " + authDecode);
			// decodifica a hash em um array de bytes

			var authString = new String(authDecode);
			System.out.println("String: " + authString);
			// transforma o array de bytes em uma string

			String[] credentials = authString.split(":");
			String username = credentials[0];
			String password = credentials[1];
			// divide a string nos dois pontos separadores entre user e password, criando um
			// novo array

			// Validar usuário
			// verifica no repository se existe o username
			var user = this.userRepository.findByUsername(username);
			if (user == null) {
				response.sendError(401, "Unauthorized");
			} else {

				// Validar Senha
				// comparo a senha que está sendo recebida com a senha do banco de dados a fim
				// de validação
				var verifiedPassword = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
				if (verifiedPassword.verified) {
					request.setAttribute("userId", user.getId());
					filterChain.doFilter(request, response);

				} else {
					response.sendError(401, "Unauthorized");
				}
			}

		} else {
			filterChain.doFilter(request, response);
		}
		
		

	}

}
