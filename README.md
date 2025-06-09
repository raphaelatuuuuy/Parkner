# <div align="center">Parkner Parking Management System</div>

<div align="center">
  <img src="src/asset/icons/parkner-logo.png" alt="Parkner Logo" width="500" height="500"/>
</div>

## About

**Parkner** is a digital parking management system designed to assist parking attendants in efficiently managing vehicle entries, exits, and parking operations. Traditional manual methods often lead to inefficiencies such as miscalculated tickets, lost records, and human error—resulting in customer dissatisfaction and operational challenges. Parkner addresses these issues by providing a user-friendly application that enables real-time slot monitoring, digital record-keeping, and automated fee computation. The system aims to enhance accuracy, security, and efficiency in parking operations, reducing manual workload and minimizing errors for both staff and customers.

## Purpose

This project was developed as a requirement for **[COMP 009] OBJECT-ORIENTED PROGRAMMING** under Prof. Renz Angelo De Vera Cadaoas.

**Additional purposes and goals:**

- Provide hands-on experience in designing and implementing a real-world object-oriented application.
- Demonstrate Java OOP concepts, GUI development, and database integration.
- Offer a practical solution for parking lot management, improving efficiency and reducing manual errors.
- Serve as a learning platform for teamwork, version control, and software engineering best practices.
- Create a user-friendly system that can be easily adapted for academic or small business use.

## Features

- **Real-Time Slot Monitoring:**  
  View the total number of parking spaces and monitor their availability as cars come and go.

- **Vehicle Entry and Exit Logging:**  
  Digitally log vehicle details such as parking slot, car brand, plate number, time-in, and running time.

- **Digital Record-Keeping:**  
  All parking transactions and customer information are securely stored in a database for easy retrieval and improved data management.

- **Automated Fee Calculation:**  
  The system automatically calculates parking fees based on recorded entry and exit times, minimizing miscalculations.

- **Receipt Generation:**  
  Customers receive digital receipts before exiting, ensuring reliable records and transparency.

- **User-Friendly Interface:**  
  Intuitive design tailored for attendants, ensuring easy navigation and reducing the learning curve.

- **CRUD for Parking Slots:**  
  Add, view, update, and delete parking slots (only if not occupied).

- **Service History:**  
  View all past transactions, including car details, amount paid, time in/out, and reference ID. Displays total revenue earned.

## Scope and Limitations

**Scope:**

- Real-time slot monitoring for available and occupied slots.
- Vehicle logging (plate number, car brand, time in/out).
- Automated fee computation based on duration.
- Digital ticketing/receipt for clear transaction records.
- Database storage for vehicle and transaction records.
- User-friendly interface for staff.
- Designed for desktop use (Java Swing).

**Limitations:**

- **Hardware Compatibility:** Real-time monitoring depends on manual updates or integration with external sensors; without hardware, slot data may be less reliable.
- **Attendant-Operated Only:** No direct customer or self-service functionality; designed for staff use.
- **No Advanced Features:** Mobile payment, reservations, and customer self-service are not implemented.
- **Manual Data Entry:** Relies on attendants to input vehicle information, which may introduce errors.
- **No Customer Dispute Handling:** Lacks integrated features for managing complaints, disputes, or refunds.

## Programming Languages & Technologies Used

<div>
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Swing-007396?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/NetBeans_IDE-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white"/>
</div>

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
