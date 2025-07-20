import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Admin admin = new Admin("1", "admin");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Login:");
        System.out.print("Enter id: ");
        String id = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        if (id.equals("1")) {
            if (admin.login(id, name)) {
                System.out.println("Admin logged in");
                admin.adminFeatures(scanner);
            } else {
                System.out.println("Invalid admin credentials");
            }
        } else {
            String userType = checkUserType(id, name);
            if (userType != null) {
                System.out.println(userType + " logged in");
                if (userType.equals("Student")) {
                    Student student = new Student(id, name);
                    student.studentFeatures(scanner);
                } else if (userType.equals("Lecturer")) {
                    Lecturer lecturer = new Lecturer(id, name);
                    lecturer.lectureFeatures(scanner);
                }
            } else {
                System.out.println("Invalid credentials");
            }
        }
    }

    public static String checkUserType(String enteredId, String enteredName) {
        if (checkCredentials(enteredId, enteredName, "student.csv")) {
            return "Student";
        } else if (checkCredentials(enteredId, enteredName, "lecturer.csv")) {
            return "Lecturer";
        }
        return null;
    }

    public static boolean checkCredentials(String enteredId, String enteredName, String userType) {
        List<String> userLines = readFromFile(userType);
        for (String line : userLines) {
            String[] parts = line.split(",");
            if (parts.length == 2 && parts[0].equals(enteredId) && parts[1].equals(enteredName)) {
                return true; // Credentials matched
            }
        }
        return false; // No match found
    }

    public static List<String> readFromFile(String fileName) {
        try {
            return Files.readAllLines(Paths.get(fileName));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return new ArrayList<>();
    }
}
