package com.subtrack.server.dao;

import com.subtrack.server.model.Tag;
import java.util.List;

public interface TagDao {
    Integer save(Tag tag);
    Tag findById(Integer id);
    Tag findByName(String name);
    List<Tag> findAll();
}
