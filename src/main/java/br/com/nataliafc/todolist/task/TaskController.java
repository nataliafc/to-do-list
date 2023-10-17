package br.com.nataliafc.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nataliafc.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	private TaskRepository taskRepository;

	@PostMapping("/")
	public ResponseEntity<String> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {

		var userId = request.getAttribute("userId");
		taskModel.setUserId((UUID) userId);

		var currentDate = LocalDateTime.now();

		if (currentDate.isAfter(taskModel.getInitialDate())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A data de ínício / data de término deve ser maior que a data atual!");
		}

		if (currentDate.isAfter(taskModel.getFinalDate())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A data de ínício / data de término deve ser maior que a data atual!");
		}

		if (taskModel.getInitialDate().isAfter(taskModel.getFinalDate())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A data de ínício não pode ser posterior à data de término!");
		}

		var taskCreated = this.taskRepository.save(taskModel);
		return ResponseEntity.status(HttpStatus.OK).body("Tarefa inserida com sucesso!");
	}

	@GetMapping("/")
	public List<TaskModel> list(HttpServletRequest request) {
		var userId = request.getAttribute("userId");
		var tasks = this.taskRepository.findByUserId((UUID) userId);
		System.out.println(tasks);
		return tasks;
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@RequestBody TaskModel taskModel, @PathVariable UUID id,
			HttpServletRequest request) {
		// procure pelo id; se não encontrar, retorne null
		var task = this.taskRepository.findById(id).orElse(null);

		if (task == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada!");
		}

		var userId = request.getAttribute("userId");

		// se o userId da task no db for diferente do userId que ta tentando editar a
		// task, retorna 400
		if (!task.getUserId().equals(userId)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Usuário não tem permissão para alterar esta tarefa!");
		}

		// source: a fonte de dados que pretendo alterar (todas as taskModel);
		// target: o dado que pretendo alterar (a task com o id específico que to
		// procurando)
		Utils.copyNonNullProperties(taskModel, task);

		var taskUpdated = this.taskRepository.save(task);
		return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
	}

}
