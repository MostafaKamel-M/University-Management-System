import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

class Student {
    private String id;
    private String name;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean login(String enteredId, String enteredName) {
        return this.id.equals(enteredId) && this.name.equals(enteredName);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void studentFeatures(Scanner scanner) {
        System.out.println("Choose trimester");
        System.out.println("1. Trimester 1");
        System.out.println("2. Trimester 2");
        System.out.println("3. Trimester 3");
        System.out.print("Enter your choice: ");
        int trimesterChoice = scanner.nextInt();
        scanner.nextLine(); 

        String trimester = ""; 
        switch (trimesterChoice) {
            case 1:
                trimester = "Trimester 1";
                break;
            case 2:
                trimester = "Trimester 2";
                break;
            case 3:
                trimester = "Trimester 3";
                break;
            default:
                System.out.println("Invalid trimester choice.");
                return;
        }

        System.out.print("Enter the course name you want to register for: ");
        String courseName = scanner.nextLine();

        addStudentToCourse(courseName, id, trimester);
    }

    private void addStudentToCourse(String courseName, String studentId, String trimester) {
        String courseFileName = "course.csv";

        try {
            List<String> courseLines = Files.readAllLines(Paths.get(courseFileName));
            boolean courseExists = false;

            for (int i = 0; i < courseLines.size(); i++) {
                String line = courseLines.get(i);
                if (line.startsWith(courseName + ",")) {
                    courseExists = true;
                    courseLines.set(i, line + ";" + studentId + ";" + trimester);
                    break;
                }
            }

            if (courseExists) {
                Files.write(Paths.get(courseFileName), courseLines, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("Student " + studentId + " registered for the course " + courseName);
            } else {
                System.out.println("Course '" + courseName + "' does not exist.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while registering for the course: " + e.getMessage());
        }
    }
}
