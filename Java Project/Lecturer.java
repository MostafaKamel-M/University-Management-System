import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

class Lecturer {
    private String id;
    private String name;

    public Lecturer(String id, String name) {
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

    public void lectureFeatures(Scanner scanner) {
        System.out.println("View students in courses:");
        viewStudentsInCourse(scanner);
    }

    private void viewStudentsInCourse(Scanner scanner) {
        System.out.print("Enter course name to view students: ");
        String courseName = scanner.nextLine();

        String courseFileName = "course.csv";

        try {
            List<String> courseLines = Files.readAllLines(Paths.get(courseFileName));
            boolean courseFound = false;

            // Check if the course name exists in the first line of the file
            String firstLine = courseLines.get(0);
            if (!firstLine.contains(courseName)) {
                System.out.println("Course '" + courseName + "' not found in the course list.");
                return;
            }

            for (String line : courseLines) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(courseName)) {
                    courseFound = true;
                    String students = parts[4];
                    String[] studentNames = students.split(";");
                    if (studentNames.length == 1 && studentNames[0].isEmpty()) {
                        System.out.println("No students enrolled in course '" + courseName + "'.");
                    } else {
                        System.out.println("Students enrolled in course '" + courseName + "':");
                        for (String student : studentNames) {
                            System.out.println(student);
                        }
                    }
                    break;
                }
            }

            if (!courseFound) {
                System.out.println("Course '" + courseName + "' not found or has no students enrolled.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
