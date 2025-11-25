package com.raulvalesc.repository.user.persistence;

import com.raulvalesc.repository.shared.persistence.MyCriteriaRepository;
import com.raulvalesc.repository.shared.persistence.SearchResponse;
import com.raulvalesc.repository.shared.persistence.criteria.Criteria;
import com.raulvalesc.repository.user.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends MyCriteriaRepository<User, Integer> {
    public UserRepository() {
        super(User.class);
    }

    @Transactional
    public void create(User user) {
        this._save(user);
    }

    @Transactional
    public void delete(User user) {
        this._remove(user);
    }

    public User getById(Integer id) {
        return this._findById(id);
    }

    public SearchResponse<User> searchByCriteria(Criteria criteria) {
        return this._searchByCriteria(criteria);
    }

    @Transactional
    public void update(User user) {
        this._update(user);
    }
}
