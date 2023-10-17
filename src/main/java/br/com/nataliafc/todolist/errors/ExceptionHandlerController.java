package br.com.nataliafc.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// anotação usada para definir classes globais no momento de tratamento de exceções
@ControllerAdvice
public class ExceptionHandlerController {

	// toda exceção desse tipo vai passar por esta classe aqui
	// e.getMessage vai pegar a mensagem personalizada que colocamos no taskmodel
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
	}
}
