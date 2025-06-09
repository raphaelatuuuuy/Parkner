# Parkner Parking Management System

![Parkner Logo](src/asset/icons/parkner-logo.png)

## About

**Parkner** is a Java-based parking management system designed to streamline the process of managing parking slots, vehicle entries, payments, and service history for parking lots. Built with a modern UI using Java Swing, it provides an intuitive dashboard for parking attendants to handle daily parking operations efficiently.

## Purpose

This project was developed as a requirement for **[COMP 009] OBJECT-ORIENTED PROGRAMMING** under Prof. Renz Angelo De Vera Cadaoas.  
Additional purposes and goals include:

- To provide hands-on experience in designing and implementing a real-world object-oriented application.
- To demonstrate the application of Java OOP concepts, GUI development, and database integration.
- To offer a practical solution for parking lot management, improving efficiency and reducing manual errors.
- To serve as a learning platform for teamwork, version control, and software engineering best practices.
- To create a user-friendly system that can be easily adapted for academic or small business use.

## Features

- **CRUD for Parking Slots:**

  - **Create:** Add new parking slots to the system.
  - **Read:** View all parking slots and their statuses (Available/Unavailable).
  - **Update:** Change the status of parking slots (e.g., mark as available/unavailable).
  - **Delete:** Remove parking slots (only if not occupied).

- **Vehicle Management:**

  - Park a car by entering car brand and license plate.
  - Assigns the lowest-numbered available slot automatically.
  - Tracks time-in for each parked vehicle.

- **Payment & Checkout:**

  - Calculates parking fee based on duration (50 pesos/hour).
  - Generates a unique reference number for each transaction.
  - Accepts payment, computes change, and generates a printable receipt.
  - Service history is updated after each payment.

- **Service History:**

  - View all past transactions, including car details, amount paid, time in/out, and reference ID.
  - Displays total revenue earned.

- **Modern UI:**
  - Custom color palette and fonts for a professional look.
  - Sidebar navigation with icons.
  - Responsive layout for different screen sizes.

## Programming Languages & Technologies Used

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-007396?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![NetBeans IDE](https://img.shields.io/badge/NetBeans_IDE-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)

- **Java** (JDK 15+)
- **Java Swing** (for GUI)
- **MySQL** (database)
- **JDBC** (MySQL Connector/J)
- **NetBeans IDE** (recommended for development)
- **Font:** Inter (install manually from `src/assets/font` if desired)

## Color Palette Reference

The system uses a carefully selected color palette for clarity, accessibility, and a modern, professional look:

| Color Name             | Hex Code | Usage                                           |
| ---------------------- | -------- | ----------------------------------------------- |
| Primary Yellow         | #E6B400  | Main highlights, table headers, sidebar buttons |
| Accent Gold            | #D4A300  | Borders, revenue panel, accent elements         |
| Background White       | #FAFAFA  | Main background, panels                         |
| Text Black             | #141414  | Main text, labels                               |
| Text Dark Gray         | #333333  | Secondary text, subtle labels                   |
| Panel Light Gray       | #F2F2F2  | Panel backgrounds, alternate table rows         |
| Success Green          | #43A047  | Success messages, available slots indicator     |
| Warning Orange         | #FF9100  | Warnings, attention indicators                  |
| Error Red              | #E57373  | Error messages, delete actions, revenue border  |
| Border Gray            | #BDBDBD  | Panel and table borders                         |
| Table Selection Yellow | #FFECA3  | Table row selection highlight                   |
| Table Row Alt Yellow   | #FCF3CF  | Alternate table row background                  |

**Why these colors?**

- **Yellow/Gold/Orange:** These colors are mainly used in car parking signage and road markings, making them instantly recognizable and intuitive for users. They are used for highlights, action buttons, table headers, and warnings to create a warm, inviting, and energetic feel, and to make key actions and totals stand out.
- **Green:** Indicates success and availability, providing clear positive feedback.
- **Red:** Used for errors and destructive actions (like delete), drawing attention to important warnings.
- **Gray/White:** Provides a clean, modern, and readable background, reducing eye strain and focusing attention on content.
- **Consistent palette:** Ensures a professional, cohesive look and improves usability for all users.

## Installation

1. **Requirements:**

   - NetBeans IDE 12.1 or later
   - JDK 15 or later
   - MySQL Server
   - [mysql-connector-java-8.0.23](https://dev.mysql.com/downloads/connector/j/)
   - Font: Inter (install manually from `src/assets/font` if desired)

2. **Database Setup:**

   - Import the provided `parking-db.sql` file to set up tables and initial data.

3. **Project Setup:**
   - Clone or download this repository.
   - Open the project in NetBeans.
   - Add the MySQL connector JAR to your project's libraries.
   - Build and run the project.

## Usage

- **Dashboard:**  
  View all currently parked cars, add new cars, and process payments.

- **Parking Slot Management:**  
  Add, edit, or delete parking slots as needed.

- **Service History:**  
  Review all past transactions and monitor total revenue.

- **Receipts:**  
  Print receipts for each completed transaction.

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
