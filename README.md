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
- activemq.bat start

### 3) Configure Database
- Create database in PostgreSQL
- Update Hibernate DB config in server (hibernate.cfg.xml)

## 4) Run Applications
- Start ServerMain.java
- Start ClientMain.java

### 🧪 Testing Approach
I used a top-down testing strategy:

- Unit-level checks for business rules in Service layer
- Integration testing for RMI + Hibernate + ActiveMQ flow
- End-to-End testing from registration/login to CRUD and export

### 📸 Screenshots

#### Dashboard

<img width="959" height="515" alt="image" src="https://github.com/user-attachments/assets/d61dc373-40a6-4350-8b55-68411b795573" />

### Manage Subscriptions

<img width="959" height="510" alt="image" src="https://github.com/user-attachments/assets/30e15134-0ab2-4406-bc64-60f3e2d09ec6" />

### Login 

<img width="959" height="505" alt="image" src="https://github.com/user-attachments/assets/caf462d2-079c-4bcd-8fc4-6d19ac554a27" />

<img width="959" height="513" alt="image" src="https://github.com/user-attachments/assets/55b095bd-ad5b-45fb-8eae-d3a3ab4d8b81" />

<img width="959" height="527" alt="image" src="https://github.com/user-attachments/assets/1198fa5d-021c-4517-8141-32e7aa5e5602" />



  

