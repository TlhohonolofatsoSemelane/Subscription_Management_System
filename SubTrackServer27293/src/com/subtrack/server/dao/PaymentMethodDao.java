package com.subtrack.server.dao;

import com.subtrack.server.model.PaymentMethod;
import java.util.List;

public interface PaymentMethodDao {
    Integer save(PaymentMethod paymentMethod);
    PaymentMethod findById(Integer id);
    List<PaymentMethod> findAll();
}
