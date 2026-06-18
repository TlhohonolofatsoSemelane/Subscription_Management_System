package com.subtrack.server.dao;

import com.subtrack.server.model.Subscription;
import java.util.List;

public interface SubscriptionDao {
    Integer save(Subscription subscription);
    void update(Subscription subscription);
    void delete(Subscription subscription);
    Subscription findById(Integer id);
    List<Subscription> findByUserId(Integer userId);
    List<Subscription> findAll();
}
