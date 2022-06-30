package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	@PostMapping
	public ResponseEntity<?> createTodo(
			@AuthenticationPrincipal String userId,
			@RequestBody TodoDTO dto) {
		try {
			//String temporaryUserId = "temporary-user";
			
			//(1)
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			//(2)
			entity.setId(null);
			
			//(3)
			//entity.setUserId(temporaryUserId);
			entity.setUserId(userId);
			
			//(4)
			List<TodoEntity> entities = service.create(entity);
			
			//(5)
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//(6)
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			//(7)
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
			
			
		}
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
		//String temporaryUserId = "temporary-user";
		
		// (1)
		//List<TodoEntity> entities = service.retrieve(temporaryUserId);
		List<TodoEntity> entities = service.retrieve(userId);
		
		// (2)
		List<TodoDTO> dtos = entities.stream().map((e)->(new TodoDTO(e))).collect(Collectors.toList());

		// (3)
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

		//(4)
		return ResponseEntity.ok().body(response);	
	}

	@PutMapping
	public ResponseEntity<?> updateTodo(
			@AuthenticationPrincipal String userId,
			@RequestBody TodoDTO dto) {
		//String temporaryUserId = "temporary-user";
		
		//(1)
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		//(2)
		//entity.setUserId(temporaryUserId);
		entity.setUserId(userId);
		
		//(3)
		List<TodoEntity> entities = service.update(entity);
		
		// (4)
		List<TodoDTO> dtos = entities.stream().map((e)->(new TodoDTO(e))).collect(Collectors.toList());

		// (5)
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

		//(6)
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(
			@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
		try {
			//String temporaryUserId = "temporary-user";
			
			TodoEntity entity = TodoDTO.toEntity(dto);
			//entity.setUserId(temporaryUserId);
			entity.setUserId(userId);
			
			List<TodoEntity> entities = service.delete(entity);
			
			// (4)
			List<TodoDTO> dtos = entities.stream().map((e)->(new TodoDTO(e))).collect(Collectors.toList());

			// (5)
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

			//(6)
			return ResponseEntity.ok().body(response);
			
			
		} catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
			
			
		}
	}
}










