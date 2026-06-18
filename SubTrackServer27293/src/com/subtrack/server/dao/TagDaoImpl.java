package com.subtrack.server.dao;

import com.subtrack.server.model.Tag;
import com.subtrack.server.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TagDaoImpl implements TagDao {

    @Override
    public Integer save(Tag tag) {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Integer id = (Integer) session.save(tag);
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
    public Tag findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return (Tag) session.get(Tag.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public Tag findByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("FROM Tag t WHERE t.name = :name");
            q.setParameter("name", name);
            return (Tag) q.uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tag> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Tag").list();
        } finally {
            session.close();
        }
    }
}
