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

## Demo Accounts

The backend seeds these accounts and sample services when the database is empty:

| Role  | Email             | Password   |
| ----- | ----------------- | ---------- |
| Admin | `admin@queue.com` | `admin123` |
| Staff | `staff@queue.com` | `staff123` |

These credentials are for local development only. Change or remove the seeded credentials before deploying the application.

---

## Project Structure

- `backend/`: Spring Boot REST API, authentication, queue services, database access, and WebSocket endpoint
- `frontend/`: React and TypeScript application for customers, staff, and administrators

---

## Prerequisites

Before running the project locally, make sure you have:

- Java 17+
- MySQL 8+
- Node.js 20.19+ or 22.12+, and npm (required by Vite 8)

The backend includes a Maven wrapper, so a separate Maven installation is not required.

## How to Run Locally

### 1. Configure MySQL and the backend

Create the database and copy the sample configuration to the local configuration file. Run these commands from the repository root in PowerShell:

```powershell
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS virtual_queue;"
Copy-Item backend/src/main/resources/application-sample.properties backend/src/main/resources/application.properties
```

Edit `backend/src/main/resources/application.properties` with your MySQL username and password. Set `jwt.secret` to a private random value of at least 32 characters. This local file is ignored by Git; do not commit credentials.

Start the backend from the repository root:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

In Git Bash or a Unix shell, use `./mvnw spring-boot:run`. The API listens on port `8081` by default.

### 2. Configure and run the frontend

Create `frontend/.env.local` with the local backend URLs:

```dotenv
VITE_API_URL=http://localhost:8081/api
VITE_WS_URL=http://localhost:8081/ws
```

Then, in a second terminal, run:

```bash
cd frontend
npm install
npm run dev
```

Open the local URL printed by Vite, usually `http://localhost:5173`.

## Validation

Run the backend tests from the repository root:

```powershell
cd backend
.\mvnw.cmd test
```

Run the frontend checks from the repository root:

```bash
cd frontend
npm run lint
npm run build
```

## Summary

Virtual Queue Management provides customers with a way to join and track service queues, staff with tools to advance the queue, and administrators with service and analytics controls. The React frontend communicates with a Spring Boot API over HTTP and receives live queue updates over WebSockets.
