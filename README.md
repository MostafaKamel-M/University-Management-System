# OOPD Project - University Management System (Swing GUI)

This is a simple Java desktop application for managing students, lecturers, and courses at a university. The app uses a graphical user interface (GUI) built with **Swing** and stores data in CSV files.

## Features
- **Login system** for Admin, Student, and Lecturer
- **Admin Panel**: Add/view students, lecturers, and courses
- **Student Panel**: View available courses, register for courses, view registered courses
- **Lecturer Panel**: View courses they teach, view students in their courses

## How to Run
1. **Navigate to the `PART1 NEW 3` folder** in your terminal.
2. **Compile all Java files:**
   ```sh
   javac *.java
   ```
3. **Run the GUI application:**
   ```sh
   java MainGUI
   ```

## Login Credentials
- **Admin:**
  - ID: `1`
  - Name: `admin`
- **Student/Lecturer:**
  - Credentials are stored in `student.csv` and `lecturer.csv` (format: `id,name` per line)

## How to Use
### 1. Login
- Enter your ID and Name, then click **Login**.
- The app will open the appropriate panel based on your credentials.

### 2. Admin Panel
- **Tabs:**
  - **Students:** Add new students and view all students.
  - **Lecturers:** Add new lecturers and view all lecturers.
  - **Courses:** Add new courses and view all courses.
- **Data is saved in CSV files** (`student.csv`, `lecturer.csv`, `course.csv`).

### 3. Student Panel
- **Available Courses:** View all courses and register for selected courses.
- **My Courses:** View all courses you are registered for.
- **Registrations are saved in `registration.csv`** (format: `studentId,courseId`).

### 4. Lecturer Panel
- **My Courses:** View courses assigned to you (by name or ID match).
- **Students in My Courses:** View all students registered in your courses.

## Data Files
- `student.csv` — List of students (`id,name`)
- `lecturer.csv` — List of lecturers (`id,name`)
- `course.csv` — List of courses (`courseId,courseName`)
- `registration.csv` — Student course registrations (`studentId,courseId`)

## Notes
- The app uses **Swing** for the GUI (no JavaFX).
- All data is stored in CSV files in the same folder as the app.
- You can edit the CSV files manually to add or remove users/courses if needed.

---

**Enjoy using the University Management System!** 
