package com.subtrack.server.rmi.dto;

import java.io.Serializable;

public class SubscriptionDto implements Serializable {
    private Integer id;
    private Integer userId;
    private String name;
    private Double amount;
    private String billingCycle;
    private String nextPaymentDate; // yyyy-MM-dd

    public SubscriptionDto() {}

    public SubscriptionDto(Integer id, Integer userId, String name, Double amount, String billingCycle, String nextPaymentDate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.billingCycle = billingCycle;
        this.nextPaymentDate = nextPaymentDate;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getBillingCycle() { return billingCycle; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }

    public String getNextPaymentDate() { return nextPaymentDate; }
    public void setNextPaymentDate(String nextPaymentDate) { this.nextPaymentDate = nextPaymentDate; }
}
