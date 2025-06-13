# <div align="center">Parkner Parking Management System</div>

<div align="center">
  <img src="src/asset/icons/parkner-logo.png" alt="Parkner Logo" width="220"/>
</div>

## About

**Parkner** is a digital parking management system for attendants to efficiently manage vehicle entries, exits, and parking operations. It replaces manual methods with real-time slot monitoring, digital record-keeping, and automated fee computation, improving accuracy, security, and efficiency for both staff and customers.

## Purpose

This project was developed for **[COMP 009] OBJECT-ORIENTED PROGRAMMING** under Prof. Renz Angelo De Vera Cadaoas.

**Goals:**

- Demonstrate Java OOP, GUI, and database integration.
- Provide a practical parking lot management solution.
- Serve as a platform for teamwork and software engineering best practices.
- User-friendly and adaptable for academic or small business use.

## Features

- **Parking Slot Management**

  - Manage total slots, maintenance/reserved slots, and occupied slots via a summary table.
  - Easily adjust total and maintenance slots from the dashboard.
  - Occupied slots are updated automatically as cars enter/exit.

- **Car Parking and Payment**

  - Add cars to the parking lot with car brand and license plate.
  - Each car is assigned a unique reference number.
  - Pay for parking using either Cash or Digital (QR code) payment.
  - Receipts display payment details, payment method, and a QR code for digital payments.

- **Service History**

  - View all past transactions, including car details, payment method, amount paid, date, time in/out, and reference ID.
  - Search and filter service history records.
  - Total revenue is displayed and updated in real time.

- **Receipts**

  - Receipts show car details, time in/out, total time, VAT, total amount, payment method, and QR code (for digital payments).
  - Print receipts directly from the application.

- **Digital Record-Keeping**

  - All transactions and customer info are stored in a MySQL database.

- **User-Friendly Interface**

  - Intuitive Java Swing UI for attendants.

- **Real-Time Slot Monitoring**

  - View total and available parking spaces as cars come and go.

- **Automated Fee Computation**

  - System computes parking fees and change based on duration.

- **Printable Receipts**
  - Customers receive digital receipts with QR code for transparency.

## Scope and Limitations

**Scope:**

- Real-time slot monitoring.
- Vehicle logging (plate, brand, time in/out).
- Automated fee computation.
- Digital ticketing/receipt.
- Database storage.
- Desktop use (Java Swing).

**Limitations:**

- No hardware integration (manual updates only).
- Attendant-operated only (no customer self-service).
- No mobile payment integration (QR is simulated).
- Manual data entry by attendants.
- No customer dispute handling.

## Programming Languages & Technologies Used

<div>
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Swing-007396?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/NetBeans_IDE-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white"/>
</div>

- **Java** (JDK 15+)
- **Java Swing** (GUI)
- **MySQL** (database)
- **JDBC** (MySQL Connector/J)
- **NetBeans IDE** (recommended)
- **ZXING** (QR code generation)
- **Font:** Inter (optional, see below)

## Color Palette Reference

| Color Name             | Hex Code | Usage                                          |
| ---------------------- | -------- | ---------------------------------------------- |
| Primary Yellow         | #E6B400  | Highlights, table headers, sidebar buttons     |
| Accent Gold            | #D4A300  | Borders, revenue panel, accent elements        |
| Background White       | #FAFAFA  | Main background, panels                        |
| Text Black             | #141414  | Main text, labels                              |
| Text Dark Gray         | #333333  | Secondary text, subtle labels                  |
| Panel Light Gray       | #F2F2F2  | Panel backgrounds, alternate table rows        |
| Success Green          | #43A047  | Success messages, available slots indicator    |
| Warning Orange         | #FF9100  | Warnings, attention indicators                 |
| Error Red              | #E57373  | Error messages, delete actions, revenue border |
| Border Gray            | #BDBDBD  | Panel and table borders                        |
| Table Selection Yellow | #FFECA3  | Table row selection highlight                  |
| Table Row Alt Yellow   | #FCF3CF  | Alternate table row background                 |

## Installation

1. **Requirements:**

   - NetBeans IDE 12.1 or later
   - JDK 15 or later
   - MySQL Server
   - [mysql-connector-java-8.0.23](https://dev.mysql.com/downloads/connector/j/)
   - [ZXING Library](https://repo1.maven.org/maven2/com/google/zxing/core/3.5.3/) (core-3.5.3.jar)
   - Font: Inter (optional, see below)

2. **Database Setup:**

   - Import the provided `parkner_db.sql` file to set up tables and initial data.
   - This file includes initial records for parking slots, parking_store, and sample reports.

3. **Project Setup:**

   - Clone or download this repository.
   - Open the project in NetBeans.
   - **Add all JAR files from the `libraries` folder** to your project's libraries (including MySQL Connector/J and ZXING).
   - Build and run the project.

4. **Fonts and Icons:**
   - (Optional) Install the Inter font from `src/assets/font` for best appearance.
   - Ensure all resources in `src/asset/icons` and `src/assets/font` are present.

## Usage

- **Dashboard:**  
  View all currently parked cars, add new cars, and process payments.

- **Parking Slot Management:**  
  Manage total and maintenance slots in the Manage panel. Occupied slots update automatically.

- **Service History:**  
  View all past transactions, including payment method, and monitor total revenue.

- **Receipts:**  
  Print receipts for each completed transaction (with QR code for digital payments).

## Contributors

- Bautista, Danielle Eizy
- Evardo, Seth Oseas M.
- Francisco, Cian Jake F.
- Indaya, Makaila Nelrei C.
- Latoy, Raphael Andrei G.
- Torres, Juan Miguel G.
- Urbano, Juan Miguel T.

## Logo

The Parkner logo is located at `src/asset/icons/parkner-logo.png`.

---

**Note:**  
If you encounter issues with fonts or icons, ensure all resources in the `src/asset/icons` and `src/assets/font` directories are present and properly referenced.

---
