package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {
	
  @Autowired
  private TodoRepository repository;
	
  public List<TodoEntity> create(final TodoEntity entity) {
	
    validate(entity);
	
	repository.save(entity);
	
	return repository.findByUserId(entity.getUserId());
  }	
  
  public List<TodoEntity> retrieve(final String userId) {
	  return repository.findByUserId(userId);
  }
  
  public List<TodoEntity> update(final TodoEntity entity) {
	  // (1)
	  validate(entity);
	  
	  // (2) 
	  Optional<TodoEntity> original = repository.findById(entity.getId());
	  
	  //(3)
	  original.ifPresent(todo -> {
		  todo.setTitle(entity.getTitle());
		  todo.setDone(entity.isDone());
		  repository.save(todo);
	  }
			  
	  );
	  
	  return retrieve(entity.getUserId());
	  
	  
  }
  
  public List<TodoEntity> delete(final TodoEntity entity) {
	  //(1)
	  validate(entity);
	  
	  //(2)
	  try {
		  repository.delete(entity);
	  } catch(Exception e) {
		  log.error("error deleting entity", entity.getId(), e);
		  throw new RuntimeException("error deleting entity" + entity.getId());
	  }
	  
	  return retrieve(entity.getUserId());
	  
  }
  
  private void validate(final TodoEntity entity) {
		if(entity == null) {
			throw new RuntimeException("Entity cannot be null.");
		}
		
		if(entity.getUserId() == null) {
			throw new RuntimeException("Unknow user.");
		} 
  }

}











