# Employee Management System - Secure REST API

A robust and secure Backend REST API built with Java and Spring Boot. This system manages Employees and Departments, featuring robust relationship mapping (One-to-Many) and strict security protocols using Spring Security and JSON Web Tokens (JWT).

## 🚀 Key Features

* **JWT Authentication:** Secure login and token-based access for all protected API endpoints.
* **Role-Based Access Control (RBAC):** Distinct privileges for `ADMIN` and `USER` roles.
    * *Admins* can create and delete departments.
    * *Users* can only view available departments and their profiles.
* **Entity Relationships:** Bi-directional `One-ToMany` mapping between Departments and Employees.
* **Clean JSON Responses:** Optimized data payloads using `@JsonIgnore` and `@JsonManagedReference` to prevent infinite recursion and hide sensitive fields.

## 🛠️ Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **Security:** Spring Security, JWT (io.jsonwebtoken)
* **Database Tooling:** Spring Data JPA, Hibernate
* **Database:** MySQL (or H2 Database for local testing)
* **Build Tool:** Maven

## 🔌 API Endpoints

### Authentication
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/signup` | Register a new Employee/Admin | Public |
| `POST` | `/auth/login` | Authenticate user and get JWT | Public |

### Departments
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/departments` | Create a new department | **ADMIN** Only |
| `GET` | `/api/departments` | Get all departments with their employees | ADMIN / USER |
| `DELETE` | `/api/departments/{id}`| Delete a department | **ADMIN** Only |

### Employees
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/employees` | Get list of all employees | ADMIN / USER |

## ⚙️ Setup & Installation

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/YourUsername/Employee-Management-System-SpringBoot.git](https://github.com/YourUsername/Employee-Management-System-SpringBoot.git)
