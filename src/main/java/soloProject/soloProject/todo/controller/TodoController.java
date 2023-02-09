package soloProject.soloProject.todo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import soloProject.soloProject.todo.entity.Todos;
import soloProject.soloProject.todo.exception.LogicException;
import soloProject.soloProject.todo.exception.LogicExceptionEnum;
import soloProject.soloProject.todo.repository.TodoRepository;

import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@Validated
public class TodoController {
    private TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity postTodo(@RequestParam String title, @RequestParam int todoOrder,
                                   @RequestParam boolean completed){
        Todos todos = Todos.builder().title(title).todoOrder(todoOrder).completed(completed).build();
        Todos createdTodos = repository.save(todos);
        URI location = UriComponentsBuilder.newInstance().path("/"+createdTodos.getId()).build().toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity patchTodo(@PathVariable("id") Integer id, @RequestParam @Nullable String title,
                                    @RequestParam @Nullable Integer todoOrder,
                                    @RequestParam @Nullable boolean completed){
        Todos foundTodos = findTodos(id);

        Optional.ofNullable(title).ifPresent(foundTodos::setTitle);
        Optional.ofNullable(todoOrder).ifPresent(foundTodos::setTodoOrder);
        Optional.ofNullable(completed).ifPresent(foundTodos::setCompleted);

        return new ResponseEntity(repository.save(foundTodos), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity getTodo(@PathVariable("id") Integer id){
        Todos todos = findTodos(id);
        return new ResponseEntity(todos, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getTodos(@RequestParam @Positive int page, @RequestParam @Positive int size){
        Pageable pageable = (Pageable) PageRequest.of(page-1, size);
        Page<Todos> todosPage = repository.findAll(pageable);
        List<Todos> todosList = todosPage.getContent();
        return new ResponseEntity(todosList, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteTodo(@PathVariable("id") Integer id){
        Todos todos = findTodos(id);
        repository.delete(todos);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity deleteTodos(){
        repository.deleteAll();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private Todos findTodos(int id) {
        Optional<Todos> todos = repository.findById(id);
        Todos foundTodos = todos.orElseThrow(()->new LogicException(LogicExceptionEnum.TODO_NOT_FOUND));
        return foundTodos;
    }
}
