# Subscription_Management_System

# SubTrack – Subscription Management System

SubTrack is a Java desktop-based subscription management system that helps users track recurring payments, manage subscription records, and export reports.  
It is built using a client-server architecture with Java RMI, Hibernate ORM, PostgreSQL, and ActiveMQ for asynchronous OTP notification flow.

---

## 🚀 Features

- User Registration & Login
- OTP-based authentication workflow (ActiveMQ event pipeline)
- Subscription CRUD (Create, Read, Update, Delete)
- JTable-based subscription listing with refresh
- Export subscriptions to CSV and PDF
- Layered architecture (UI, Service, DAO, Messaging)

---

## 🏗️ Tech Stack

| **Layer** | **Technology** |
|---|---|
| Language | Java |
| UI | Java Swing |
| Remote Communication | Java RMI |
| ORM/Persistence | Hibernate |
| Database | PostgreSQL |
| Messaging | Apache ActiveMQ (JMS) |
| Build Tool | Maven / Ant (depending on your setup) |

---

## 🧠 Architecture Overview

- **Client App**: Swing UI for user interactions  
- **Server App**: Business logic, persistence, and messaging  
- **Database**: Stores users and subscription records  
- **Message Broker**: ActiveMQ handles OTP notification events asynchronously

---

## ✅ Current Scope (MVP)

### Implemented
- Auth flow with OTP event generation
- Subscription CRUD operations
- Export to CSV/PDF
- Core validations in Service layer

### Deferred / Future Work
- Full UI integration for Category, Tag, and PaymentMethod entities
- Real SMTP email delivery integration for OTP (currently message-consumer simulation/log mode in some builds)

---

## ⚙️ Setup Instructions

### 1) Prerequisites
- JDK 8+  
- PostgreSQL running  
- Apache ActiveMQ running on `tcp://localhost:61616`  
- IDE (NetBeans/IntelliJ/Eclipse)

### 2) Start ActiveMQ
```bash
activemq.bat start
