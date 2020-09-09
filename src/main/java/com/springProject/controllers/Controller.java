package com.springProject.controllers;

import com.springProject.entities.SimpleEntity;
import com.springProject.models.RequestModel;
import com.springProject.models.SimpleEntityModel;
import com.springProject.models.UpdateModel;
import com.springProject.services.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private final EntityService entityService;

    @Autowired
    Controller(EntityService entityService){
        this.entityService = entityService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody SimpleEntityModel simpleEntityModel){
        SimpleEntity simpleEntity =  entityService.save(simpleEntityModel);
        return new ResponseEntity<>(simpleEntity.getId().toString(), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody UpdateModel simpleEntityModel){
        return entityService.update(simpleEntityModel);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        return entityService.delete(id);
    }
    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<SimpleEntity> getEntities(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "limit", defaultValue = "5") int limit,
                                        @RequestBody RequestModel requestModel) {
        Pageable pageableRequest = PageRequest.of(page, limit);
        return entityService.get(requestModel, pageableRequest);
    }
}
