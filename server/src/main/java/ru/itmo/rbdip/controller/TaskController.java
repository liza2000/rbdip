package ru.itmo.rbdip.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.rbdip.repository.TagRepository;
import ru.itmo.rbdip.repository.TaskRepository;
import ru.itmo.rbdip.repository.entity.Tag;
import ru.itmo.rbdip.repository.entity.Task;
import ru.itmo.rbdip.repository.data.TaskData;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "**", maxAge = 3600)
@RequestMapping("/api/task")
@RestController
class TaskController {

    public TaskController(TaskRepository repository, TagRepository tagInterface) {
        this.repository = repository;
        this.tagRepository = tagInterface;
    }

    TaskRepository repository;

    TagRepository tagRepository;

    @GetMapping
    ResponseEntity<List<Task>> searchTasks(@RequestParam(required = false) List<String> tagTitles, @RequestParam(required = false) Integer count) {
        if (tagTitles==null || tagTitles.isEmpty())
            if (count==null)
                return new ResponseEntity<>(repository.findAll(Sort.by(Sort.Direction.ASC,"deadline")), HttpStatus.OK);
            else
                return new ResponseEntity<>(repository.findAll(PageRequest.of(0,count,Sort.by(Sort.Direction.ASC,"deadline"))).toList(), HttpStatus.OK);

        List<Tag> tags = tagRepository.findAllByTitleIn(tagTitles);
        List<Long> tagIds = new ArrayList<>();
        for(Tag tag: tags)
            tagIds.add(tag.getId());
        List<Task> tasks = repository.findDistinctByTagsContainingOrderByDeadline(tagIds);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Object> createTask(@RequestBody TaskData taskdata){
      List<String> tagDatas = taskdata.getTags();
      List<Tag> tags = tagRepository.findAllByTitleIn(tagDatas);
      for(String tagData: tagDatas){
          Tag tag = tagRepository.getByTitle(tagData);
          if (tag == null) {
              tag = tagRepository.save(new Tag(0L, tagData));
              tags.add(tag);
          }
      }

      Task task = new Task(0L,taskdata.getTitle(),taskdata.getDescription(),taskdata.getDeadline(),tags);
      try {
          task = repository.save(task);
      }catch (Exception e){
          return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>(task,HttpStatus.OK);
    }


}