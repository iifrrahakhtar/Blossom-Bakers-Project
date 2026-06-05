# 🧁 Blossom Bakers - Home Bakery Management System

Welcome to the **Blossom Bakers Portal**! This is a desktop application built in Java using the **Swing** framework, featuring an elegant, custom-themed pastel user interface. It serves as a comprehensive management system designed for a home bakery business to track products, customers, orders, payments, and deliveries.

---

## ✨ Features

* **Custom Aesthetic Login Portal:** A beautifully styled login interface with rounded elements and a custom bakery background.
* **All-in-One Management Dashboard:** Seamless dynamic view switching utilizing `CardLayout`.
* **Full CRUD Functionality:** Ability to **Add**, **Update**, **Delete**, and **Refresh** live data records.
* **Automated Product Seed:** On first startup, the application populates the database with a delicious, default bakery menu (Cakes, Pastries, Cookies, and more).
* **Robust Database Integration:** Connected to a MySQL database tracking multiple operational tables:
  * Products
  * Customers
  * Orders
  * Order Details
  * Payments
  * Delivery

---

## 🛠️ Tech Stack & Requirements

* **Language:** Java 8 or higher
* **GUI Framework:** Java Swing & AWT
* **Database:** MySQL
* **Driver:** MySQL Connector/J (`com.mysql.cj.jdbc.Driver`)

---

## 🚀 How to Setup and Run

### 1. Database Configuration
Before running the application, make sure you have a MySQL server running.
1. Create a database named `homebakery`.
2. Update the credentials in `BakeryGUI.java` to match your local MySQL configuration:
```java
   private final String url = "jdbc:mysql://localhost:3306/homebakery";
   private final String user = "YOUR_DATABASE_USERNAME";
   private final String password = "YOUR_DATABASE_PASSWORD";
