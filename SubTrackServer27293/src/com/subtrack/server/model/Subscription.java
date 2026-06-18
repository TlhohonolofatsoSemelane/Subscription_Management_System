package com.subtrack.server.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Column(name = "name", nullable = false, length = 100)
    private String serviceName;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "frequency", nullable = false, length = 20)
    private String billingCycle;

    @Temporal(TemporalType.DATE)
    @Column(name = "next_billing_date", nullable = false)
    private Date nextBillingDate;

    @Column(name = "status", length = 20)
    private String status;

    public Subscription() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getBillingCycle() { return billingCycle; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }

    public Date getNextBillingDate() { return nextBillingDate; }
    public void setNextBillingDate(Date nextBillingDate) { this.nextBillingDate = nextBillingDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
