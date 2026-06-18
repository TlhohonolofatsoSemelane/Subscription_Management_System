package com.subtrack.server.dao;

import com.subtrack.server.model.User;

public interface UserDao {
    Integer save(User user);
    User findById(Integer id);
    User findByUsername(String username);

    // add this for OTP save/clear
    void update(User user);
}
