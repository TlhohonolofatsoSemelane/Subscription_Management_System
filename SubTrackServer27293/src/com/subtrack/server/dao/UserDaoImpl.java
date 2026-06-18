package com.subtrack.server.dao;

import com.subtrack.server.model.User;
import com.subtrack.server.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDaoImpl implements UserDao {

    @Override
    public Integer save(User user) {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Integer id = (Integer) session.save(user);
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
    public void update(User user) {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public User findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return (User) session.get(User.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public User findByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("FROM User u WHERE u.username = :username");
            q.setParameter("username", username);
            return (User) q.uniqueResult();
        } finally {
            session.close();
        }
    }
}
