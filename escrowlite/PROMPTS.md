# EscrowLite Project - Development Steps & Prompt History

## Project Summary
I built and set up the EscrowLite application using Spring Boot 3 and added a dark-mode frontend dashboard connected with REST APIs.

---

## Step-by-Step Prompt History

### Step 1: Backend Setup & Port Fix
"Help me set up and run the EscrowLite Spring Boot project locally. Update pom.xml to Spring Boot 3.2.5, fix port 8080 conflicts by changing server.port=8081 in application.properties, build the project with ./mvnw clean verify, and check if the backend starts without errors."

### Step 2: Dark Mode UI Dashboard
"Create a modern dark-mode index.html inside src/main/resources/static/ using Tailwind CSS. Connect it with the backend API so I can create escrow agreements, filter statuses, request rework, and approve milestone payouts."

### Step 3: Clearing Port Conflicts
"Kill any background Java processes running on port 8081, make sure application.properties is set to port 8081, and run the server again using .\mvnw spring-boot:run."

### Step 4: Complete Code Check & Git Push
"Please check the whole project end-to-end:
1. Stop any background Java process on port 8081.
2. Check all Java files (Controllers, Services, Repositories, DTOs, Models) for any compilation or code errors and fix them.
3. Make sure index.html frontend works properly with all REST endpoints.
4. Run the project to confirm http://localhost:8081/ is live.
5. Commit all changes and push the code directly to my GitHub main branch."

---

## Submission Details
* GitHub Repo: https://github.com/dhanusreen2025cse/escrowlite-backend.git
* Live UI Dashboard: http://localhost:8081/
* H2 Database Console: http://localhost:8081/h2-console
