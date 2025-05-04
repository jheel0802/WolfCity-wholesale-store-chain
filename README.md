# WolfWR: Wholesale Warehouse Management System

WolfWR is a console-based database management application developed for the course *Database Management Concepts and Systems*. It simulates the backend operations of a membership-only warehouse club chain (similar to Costco or Sam’s Club) and provides interfaces for staff to manage stores, inventory, customers, transactions, and reports.

## Features

### 📋 Information Processing
- Add, update, and delete records for:
  - Stores
  - Staff (e.g., managers, cashiers)
  - Customers (club members)
  - Suppliers
  - Discounts

### 📦 Inventory Management
- Record new inventory from suppliers
- Process item returns and restocking
- Transfer products between stores

### 💰 Billing & Transactions
- Record and generate supplier bills
- Perform customer transactions with discount application
- Compute and distribute cashback rewards for Platinum members

### 📈 Reporting Tools
- Sales summary reports (daily, monthly, yearly)
- Sales growth analysis for specific stores
- Merchandise stock levels
- Customer registration trends
- Customer activity summaries

## How It Works

1. **Main Menu** offers access to four major operational areas.
2. **User inputs** determine control flow via a command-line interface.
3. **MariaDB backend** is used for persistent data storage and retrieval.
4. **Input validation** and control flow management are handled interactively.

## Technologies Used

- Java (JDBC)
- MariaDB
- SQL
- CLI-based interaction

## Assumptions

- Membership becomes inactive upon expiration or cancellation.
- Each store can have one manager (auto-updated when job title is set).
- Product discounts are applied only if valid during transaction date.
- Reward percentage for platinum customers is fixed at 2% annually.

## Getting Started

1. Ensure MariaDB is running and configured with correct schemas and tables.
2. Update `App.java` with your MariaDB `username` and `password`.
3. Compile and run:
   ```bash
   javac App.java
   java App
