package br.com.nataliafc.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/post-user")
	public ResponseEntity<String> createUser(@RequestBody UserModel userModel) {
		
		var user = this.userRepository.findByUsername(userModel.getUsername());
		
		if(user != null) {
			// retorna um status code 400 e uma mensagem de erro
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe.");
		}
		
		// 12 é o salt de criptografia, e o método toCharArray transforma a senha em um array de caracteres
		// depois a senha hasheada é setada em vez da senha crua
		var hashedPassword = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
		
		userModel.setPassword(hashedPassword);
		
		var userCreated = this.userRepository.save(userModel);
		// retorna um status code 201 e uma mensagem de criação de usuário
		return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado!");
	}
	
}