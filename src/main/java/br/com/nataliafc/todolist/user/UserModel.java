package br.com.nataliafc.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

// o @Data do lombok cria os getters e setters automaticamente
// entidades representam as tabelas lá no db
@Data
@Entity(name = "tb_users")
public class UserModel {
	
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;
	
	public String name;
	@Column(unique = true)
	public String username;
	public String password;

	@CreationTimestamp
	private LocalDateTime createdAt;
}