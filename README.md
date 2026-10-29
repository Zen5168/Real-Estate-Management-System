\# Real Estate Management System



A Java Swing desktop app for managing properties, clients, and transactions, with market analysis and sales reports. Data is stored in MySQL using JDBC.



\## Requirements



\- \*\*JDK 17 or higher\*\*

\- \*\*Maven\*\* (NetBeans, IntelliJ, and Eclipse include it)

\- \*\*MySQL\*\* running on `localhost:3306` (for example through WAMP, XAMPP, or MySQL Server)



The MySQL connector downloads automatically through Maven (it is listed in `pom.xml`), so there is nothing to install by hand.



\## Setup



1\. Start MySQL (for example, start WAMP or XAMPP so the icon turns green).

2\. Open `src/main/java/com/mycompany/realestatemanagementsystem/DBConnection.java` and check these two values:



&#x20;  ```java

&#x20;  private static final String DB\_USER = "root";

&#x20;  private static final String DB\_PASS = "";

&#x20;  ```



&#x20;  \*\*Change `DB\_USER` and `DB\_PASS` to match your own MySQL / phpMyAdmin login.\*\* phpMyAdmin uses the same username and password as MySQL, so whatever you type to log in to phpMyAdmin is what goes here. The defaults (`root` with an empty password) work for a fresh WAMP or XAMPP install.



3\. If your MySQL does not use port 3306, change the port in `DB\_SERVER\_URL` in the same file.



\## Running the app



1\. Open the project in your IDE and let Maven download the dependencies.

2\. Run the main class `RealEstateMain`.



The database `real\_estate`, all tables, and the admin account are created automatically the first time the app connects, so you do not need to import any SQL file.



\## Login



| Username | Password |

|----------|----------|

| admin    | admin    |



The admin account is the only data that exists at the start. Properties, clients, and transactions are all added through the app.



\## Features



\- Add, update, delete, and search properties (binary search by price or location)

\- Add, update, delete, and search clients (hash map for fast lookup)

\- Record and cancel sales, with the property status updating automatically

\- Market analysis and sales reports, sorted with insertion sort



\## Troubleshooting



| Problem | Fix |

|---------|-----|

| `Access denied for user 'root'` | `DB\_USER` or `DB\_PASS` in `DBConnection.java` does not match your MySQL login |

| `Communications link failure` | MySQL is not running, or it uses a different port than 3306 |

| `No suitable driver` | Maven has not downloaded the dependencies yet; reload the Maven project |

| `release version 17 not supported` | Your JDK is older than 17; install JDK 17 or higher |

