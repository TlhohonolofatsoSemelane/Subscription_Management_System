package com.subtrack.server.service.impl;

import com.subtrack.server.dao.UserDao;
import com.subtrack.server.model.User;
import com.subtrack.server.service.UserService;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User findById(Integer id) {
        if (id == null) throw new IllegalArgumentException("User id is required.");
        return userDao.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        return userDao.findByUsername(username.trim());
    }

    @Override
    public Integer create(User user) {
        if (user == null) throw new IllegalArgumentException("User is required.");
        return userDao.save(user);
    }
}
