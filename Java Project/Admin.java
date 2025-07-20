import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

class Admin {
    private String id;
    private String name;
    private ArrayList<Student> students;
    private ArrayList<Lecturer> lecturers;

    public Admin(String id, String name) {
        this.id = id;
        this.name = name;
        this.students = new ArrayList<>();
        this.lecturers = new ArrayList<>();
    }

    public boolean login(String enteredId, String enteredName) {
        return this.id.equals(enteredId) && this.name.equals(enteredName);
    }

    public void adminFeatures(Scanner scanner) {
        System.out.println("Choose option");
        System.out.println("1. Add Student");
        System.out.println("2. Add Lecturer");
        System.out.println("3. Add Course and assign to lecturer");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                addStudent(scanner);
                break;
            case 2:
                addLecturer(scanner);
                break;
            case 3:
                addCourseAndAssignToLecturer(scanner);
                break;
            case 4:
                System.out.println("Exiting...");
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void addStudent(Scanner scanner) {
        System.out.print("Enter ID for new student: ");
        String newId = scanner.nextLine();
        System.out.print("Enter name for new student: ");
        String newName = scanner.nextLine();

        // Input validation
        if (newId.isEmpty() || newName.isEmpty()) {
            System.out.println("ID and name cannot be empty. Please try again.");
            return;
        }

        Student newStudent = new Student(newId, newName);
        students.add(newStudent);
        saveToFile(newStudent, "student.csv");
        System.out.println("New student created with ID: " + newId);
    }

    private void addLecturer(Scanner scanner) {
        System.out.print("Enter ID for new lecturer: ");
        String newId = scanner.nextLine();
        System.out.print("Enter name for new lecturer: ");
        String newName = scanner.nextLine();

        // Input validation
        if (newId.isEmpty() || newName.isEmpty()) {
            System.out.println("ID and name cannot be empty. Please try again.");
            return;
        }

        Lecturer newLecturer = new Lecturer(newId, newName);
        lecturers.add(newLecturer);
        saveToFile(newLecturer, "lecturer.csv");
        System.out.println("New lecturer created with ID: " + newId);
    }

    private void addCourseAndAssignToLecturer(Scanner scanner) {
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();
        System.out.print("Enter lecturer name: ");
        String lecturerName = scanner.nextLine();
        System.out.print("Enter lecturer ID: ");
        String lecturerId = scanner.nextLine();

        // Input validation
        if (courseName.isEmpty() || courseCode.isEmpty() || lecturerName.isEmpty() || lecturerId.isEmpty()) {
            System.out.println("All fields are required. Please try again.");
            return;
        }

        Course newCourse = new Course(courseName, courseCode, lecturerName, lecturerId);
        saveToFile(newCourse, "course.csv");
        System.out.println("New course created and assigned to lecturer: " + newCourse.getCourseName() + " to " + newCourse.getLecturerName());
    }

    private void saveToFile(Object item, String fileName) {
        try {
            StringBuilder sb = new StringBuilder();
            if (item instanceof Student) {
                Student student = (Student) item;
                String studentRecord = student.getId() + "," + student.getName() + "\n";
                Files.write(Paths.get(fileName), studentRecord.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else if (item instanceof Lecturer) {
                Lecturer lecturer = (Lecturer) item;
                String lecturerRecord = lecturer.getId() + "," + lecturer.getName() + "\n";
                Files.write(Paths.get(fileName), lecturerRecord.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else if (item instanceof Course) {
                Course course = (Course) item;
                String courseRecord = course.getCourseName() + "," + course.getCourseCode();
                if (course.getLecturerName() != null && !course.getLecturerName().isEmpty()
                        && course.getLecturerId() != null && !course.getLecturerId().isEmpty()) {
                    courseRecord += "," + course.getLecturerName() + "," + course.getLecturerId();
                }
                courseRecord += "\n";
                Files.write(Paths.get(fileName), courseRecord.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                System.out.println("Invalid item type. Cannot write to file.");
                return; //Return if the item type is not recognized
            }
        } catch (IOException ex) {
            System.out.println("Error writing to file: " + ex.getMessage());
        }
    }
}