import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

class Student implements Serializable {
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }
}

class StudentManagementSystem {
    private ArrayList<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
    }

    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    public ArrayList<Student> getAllStudents() {
        return students;
    }

    public void saveStudentsToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(students);
            JOptionPane.showMessageDialog(null, "Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving data.");
        }
    }

    public void loadStudentsFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            students = (ArrayList<Student>) ois.readObject();
            JOptionPane.showMessageDialog(null, "Data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data.");
        }
    }
}

public class StudentManagementSystemGUI {
    private JFrame frame;
    private StudentManagementSystem sms;

    public StudentManagementSystemGUI() {
        frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        sms = new StudentManagementSystem();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog("Enter file name to save data:");
                if (fileName != null && !fileName.isEmpty()) {
                    sms.saveStudentsToFile(fileName);
                }
            }
        });
        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog("Enter file name to load data:");
                if (fileName != null && !fileName.isEmpty()) {
                    sms.loadStudentsFromFile(fileName);
                }
            }
        });
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField nameField = new JTextField();
        JTextField rollNumberField = new JTextField();
        JTextField gradeField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Roll Number:"));
        panel.add(rollNumberField);
        panel.add(new JLabel("Grade:"));
        panel.add(gradeField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    int rollNumber = Integer.parseInt(rollNumberField.getText());
                    String grade = gradeField.getText();
                    if (!name.isEmpty() && !grade.isEmpty()) {
                        Student student = new Student(name, rollNumber, grade);
                        sms.addStudent(student);
                        JOptionPane.showMessageDialog(null, "Student added successfully.");
                        nameField.setText("");
                        rollNumberField.setText("");
                        gradeField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Name and grade cannot be empty.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid roll number.");
                }
            }
        });

        JButton removeButton = new JButton("Remove Student");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int rollNumber = Integer.parseInt(rollNumberField.getText());
                    sms.removeStudent(rollNumber);
                    JOptionPane.showMessageDialog(null, "Student removed successfully.");
                    nameField.setText("");
                    rollNumberField.setText("");
                    gradeField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid roll number.");
                }
            }
        });

        JButton searchButton = new JButton("Search Student");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int rollNumber = Integer.parseInt(rollNumberField.getText());
                    Student student = sms.searchStudent(rollNumber);
                    if (student != null) {
                        nameField.setText(student.getName());
                        gradeField.setText(student.getGrade());
                    } else {
                        JOptionPane.showMessageDialog(null, "Student not found.");
                        nameField.setText("");
                        gradeField.setText("");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid roll number.");
                }
            }
        });

        JButton displayButton = new JButton("Display All Students");
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Student> students = sms.getAllStudents();
                StringBuilder studentList = new StringBuilder();
                for (Student student : students) {
                    studentList.append(student.getName()).append(", Roll Number: ").append(student.getRollNumber())
                            .append(", Grade: ").append(student.getGrade()).append("\n");
                }
                if (studentList.length() == 0) {
                    JOptionPane.showMessageDialog(null, "No students to display.");
                } else {
                    JOptionPane.showMessageDialog(null, studentList.toString());
                }
            }
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.add(addButton, BorderLayout.SOUTH);
        frame.add(removeButton, BorderLayout.SOUTH);
        frame.add(searchButton, BorderLayout.SOUTH);
        frame.add(displayButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentManagementSystemGUI();
            }
        });
    }
}

