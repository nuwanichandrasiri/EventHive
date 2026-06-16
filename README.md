# 🎯 EventHive — University Event Registration System

A console-based university event registration system built with Java, featuring admin and student panels with full file persistence.

## ✨ Features

### Admin Panel
- 🔐 Password-protected access
- 📅 Create events with name, description, date, type, location and capacity
- 👥 View all events and attendees
- 🔍 Search events by name or date
- 🔍 Search students by name or ID
- 📊 Registration reports
- ✅ Mark events as Completed
- 🚫 Duplicate event prevention

### Student Panel
- 🎓 Register with campus student ID (validated)
- 📋 View and search available events
- 📝 Register for events
- ❌ Cancel registration with confirmation
- 👁 View personal registrations

### System Features
- 💾 Data persists via file handling (events.txt, students.txt, registrations.txt)
- 🔡 Case-insensitive ID input
- ✅ Input validation (name, email, student ID)
- 🔴 Auto marks events as Full when capacity is reached
- 🔢 Real campus student ID support

## 🛠 Built With
- Java (OOP — Classes, ArrayLists, File I/O)

## 📁 Project Structure
EventHive/

├── Main.java            # Menu and user interaction

├── EventManager.java    # Core logic, file handling

├── Event.java           # Event class

├── Student.java         # Student class

├── Registration.java    # Registration class

├── events.txt           # Persisted event data

├── students.txt         # Persisted student data

└── registrations.txt    # Persisted registration data

## 🚀 Getting Started
1. Clone the repo
git clone https://github.com/nuwanichandrasiri/EventHive.git
2. Compile
javac Main.java
3. Run
java Main

## 👩‍💻 Author
**Nuwani Chandrasiri**
BSc (Hons) Software Engineering — KIU Sri Lanka
[LinkedIn](https://linkedin.com/in/nuwani-chandrasiri-dev) · [GitHub](https://github.com/nuwanichandrasiri) · [Portfolio](https://nuwanichandrasiri.github.io/portfolio)
