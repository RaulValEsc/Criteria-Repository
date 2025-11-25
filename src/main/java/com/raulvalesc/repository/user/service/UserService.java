package com.raulvalesc.repository.user.service;

import com.raulvalesc.repository.shared.persistence.SearchResponse;
import com.raulvalesc.repository.shared.persistence.criteria.Criteria;
import com.raulvalesc.repository.user.dto.UserDto;
import com.raulvalesc.repository.user.model.User;
import com.raulvalesc.repository.user.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void create(UserDto userDto) {
        User user = new User();

        user.setName(userDto.getName());

        user.setEmail(userDto.getEmail());

        this.repository.create(user);
    }

    public void delete(Integer id) {
        User user = this.repository.getById(id);

        if (user == null) {
            return;
        }

        this.repository.delete(user);
    }

    public User findById(Integer id) {
        User user = this.repository.getById(id);

        if (user == null) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }

        return user;
    }

    public SearchResponse<User> searchByCriteria(Criteria criteria) {
        return this.repository.searchByCriteria(criteria);
    }

    public void update(UserDto userDto) {
        User user = this.repository.getById(userDto.getId());

        if (user == null) {
            throw new EntityNotFoundException("User with id " + userDto.getId() + " do not exist");
        }

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        this.repository.update(user);
    }
}
