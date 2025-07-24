# Qwizzle
This is a full-stack quiz application built using Spring Boot (Java) for the backend and React.js for the frontend. It supports dynamic quiz generation based on category and number of questions.

# Qwizzle – Spring Boot + React + PostgreSQL

A full-stack quiz application built using **Spring Boot (Backend)**, **React (Frontend)**, and **PostgreSQL (Database)**.

### 🔧 Features
- 📚 Dynamic category-based quiz generation
- ✍️ Add & fetch quiz questions with difficulty levels
- 🎯 Evaluate quiz responses and calculate scores
- 📡 REST API integration with CORS enabled



<img width="1365" height="638" alt="Screenshot 2025-07-24 174852" src="https://github.com/user-attachments/assets/5567fceb-e26a-4a2b-8ffa-31dfe5d87627" />
<img width="1365" height="653" alt="Screenshot 2025-07-24 175018" src="https://github.com/user-attachments/assets/1b2ad447-4246-4864-bbe6-e497e409f5e4" />


### 📦 Tech Stack
- ⚙️ Backend: Java 17, Spring Boot 3.x, Spring Security, JPA, Hibernate
- 💾 Database: PostgreSQL
- 🌐 Frontend: React, Bootstrap
- 📡 Communication: REST APIs (CORS enabled)

---

### 🚀 How to Run Locally

#### 1. Start PostgreSQL
Make sure PostgreSQL is running and credentials are set in `application.properties`.

#### 2. Run Spring Boot
```bash
mvn clean spring-boot:run
