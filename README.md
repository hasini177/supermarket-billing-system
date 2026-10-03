🛒 Supermarket Billing System

A desktop billing application built with **Java (Swing)** and **MySQL**. Enter product IDs, manage a cart, calculate totals with 5% tax, and generate an invoice. Stock in the database updates automatically after checkout.

---

## ✨ Features

- **Swing GUI:** User-friendly desktop interface for billing
- **Cart management:** Add items by product ID and quantity; remove an item or reduce its quantity
- **Stock validation:** Blocks adding more than what is available in inventory
- **Automated billing:** Calculates subtotal, 5% tax, and final total
- **Database storage:** Saves products, bills, and purchased items in MySQL
- **Inventory sync:** Deducts purchased quantities from stock on checkout

---

## 🛠️ Tech Stack

| Component    | Technology                    |
|--------------|-------------------------------|
| Language     | Java (Core Java, Swing, OOP)  |
| Database     | MySQL                         |
| Connectivity | JDBC (MySQL Connector/J)      |
| IDE          | VS Code                       |

---

## 📦 Prerequisites

- Java Development Kit (JDK) installed
- MySQL Server installed and running
- MySQL Connector/J (`mysql-connector-j-26.7.0.jar`) placed in the `lib/` folder
- VS Code with the Java extensions

---

## 🗄️ Database 


| Table        | Purpose                                    |
| ------------ | ------------------------------------------ |
| `inventory`  | Stores product details and available stock |
| `bills`      | Stores completed bill information          |
| `bill_items` | Stores products included in each bill      |


---

## 📁 Project Structure
Supermarket-Billing-System/
│
├── src/
│   ├── BillingSystemGUI.java
│   ├── Product.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Bill.java
│   ├── DBConnection.java
│   ├── ProductDAO.java
│   └── BillingDAO.java
│
├── lib/
│   └── mysql-connector-j-26.7.0.jar
│
├── SQL/
│   ├── create_database.sql
│   ├── create_table.sql
│   └── insert_data.sql
│
└── .vscode/
    └── settings.json

---

## ▶️ How to Run

1. **Clone** the repository:
```bash
   git clone <your-repo-url>
   cd <project-folder>
```
2. Set up the database using the SQL scripts in the SQL folder.
3. **Set your database credentials** in `DBConnection.java`:
```java
   private static final String URL = "jdbc:mysql://localhost:3306/billing_system";
   private static final String USER = "your_username";
   private static final String PASSWORD = "your_password";
```
4. **Run the app:** open `BillingSystemGUI.java` in VS Code and click **Run** above the `main` method.

---

## 🧾 How to Use

1. **Add items:** Enter a Product ID (e.g., `101`) and quantity, then click **Add to Cart**.
2. **View cart:** Items appear in the table at the center of the window.
3. **Remove items:** Select a row and click **Remove Selected**. Choose to remove the whole item or just reduce the quantity.
4. **Checkout:** Click **Checkout** to:
   - Display a formatted receipt
   - Save the bill to the database
   - Deduct purchased quantities from inventory

