package br.com.nataliafc.todolist.task;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.nataliafc.todolist.user.UserModel;

public interface TaskRepository extends JpaRepository<TaskModel, UUID>{
	List<TaskModel> findByUserId(UUID userId);
	
	TaskModel findByIdAndUserId(UUID id, UUID userId);

}
