package com.raulvalesc.repository.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.raulvalesc.repository.shared.persistence.SearchResponse;
import com.raulvalesc.repository.shared.persistence.criteria.Criteria;
import com.raulvalesc.repository.shared.persistence.criteria.CriteriaMapper;
import com.raulvalesc.repository.user.dto.UserDto;
import com.raulvalesc.repository.user.model.User;
import com.raulvalesc.repository.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody UserDto userDto) {
        this.service.create(userDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        this.service.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        User user = this.service.findById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SearchResponse<User>> searchByCriteria(@RequestBody JsonNode criteriaJson) {
        Criteria criteria = CriteriaMapper.fromJson(criteriaJson);

        SearchResponse<User> searchResponse = this.service.searchByCriteria(criteria);

        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody UserDto userDto, @PathVariable Integer id) {
        userDto.setId(id);

        this.service.update(userDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
