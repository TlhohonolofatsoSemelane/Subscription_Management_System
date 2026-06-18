package com.subtrack.server.dao;

import com.subtrack.server.model.Category;
import java.util.List;

public interface CategoryDao {
    Integer save(Category category);
    Category findById(Integer id);
    Category findByName(String name);
    List<Category> findAll();
}
