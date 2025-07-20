import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseName;
    private String courseCode;
    private String lecturerName;
    private String lecturerId;

    public Course(String courseName, String courseCode) {
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    public Course(String courseName, String courseCode, String lecturerName, String lecturerId) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.lecturerName = lecturerName;
        this.lecturerId = lecturerId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void addStudentToCourse(String studentName, String trimester) {
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

            for (int i = 0; i < courseLines.size(); i++) {
                String line = courseLines.get(i);
                if (line.startsWith(courseName + ",")) {
                    courseFound = true;
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String students = parts[4];
                        String[] studentNames = students.split(",");
                        StringBuilder updatedStudents = new StringBuilder();
                        for (String student : studentNames) {
                            if (!student.isEmpty()) {
                                updatedStudents.append(student).append(";");
                            }
                        }
                        updatedStudents.append(studentName).append(";").append(trimester);
                        courseLines.set(i, courseName + "," + courseCode + "," + lecturerName + "," + lecturerId + "," + updatedStudents);
                        Files.write(Paths.get(courseFileName), courseLines, StandardOpenOption.TRUNCATE_EXISTING);
                        System.out.println("Student " + studentName + " registered for the course " + courseName);
                        break;
                    }
                }
            }

            if (!courseFound) {
                System.out.println("Course '" + courseName + "' not found or has no students enrolled.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while registering for the course: " + e.getMessage());
        }
    }
}
