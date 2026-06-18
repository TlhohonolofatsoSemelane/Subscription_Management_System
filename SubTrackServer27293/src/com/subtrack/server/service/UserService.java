package com.subtrack.server.service;

import com.subtrack.server.model.User;

public interface UserService {

    User findById(Integer id);

    User findByUsername(String username);

    Integer create(User user);
}
