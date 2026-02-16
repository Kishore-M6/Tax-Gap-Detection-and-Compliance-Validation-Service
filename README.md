#  Tax Gap Detection & Compliance Validation Service


A **Spring Boot backend system** designed for tax auditors to:

1. Validate financial transactions  
2. Compute expected taxes  
3. Detect compliance violations  
4. Execute configurable tax rules  
5. Generate exception records  
6. Maintain a complete audit trail  
7. Provide analytical summary reports  

---

# 1.Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA / Hibernate**
- **MySQL**
- **Maven**
- **JUnit + Mockito**

---

# 2.How to Run the Application

### 1️.Clone Repository

```bash
git clone https://github.com/Kishore-M6/Tax-Gap-Detection-and-Compliance-Validation-Service.git
cd Tax_Gap_Detection_and_Compliance_Validation_Service
```

---

### 2️.Configure Database

Update **application.yaml**

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/test_db
    username: root
    password: root
```

---

### 3️.Create Database Schema (MySQL)

```sql
CREATE DATABASE test_db;
```

Hibernate will automatically create tables because:

```yaml
spring.jpa.hibernate.ddl-auto: update
```
#### Database Schema

The database schema is provided in multiple formats:

• `database-schema.png` → Visual ER diagram  
• `docs/schema-sql/` → Executable DDL scripts Folder to recreate the database  
• `database_schema_diagram.mwb` → MySQL Workbench model (optional)

Use schema-sql Folder for quick setup .

---

### 4️.Build Project

```bash
mvn clean install
```

---

### 5️.Run Application

```bash
mvn spring-boot:run
```

OR

```bash
java -jar target/<generated-jar-file>.jar
```

### Application runs on:

```
http://localhost:9099
```
---

# 3.API Documentation(Sample Curl/Postman calls)
---
## 1.Upload Transactions

### Endpoint
```http
POST /api/transactions/upload
```

### Full URL
```
POST http://localhost:9099/api/transactions/upload
```

### Description
Uploads a batch of financial transactions for:

- Validation  
- Tax computation  
- Compliance checks  
- Rule execution  
- Exception generation
- Persisting into DB  

---

## 2.Exceptions API

### Base Endpoint
```http
GET /api/exceptions
```

```
http://localhost:9099/api/exceptions
```

---

### 1.Get All Exceptions

```http
GET /api/exceptions/getAll
```

---

### 2.Filter Exceptions

#### i.By Customer ID
```http
GET /api/exceptions?customerId=CUST101
```

#### ii.By Severity
```http
GET /api/exceptions?severity=HIGH
```

#### iii.By Rule Name
```http
GET /api/exceptions?ruleName=HighValueRule
```

#### iv.Filter Using Multiple Parameters
```http
GET /api/exceptions/filter?customerId=CUST101&severity=HIGH&ruleName=HighValueRule
```

 Enables precise exception analysis for auditors.

---

## 3.Reporting APIs

### Base Endpoint
```http
GET /api/reports
```

---

### i.Customer Tax Summary Report

```http
GET /api/reports/customer-tax-summary-report
```

#### Provides:

- Total transaction amount  
- Total reported tax  
- Total expected tax  
- Total tax gap  
- Compliance score  

 Ideal for evaluating customer-level tax behavior.

---

### ii.Exception Summary Reports

#### a.Total Exceptions
```http
GET /api/reports/total-exceptions
```

#### b.Severity-wise Exception Count
```http
GET /api/reports/count-of-exceptions-by-severity
```

#### c.Customer-wise Exception Count
```http
GET /api/reports/count-of-exceptions-by-customer-wise
```

---

## 4.Validation Scenarios

---

### i.Duplicate Transaction

If a transaction with the same **transactionId** is uploaded twice:

#### Example Request
```json
[ {
      "transactionId": "TXN2001",
			"date": "02-31-2025",
			"customerId": "CUST200",
			"amount": 10000,
			"taxRate": 0.18,
			"reportedTax": 1800,
			"transactionType": "SALE"
  }
]
```
 Returns **409 Conflict**

**Response:**
```
Transaction already exists: TXN2001
```

---

### ii.Invalid Date Format

#### Example Request
```json
[
  {
    "transactionId": "TXN2001",
    "date": "02-31-2025",
    "customerId": "CUST200",
    "amount": 10000,
    "taxRate": 0.18,
    "reportedTax": 1800,
    "transactionType": "SALE"
  }
]
```

 Returns **400 Bad Request**

**Response**
```json
{
  "error": "Invalid request",
  "message": "Invalid date format. Use YYYY-MM-DD"
}
```

---

### iii.Empty Transaction List

#### Example Request
```json
[]
```

 Returns **400 Bad Request**

**Message**
```
Transaction list cannot be empty
```

---

# 3.Sample Transaction Upload JSON

```json
[
  
    {
    "transactionId": "TXN1001",
    "date": "2025-02-01",
    "customerId": "CUST101",
    "amount": 10000,
    "taxRate": 0.18,
    "reportedTax": 1800,
    "transactionType": "SALE"
  },
  {
    "transactionId": "TXN1002",
    "date": "2025-02-02",
    "customerId": "CUST101",
    "amount": 200000,
    "taxRate": 0.18,
    "reportedTax": 36000,
    "transactionType": "SALE"
     },
     {
    "transactionId": "TXN1003",
    "date": "2025-02-03",
    "customerId": "CUST102",
    "amount": 5000,
    "taxRate": 0.18,
    "reportedTax": 500,
    "transactionType": "SALE"
    },
     {
    "transactionId": "TXN1004",
    "date": "2025-02-04",
    "customerId": "CUST101",
    "amount": 15000,
    "taxRate": 0.18,
    "reportedTax": 2700,
    "transactionType": "REFUND"
    },
     {
    "transactionId": "TXN1005",
    "date": "2025-02-03",
    "customerId": "CUST101",
    "amount": 250000,
    "taxRate": 0.18,
    "reportedTax": 800,
    "transactionType": "REFUND"
     },
    {
    "transactionId": "TXN1006",
    "date": "2025-02-05",
    "customerId": "CUST103",
    "amount": -1000,
    "taxRate": 0.18,
    "reportedTax": 180,
    "transactionType": "EXPENSE"
     },
     {
    "transactionId": "TXN1007",
    "date": "2025-02-06",
    "customerId": "CUST104",
    "amount": 120000,
    "taxRate": 0.05,
    "reportedTax": 6000,
    "transactionType": "SALE"
  },
     {
    "transactionId": "TXN-COMP-001",
    "date": "2025-02-10",
    "customerId": "CUST105",
    "amount": 10000,
    "taxRate": 0.18,
    "reportedTax": 1800,
    "transactionType": "SALE"
  },
  {
    "transactionId": "TXN-UNDER-001",
    "date": "2025-02-10",
    "customerId": "CUST106",
    "amount": 10000,
    "taxRate": 0.18,
    "reportedTax": 1500,
    "transactionType": "SALE"
  },
  {
    "transactionId": "TXN-OVER-001",
    "date": "2025-02-10",
    "customerId": "CUST107",
    "amount": 10000,
    "taxRate": 0.18,
    "reportedTax": 2200,
    "transactionType": "SALE"
  },
  {
    "transactionId": "TXN-NON-001",
    "date": "2025-02-10",
    "customerId": "CUST108",
    "amount": 10000,
    "taxRate": 0.18,
    "reportedTax": null,
    "transactionType": "SALE"
  },
  {
    "transactionId": "TXN-BOUNDARY-COMP-001",
    "date": "2025-02-10",
    "customerId": "CUST108",
    "amount": 10000,
    "taxRate": 0.18,
    "reportedTax": 1799,
    "transactionType": "SALE"
  },
  {
    "transactionId": "TXN-BOUNDARY-COMP-002",
    "date": "2025-02-10",
    "customerId": "CUST109",
    "amount": 10000,
    "taxRate": 0.18,
    "reportedTax": 1801,
    "transactionType": "SALE"
  }
]
```
For more API execution screenshots, see the /docs/postman-api-calls-screenshots folder.

---

# 4.Architecture Overview

The application follows a **layered architecture** to ensure scalability, maintainability, and clean separation of concerns.

### Layers:

 **Controller Layer**
- Exposes REST endpoints  
- Handles HTTP requests/responses  

 **Service Layer**
- Implements business logic  
- Coordinates validation, computation, and rule execution  

 **Repository Layer**
- Uses Spring Data JPA for database operations  

 **Rule Engine**
- Executes configurable tax rules stored in the database  

 **Validation Engine**
- Ensures transaction data integrity  

 **Audit Logging**
- Tracks ingestion, rule execution, and tax computation events  

---

# 5.Key Design Highlights

 - Database-driven rule engine  
 - Batch transaction processing  
 - Failure logging with reasons  
 - Exception management  
 - Audit trail for traceability
 - Provides summary reports    

---

# 6.Test Coverage

Project includes:

 1.Unit tests for:
- Service layer  
- Rule engine
- Controller layer

 2.Integration Test for Testing Entire flow   

**Coverage Report from IDE:** 

```bash
mvn clean test
```

OR

```bash
mvn test
```

<img width="1918" height="1027" alt="test coverage" src="https://github.com/user-attachments/assets/435945bb-8965-440f-9f2b-9fe9b5b06016" />


---
