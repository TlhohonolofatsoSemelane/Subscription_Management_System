package com.subtrack.server.dao;

import com.subtrack.server.model.PaymentMethod;
import com.subtrack.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PaymentMethodDaoImpl implements PaymentMethodDao {

    @Override
    public Integer save(PaymentMethod paymentMethod) {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            Integer id = (Integer) session.save(paymentMethod);
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
    public PaymentMethod findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return (PaymentMethod) session.get(PaymentMethod.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PaymentMethod> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM PaymentMethod").list();
        } finally {
            session.close();
        }
    }
}
