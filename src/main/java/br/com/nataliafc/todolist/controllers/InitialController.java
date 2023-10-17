package br.com.nataliafc.todolist.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/primeira-rota")
public class InitialController {
	
	@GetMapping("/")
	public String firstMessage() {
		return "Funcionou";
	}
	
}