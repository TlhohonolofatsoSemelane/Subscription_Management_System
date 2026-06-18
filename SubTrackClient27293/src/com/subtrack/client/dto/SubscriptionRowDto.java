package com.subtrack.client.dto;

public class SubscriptionRowDto {
    private int id;
    private String name;
    private double amount;
    private String billingCycle;
    private String nextPaymentDate;

    public SubscriptionRowDto(int id, String name, double amount, String billingCycle, String nextPaymentDate) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.billingCycle = billingCycle;
        this.nextPaymentDate = nextPaymentDate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getAmount() { return amount; }
    public String getBillingCycle() { return billingCycle; }
    public String getNextPaymentDate() { return nextPaymentDate; }
}
