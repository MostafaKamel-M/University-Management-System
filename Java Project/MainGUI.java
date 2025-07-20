import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class MainGUI extends JFrame {
    private JTextField idField;
    private JTextField nameField;
    private JLabel messageLabel;
    private JPanel loginPanel;
    private JPanel adminPanel;
    private JTabbedPane adminTabs;
    private JPanel lecturerPanel;
    private JTabbedPane lecturerTabs;
    private String currentLecturerId;
    private String currentLecturerName;
    private JPanel studentPanel;
    private JTabbedPane studentTabs;
    private String currentStudentId;
    private String currentStudentName;

    public MainGUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

        loginPanel = createLoginPanel();
        add(loginPanel, "login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idLabel, gbc);

        idField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(idField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        nameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(nameField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(messageLabel, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        return panel;
    }

    private void showAdminPanel() {
        if (adminPanel == null) {
            adminPanel = new JPanel(new BorderLayout());
            adminTabs = new JTabbedPane();
            adminTabs.addTab("Students", createUserTab("student.csv", "Student"));
            adminTabs.addTab("Lecturers", createUserTab("lecturer.csv", "Lecturer"));
            adminTabs.addTab("Courses", createCourseTab());
            adminPanel.add(adminTabs, BorderLayout.CENTER);
        }
        setTitle("Admin Panel");
        getContentPane().removeAll();
        add(adminPanel);
        revalidate();
        repaint();
    }

    private JPanel createUserTab(String fileName, String userType) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel();
        JTextField idField = new JTextField(8);
        JTextField nameField = new JTextField(8);
        JButton addButton = new JButton("Add " + userType);
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(addButton);
        panel.add(inputPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name"};
        List<String[]> data = loadUserData(fileName);
        JTable table = new JTable(data.toArray(new String[0][0]), columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            if (!id.isEmpty() && !name.isEmpty()) {
                appendToFile(fileName, id + "," + name);
                ((javax.swing.table.DefaultTableModel) table.getModel()).addRow(new String[]{id, name});
                idField.setText("");
                nameField.setText("");
            }
        });
        return panel;
    }

    private JPanel createCourseTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel();
        JTextField idField = new JTextField(8);
        JTextField nameField = new JTextField(8);
        JButton addButton = new JButton("Add Course");
        inputPanel.add(new JLabel("Course ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Course Name:"));
        inputPanel.add(nameField);
        inputPanel.add(addButton);
        panel.add(inputPanel, BorderLayout.NORTH);

        String[] columns = {"Course ID", "Course Name"};
        List<String[]> data = loadUserData("course.csv");
        JTable table = new JTable(data.toArray(new String[0][0]), columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            if (!id.isEmpty() && !name.isEmpty()) {
                appendToFile("course.csv", id + "," + name);
                ((javax.swing.table.DefaultTableModel) table.getModel()).addRow(new String[]{id, name});
                idField.setText("");
                nameField.setText("");
            }
        });
        return panel;
    }

    private List<String[]> loadUserData(String fileName) {
        List<String[]> data = new ArrayList<>();
        List<String> lines = readFromFile(fileName);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                data.add(parts);
            }
        }
        return data;
    }

    private void appendToFile(String fileName, String line) {
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(fileName), (line + System.lineSeparator()).getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + ex.getMessage());
        }
    }

    private void showLecturerPanel(String lecturerId, String lecturerName) {
        this.currentLecturerId = lecturerId;
        this.currentLecturerName = lecturerName;
        if (lecturerPanel == null) {
            lecturerPanel = new JPanel(new BorderLayout());
            lecturerTabs = new JTabbedPane();
            lecturerTabs.addTab("My Courses", createLecturerCoursesTab());
            lecturerTabs.addTab("Students in My Courses", createLecturerStudentsTab());
            lecturerPanel.add(lecturerTabs, BorderLayout.CENTER);
        }
        setTitle("Lecturer Panel");
        getContentPane().removeAll();
        add(lecturerPanel);
        revalidate();
        repaint();
    }

    private JPanel createLecturerCoursesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Course ID", "Course Name"};
        List<String[]> allCourses = loadUserData("course.csv");
        List<String[]> myCourses = new ArrayList<>();
        for (String[] course : allCourses) {
            if (course[1].toLowerCase().contains(currentLecturerName.toLowerCase()) || course[0].equals(currentLecturerId)) {
                myCourses.add(course);
            }
        }
        JTable table = new JTable(myCourses.toArray(new String[0][0]), columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLecturerStudentsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Course ID", "Course Name", "Student ID", "Student Name"};
        List<String[]> allCourses = loadUserData("course.csv");
        List<String[]> registrations = loadUserData("registration.csv");
        List<String[]> students = loadUserData("student.csv");
        List<String[]> myRows = new ArrayList<>();
        for (String[] course : allCourses) {
            boolean isMyCourse = course[1].toLowerCase().contains(currentLecturerName.toLowerCase()) || course[0].equals(currentLecturerId);
            if (isMyCourse) {
                for (String[] reg : registrations) {
                    if (reg.length == 2 && reg[1].equals(course[0])) { // reg[0]=studentId, reg[1]=courseId
                        for (String[] student : students) {
                            if (student[0].equals(reg[0])) {
                                myRows.add(new String[]{course[0], course[1], student[0], student[1]});
                            }
                        }
                    }
                }
            }
        }
        JTable table = new JTable(myRows.toArray(new String[0][0]), columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void showStudentPanel(String studentId, String studentName) {
        this.currentStudentId = studentId;
        this.currentStudentName = studentName;
        if (studentPanel == null) {
            studentPanel = new JPanel(new BorderLayout());
            studentTabs = new JTabbedPane();
            studentTabs.addTab("Available Courses", createStudentAvailableCoursesTab());
            studentTabs.addTab("My Courses", createStudentRegisteredCoursesTab());
            studentPanel.add(studentTabs, BorderLayout.CENTER);
        }
        setTitle("Student Panel");
        getContentPane().removeAll();
        add(studentPanel);
        revalidate();
        repaint();
    }

    private JPanel createStudentAvailableCoursesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Course ID", "Course Name"};
        List<String[]> allCourses = loadUserData("course.csv");
        JTable table = new JTable(allCourses.toArray(new String[0][0]), columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        JButton registerButton = new JButton("Register for Selected Course");
        panel.add(registerButton, BorderLayout.SOUTH);
        registerButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String courseId = (String) table.getValueAt(row, 0);
                // Check if already registered
                List<String[]> registrations = loadUserData("registration.csv");
                boolean alreadyRegistered = false;
                for (String[] reg : registrations) {
                    if (reg.length == 2 && reg[0].equals(currentStudentId) && reg[1].equals(courseId)) {
                        alreadyRegistered = true;
                        break;
                    }
                }
                if (!alreadyRegistered) {
                    appendToFile("registration.csv", currentStudentId + "," + courseId);
                    JOptionPane.showMessageDialog(this, "Registered for course!");
                } else {
                    JOptionPane.showMessageDialog(this, "Already registered for this course.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a course to register.");
            }
        });
        return panel;
    }

    private JPanel createStudentRegisteredCoursesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Course ID", "Course Name"};
        List<String[]> allCourses = loadUserData("course.csv");
        List<String[]> registrations = loadUserData("registration.csv");
        List<String[]> myCourses = new ArrayList<>();
        for (String[] reg : registrations) {
            if (reg.length == 2 && reg[0].equals(currentStudentId)) {
                for (String[] course : allCourses) {
                    if (course[0].equals(reg[1])) {
                        myCourses.add(course);
                    }
                }
            }
        }
        JTable table = new JTable(myCourses.toArray(new String[0][0]), columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void handleLogin() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        if (id.equals("1")) {
            Admin admin = new Admin("1", "admin");
            if (admin.login(id, name)) {
                showAdminPanel();
            } else {
                messageLabel.setText("Invalid admin credentials");
            }
        } else {
            String userType = checkUserType(id, name);
            if (userType != null) {
                if (userType.equals("Lecturer")) {
                    showLecturerPanel(id, name);
                } else if (userType.equals("Student")) {
                    showStudentPanel(id, name);
                } else {
                    messageLabel.setText(userType + " logged in"); // Placeholder for future
                }
            } else {
                messageLabel.setText("Invalid credentials");
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
                return true;
            }
        }
        return false;
    }

    public static List<String> readFromFile(String fileName) {
        try {
            return Files.readAllLines(Paths.get(fileName));
        } catch (IOException ex) {
            // Optionally show error dialog
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
} 