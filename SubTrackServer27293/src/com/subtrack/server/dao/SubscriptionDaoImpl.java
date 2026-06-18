package com.subtrack.server.dao;

import com.subtrack.server.model.Subscription;
import com.subtrack.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class SubscriptionDaoImpl implements SubscriptionDao {

    @Override
    public Integer save(Subscription subscription) {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Integer id = (Integer) session.save(subscription);
            tx.commit();

            Integer uid = (subscription.getUser() != null) ? subscription.getUser().getId() : null;
            System.out.println("DAO SAVE -> subId=" + id + ", userId=" + uid);
            return id;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }


    @Override
public void update(Subscription subscription) {
    Transaction tx = null;
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
        tx = session.beginTransaction();

        Subscription managed = (Subscription) session.get(Subscription.class, subscription.getId());
        if (managed == null) {
            throw new RuntimeException("Subscription not found id=" + subscription.getId());
        }

        managed.setServiceName(subscription.getServiceName());
        managed.setAmount(subscription.getAmount());
        managed.setBillingCycle(subscription.getBillingCycle());
        managed.setNextBillingDate(subscription.getNextBillingDate());

        tx.commit();
        System.out.println("DAO UPDATE -> subId=" + subscription.getId());
    } catch (Exception e) {
        if (tx != null) tx.rollback();
        throw e;
    } finally {
        session.close();
    }
}


    @Override
    public void delete(Subscription subscription) {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Subscription managed = (Subscription) session.get(Subscription.class, subscription.getId());
            if (managed != null) {
                session.delete(managed);
            }
            tx.commit();
            System.out.println("DAO DELETE -> subId=" + subscription.getId());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Subscription findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return (Subscription) session.get(Subscription.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Subscription> findByUserId(Integer userId) {
        if (userId == null) return Collections.emptyList();

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        Object db = session.createSQLQuery("select current_database()").uniqueResult();
        System.out.println("DAO DB => " + db);

        List<Subscription> list = session.createQuery(
                "SELECT s FROM Subscription s " +
                "JOIN FETCH s.user u " +
                "WHERE u.id = :uid " +
                "ORDER BY s.id DESC"
        ).setParameter("uid", userId).list();

        System.out.println("DAO FIND_BY_USER -> userId=" + userId + ", rows=" + list.size());
        return list;
    } catch (Exception e) {
        System.out.println("DAO FIND_BY_USER ERROR -> userId=" + userId + ", err=" + e.getMessage());
        e.printStackTrace();
        throw e;
    } finally {
        session.close();
    }
}


    @Override
    @SuppressWarnings("unchecked")
    public List<Subscription> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Subscription s ORDER BY s.id DESC").list();
        } finally {
            session.close();
        }
    }
}
