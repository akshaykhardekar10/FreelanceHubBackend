
# 💼 FreelanceHubBackend

**FreelanceHubBackend** is the backend service for a freelancing platform, built using **Spring Boot** and **MongoDB**. It supports a React-based frontend and provides RESTful APIs for user management, project listings, bids, and more.

---

## 📑 Table of Contents

- [🚀 Features](#-features)
- [🛠️ Tech Stack](#-tech-stack)
- [⚙️ Getting Started](#-getting-started)
- [📡 API Endpoints](#-api-endpoints)
- [📁 Project Structure](#-project-structure)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [📬 Contact](#-contact)

---

## 🚀 Features

- User registration and authentication (JWT-based)
- Role-based access for freelancers and clients
- Project posting and management
- Bidding system for freelancers
- Project assignment and status tracking
- RESTful API for frontend integration
- MongoDB for scalable data storage

---

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot
- **Database:** MongoDB
- **Frontend:** React (See [FreelanceHubFrontend](https://github.com/akshaykhardekar10/FreelanceHub-Frontend))

---

## ⚙️ Getting Started

### ✅ Prerequisites

- Java 17+
- Maven 3.6+
- MongoDB running locally or via cloud

### 📦 Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/akshaykhardekar10/FreelanceHubBackend.git
   cd FreelanceHubBackend
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API:**
   ```
   http://localhost:8080/api/
   ```

---

## 📡 API Endpoints (Sample)

| Method | Endpoint                   | Description                        |
|--------|----------------------------|------------------------------------|
| POST   | `/api/auth/signup`         | Register a new user                |
| POST   | `/api/auth/login`          | Authenticate user and return JWT   |
| GET    | `/api/projects`            | List all available projects        |
| POST   | `/api/projects`            | Post a new project                 |
| POST   | `/api/bids/{projectId}`    | Submit a bid on a project          |
| GET    | `/api/users/{userId}`      | Fetch user profile info            |

---

## 📁 Project Structure

```
FreelanceHubBackend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/freelancehub/
│   │   │       ├── controller/        # REST Controllers
│   │   │       ├── model/             # Domain models
│   │   │       ├── repository/        # MongoDB repositories
│   │   │       ├── service/           # Business logic
│   │   │       └── config/            # Security and app config
│   └── resources/
│       └── application.properties     # Spring Boot configuration
├── pom.xml
└── README.md
```

---

## 🤝 Contributing

Contributions are welcome! Follow the steps below:

1. Fork the repository
2. Create a new branch:
   ```bash
   git checkout -b feature/YourFeature
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add new feature"
   ```
4. Push to your branch:
   ```bash
   git push origin feature/YourFeature
   ```
5. Open a Pull Request

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 📬 Contact

**Akshay Khardekar**  
📧 khardekarakshay33@gmail.com  
🔗 [GitHub](https://github.com/akshaykhardekar10) • [LinkedIn](https://linkedin.com/in/akshaykhardekar)

---
