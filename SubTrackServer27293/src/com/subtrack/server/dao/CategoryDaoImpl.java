package com.subtrack.server.dao;

import com.subtrack.server.model.Category;
import com.subtrack.server.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    @Override
    public Integer save(Category category) {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Integer id = (Integer) session.save(category);
            tx.commit();
            return id;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Category findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return (Category) session.get(Category.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public Category findByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("FROM Category c WHERE c.name = :name");
            q.setParameter("name", name);
            return (Category) q.uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Category> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Category").list();
        } finally {
            session.close();
        }
    }
}
