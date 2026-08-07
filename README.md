# Virtual Queue Management System

This project is a virtual queue management application built to help businesses organize customer flow in a simple and modern way. Instead of customers waiting in a physical line, they can join a digital queue, track their position, and receive updates in real time.

The system is designed for three main user groups:

- Customers, who join the queue and check their status
- Staff, who manage the queue and call the next customer
- Admins, who oversee services, staff, and analytics

---

## Purpose of the Project

The main goal of this project is to make queue handling more transparent, efficient, and convenient. It is useful for environments such as hospitals, service centers, banks, support desks, and other businesses that deal with waiting customers.

By turning the queue into a digital system, the application helps reduce confusion, improves customer experience, and gives staff better control over the flow of service.

---

## What the System Does

The platform allows users to:

- join a queue for a specific service
- receive a token number
- view their position and estimated waiting time
- check their queue status using an access key
- cancel their queue spot if needed
- let staff call the next person in line
- let admins monitor services and queue activity

---

## Tech Stack

### Backend

- Java 17
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Spring Security
- Hibernate
- MySQL
- JWT for authentication
- WebSocket for real-time updates

### Frontend

- React 19
- TypeScript
- Vite
- React Router
- Axios
- SockJS and STOMP for real-time communication

### Development Tools

- Maven
- Lombok
- ESLint

---

## How the Queue Works

When a customer joins the queue, the system:

1. creates or opens a queue for the selected service
2. assigns a token number
3. generates an access key for later status checking
4. places the customer in the correct position based on priority

---

## Algorithms and Logic Implemented

### Priority-Based Queue Ordering

Customers are given a priority level of:

- emergency
- high
- normal

The queue ordering follows this logic:

- emergency customers are placed at the front
- high-priority customers are placed after emergencies
- normal customers are placed at the end

This allows urgent cases to move faster when necessary.

### ETA Estimation

The system estimates waiting time using:

- recent service history
- the number of active counters
- the customer’s current position in the queue

This makes queue movement more understandable for users.

### Position Recalculation

Whenever a customer is added, removed, or called forward, the system updates the queue positions and recalculates wait estimates so the information stays accurate.

### Token and Access Key Generation

Each new customer receives:

- a token number such as T001
- an access key used to check their current status later

---

## User Privileges and Roles

### Customers

Customers can:

- join a queue
- see their token number
- check their current status
- view their waiting position and ETA
- cancel their queue spot if they no longer need it

### Staff

Staff members can:

- view the live queue
- call the next customer
- help advance the service flow
- monitor the active queue for their assigned service

### Admins

Admins have the highest level of control. They can:

- manage services
- manage staff accounts
- view analytics and queue summaries
- oversee the overall flow of operations

---

## Database

The backend uses MySQL as the database. The application is configured to connect to a database named virtual_queue, and Hibernate is used to manage the data models.

Main entities include:

- Customer
- ServiceQueue
- Staff
- Admin
- Service
- ServiceHistory

This allows the system to store customer details, queue activity, staff information, and service history in a structured way.

---

## Real-Time Communication

The project uses WebSocket-based communication so queue updates can appear live. This helps staff and customers stay updated without repeatedly refreshing the page.

---

## Frontend Status and Future Layout

The frontend is still being developed and improved. The current layout focuses on core functionality, but the planned direction is a cleaner and more polished experience.

The future frontend design is expected to include:

- separate dashboards for customers, staff, and admins
- a modern and responsive interface
- real-time queue updates on screen
- clearer analytics visuals
- a smoother experience for joining and tracking queues

---

## Project Structure

- backend: Spring Boot application, REST APIs, queue logic, authentication, and database access
- frontend: React and TypeScript interface for users and staff

---

## Prerequisites

Before running the project locally, make sure you have:

- Java 17+
- MySQL 8+
- Maven
- Node.js and npm

---

## How to Run Locally

### Backend

1. Start MySQL
2. Create a database named virtual_queue
3. Update your database credentials in the backend configuration file
4. Run:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

### Frontend

1. Install dependencies:
   ```bash
   cd frontend
   npm install
   ```
2. Start the development server:
   ```bash
   npm run dev
   ```

---

## Summary

This project combines modern web development, real-time communication, and practical queue management into one simple system. It is designed to make service handling more organized, give customers better visibility, help staff work more efficiently, and give admins stronger control over operations.
