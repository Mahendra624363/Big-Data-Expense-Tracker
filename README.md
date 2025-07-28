# 💰 Expense Tracker using Java + Hive (Cloudera Hadoop)

A console-based expense tracker application built using **Java** and **Apache Hive** on **Cloudera**. This project allows users to register, login, and manage their expenses while allowing admin users to monitor all users and expenses efficiently using Hive queries and partitions.

---

## 📌 Features

### 👤 User Features
- User Registration & Login
- Add expenses with:
  - Amount
  - Category
  - Date
- View all expenses
- View expense summaries:
  - By **Day** (Last 30 days)
  - By **Month**
  - By **Year**
- Change password

### 🔑 Admin Features
- Admin Login
- View all users
- View all expenses
- View specific user expenses
- Delete users
- Change user passwords

---

## 🛠️ Technologies Used

- **Java (JDK 8)**  
- **Apache Hive** (on Cloudera)
- **Hive JDBC (hive-jdbc-uber.jar)**
- **Hive Partitioning** (Year, Month, Day)

---

## 🗃️ Hive Tables Structure

### 📄 `users` Table
```sql
CREATE TABLE users (
  username STRING,
  password STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';
 ```

📄 expenses Table with Partitioning
```sql

CREATE TABLE expenses (
  username STRING,
  amount DOUBLE,
  category STRING
)
PARTITIONED BY (
  year INT,
  month INT,
  day INT
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';
```


🚀 How to Run
✅ Prerequisites

Cloudera or any Hive-enabled Hadoop environment

Hive JDBC JAR: hive-jdbc-uber.jar

⚙️ Compile the Project
javac -cp ".:hive-jdbc-uber.jar" *.java

▶️ Run the Project
java -cp ".:hive-jdbc-uber.jar" Main

