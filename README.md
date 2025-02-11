# 🎸 BandUp – A Social Network for Musicians
## 📌 About
**BandUp** is a social networking app designed for musicians looking to connect, collaborate,
form bands, and share their music and skills. 
The app enables users to find musicians based on instruments, 
music genres, and location.
It also includes social features such as chat, posts, 
and a system for making connections and following other users.

## 📂 Related Repositories
- **Backend:** (this repo)
- **Frontend:** (TBA)

## 🚀 Features
- ### 🔍 User Management
    - User registration and login with JWT authentication
    - Profile editing, including adding a bio, profile picture, instruments, and music genres
    - Ability to update location in the profile
- ### 🎵 Band Management
    - Band creation
    - Managing band members, including adding and removing members
    - Editing band information, such as name, description, and other details
- ### 📨 Messaging
    - Private chat between users (WebSockets)
    - Group chat for bands (WebSockets)
    - Real-time notifications (WebSockets) – **TBA**
- ### 📢 Posts
    - Posting text and multimedia content, including photos, audio, and videos
    - Like and comment system for user engagement
- ### 👥 Interactions
    - Friends system, including sending, accepting, and rejecting invitations
    - Ability to follow other users
    - Blocking unwanted users
- ### 🔎 Search & Recommendations
    - Advanced search system for finding users and bands
    - Musician recommendation algorithm based on location, music genres, and instruments

## 🛠️ Tech Stack
- ### ⚙️ Backend
    - Java + Spring Boot
    - PostgreSQL + Liquibase
    - WebSockets (STOMP)
    - Firebase Cloud Storage
    - REST API
    - JWT Authentication
    - Hexagonal Architecture
    - Swagger
- ### 📱 Frontend
    - Flutter
    - BLoC

## 🛠️ Installation & Running
- ### 📌 Requirements
    - Java 17
    - Maven
    - Docker
    - PostgreSQL
    - Firebase account
- ### 🚀 Running Backend
    - Clone the repository
      ```bash
      git clone https://github.com/kacpi-k/BandUp-backend.git
      cd bandup-backend
      ```
    - Configure environment variables
        - Create a `.env` file in the project's root directory (following the template provided in `.env.example`) 
        - Create a `serviceAccountKey.json` file in the project's root directory (instructions provided in `serviceAccountKey.json.example`)
      <br /><br />
    - Run PosrgreSQL DB via Docker
      ```bash
      docker-compose up -d
      ```
    - Run the backend
      ```bash
      mvn spring-boot:run
      ```
      
## 📝 API Documentation
- Swagger available at: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)