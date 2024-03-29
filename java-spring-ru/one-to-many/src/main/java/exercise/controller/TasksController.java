package exercise.controller;

import java.util.List;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.mapper.UserMapper;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    // BEGIN

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping(path="")
    public List<TaskDTO> show(@RequestParam (defaultValue = "10") Long limit){
        return taskRepository.findAll().stream()
                .limit(limit).map(p -> taskMapper.map(p)).toList();
    }

    @GetMapping(path="/{id}")
    public TaskDTO index(@PathVariable Long id){
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return taskMapper.map(task);
    }

    @PostMapping(path="")
    public ResponseEntity<TaskDTO> create(@RequestBody TaskCreateDTO dto){
        var task = taskMapper.map(dto);
        var assignee = userRepository.findById(dto.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignee with id " + dto.getAssigneeId() + " not found"));
        taskRepository.save(task);
        assignee.addTask(task);
        userRepository.save(assignee);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.map(task));
    }

    @PutMapping(path="/{id}")
    public TaskDTO update(@PathVariable Long id, @RequestBody TaskUpdateDTO data){
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        var newAssignee = userRepository.findById(data.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignee with id " + data.getAssigneeId() + " not found"));
        taskMapper.update(data, task);
        taskRepository.save(task);

        newAssignee.addTask(task);
        userRepository.save(newAssignee);

        return taskMapper.map(task);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path="/{id}")
    public void delete(@PathVariable Long id){
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        var assignee = task.getAssignee();
        assignee.removeTask(task);

        userRepository.save(assignee);

        taskRepository.delete(task);
    }
    // END
}
