# 💼 FreelanceHubBackend — AI-Powered Freelance Matching Engine

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.3.x-green)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-brightgreen)
![Authentication](https://img.shields.io/badge/Auth-JWT-orange)
![Documentation](https://img.shields.io/badge/API-Swagger-blue)

FreelanceHubBackend is a production-ready backend system built using **Spring Boot**, **MongoDB**, and **JWT authentication**, enhanced with **AI-powered semantic matching** using Google Gemini text embeddings.

This backend powers a React-based freelancing platform and implements intelligent freelancer–project matching using vector similarity instead of traditional keyword matching.

---

# 🏗️ System Architecture

```
        ┌────────────────────┐
        │   React Frontend   │
        └─────────┬──────────┘
                  │ HTTP (REST)
                  ▼
        ┌────────────────────┐
        │ Spring Boot API    │
        │  (Controllers)     │
        └─────────┬──────────┘
                  │
                  ▼
        ┌────────────────────┐
        │ JWT Auth Filter    │
        │ (Security Layer)   │
        └─────────┬──────────┘
                  │
                  ▼
        ┌────────────────────┐
        │ Service Layer      │
        │ (Business Logic)   │
        └───────┬─────┬──────┘
                │     │
                │     ▼
                │  ┌────────────────────┐
                │  │ Gemini Embedding   │
                │  │ API Integration    │
                │  └────────────────────┘
                │
                ▼
        ┌────────────────────┐
        │ MongoDB Database   │
        │ (Users, Projects,  │
        │  Embeddings)       │
        └────────────────────┘
```
---

### Architecture Flow

Frontend (React)  
→ Spring Boot REST Controllers  
→ JWT Authentication Filter  
→ Service Layer  
→ AI Embedding Service (Gemini API)  
→ MongoDB Database  

---

# 🤖 AI-Powered Semantic Matching


<!-- DRAG & DROP AI FLOW IMAGE BELOW -->

<!-- Example after upload:
![AI Flow](./assets/ai-flow.png)
-->

---

## 🔍 How Matching Works

1. Combine:
   - Title
   - Description
   - Domain
   - Skills  

2. Generate high-dimensional embedding using **Gemini Embedding API**

3. Store embedding vector in MongoDB

4. Compute cosine similarity between vectors

5. Return ranked results

---

# 📊 Matching Algorithm

Cosine Similarity Formula:

```
cos(θ) = (A · B) / (||A|| ||B||)
```

### Why Cosine Similarity?

- Focuses on semantic direction, not magnitude
- Stable for NLP embeddings
- Deterministic and efficient
- Industry standard for vector similarity

---

# 🔐 Security Architecture

- JWT-based stateless authentication
- Custom `OncePerRequestFilter`
- Centralized whitelist for public endpoints
- Role-based access control
- Swagger endpoints excluded securely from JWT filter

---

# 📡 API Documentation (Swagger)

Swagger UI available locally:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📸 Swagger UI Preview

<img width="1679" height="775" alt="Screenshot 2026-02-27 231230" src="https://github.com/user-attachments/assets/3d632471-8755-4a29-8470-05133f671a55" />
<img width="1647" height="876" alt="Screenshot 2026-02-27 231244" src="https://github.com/user-attachments/assets/5f5bdb32-ebef-47a7-98df-4fb9056a6f11" />

---

# 🌐 Frontend Integration

This backend powers the React frontend:

https://github.com/akshaykhardekar10/FreelanceHub-Frontend


<img width="1894" height="904" alt="Screenshot 2025-05-04 160758" src="https://github.com/user-attachments/assets/36cdd53b-0eae-4fa2-97b7-eb4d382da55e" />

---

# 🚀 Core Backend Features

### Authentication & Security
- User signup/login
- JWT token generation
- Role-based authorization
- Stateless session management

### Business Logic
- Project posting
- Freelancer bidding
- Project assignment
- Status tracking

### AI Capabilities
- Gemini text embedding integration
- Vector storage in MongoDB
- Cosine similarity-based ranking
- Adaptive retry logic for API resilience

---

# 🛠 Tech Stack

### Backend
- Java 21
- Spring Boot 3.3.x
- Spring Security
- JWT
- MongoDB

### AI Integration
- Google Gemini Embedding API
- Vector similarity computation

### Documentation
- springdoc OpenAPI (Swagger)

---

# ⚙️ Running Locally

## Prerequisites

- Java 17+
- Maven 3.6+
- MongoDB (Local or Atlas)

---

## Setup

```bash
git clone https://github.com/akshaykhardekar10/FreelanceHubBackend.git
cd FreelanceHubBackend
mvn clean install
mvn spring-boot:run
```

Access Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

---

# 📈 Future Improvements

- MongoDB Atlas Vector Search
- ANN indexing (FAISS / Vector DB)
- Embedding caching layer
- Batch embedding optimization
- AI ranking model enhancements

---

# 🎯 Resume Highlight

Built a secure AI-powered backend system integrating semantic embeddings and cosine similarity for intelligent freelance matching using Spring Boot and MongoDB.

---

# 📬 Contact

**Akshay Khardekar**  
📧 khardekarakshay33@gmail.com  
🔗 GitHub: https://github.com/akshaykhardekar10  
🔗 LinkedIn: https://linkedin.com/in/akshaykhardekar
