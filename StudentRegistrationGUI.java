import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRegistrationGUI extends JFrame {
    private JTextField rollNoField, firstNameField, lastNameField, cityField, genderField, deptField, mobileNoField;
    private JButton registerButton, updateButton, deleteButton, displayButton;
    private Connection connection;

    
    public StudentRegistrationGUI() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student", "postgres", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setTitle("Student Registration System");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel rollNoLabel = new JLabel("Roll No: ");
        rollNoField = new JTextField(10);
        JLabel firstNameLabel = new JLabel("First Name: ");
        firstNameField = new JTextField(20);
        JLabel lastNameLabel = new JLabel("Last Name: ");
        lastNameField = new JTextField(20);
        JLabel cityLabel = new JLabel("City: ");
        cityField = new JTextField(20);
        JLabel genderLabel = new JLabel("Gender: ");
        genderField = new JTextField(10);
        JLabel deptLabel = new JLabel("Department: ");
        deptField = new JTextField(20);
        JLabel mobileNoLabel = new JLabel("Mobile No: ");
        mobileNoField = new JTextField(10);

        registerButton = new JButton("Insert");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        displayButton = new JButton("Display");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle registration (insert) here
                insertStudent();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle update here
                updateStudent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle deletion here
                deleteStudent();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle display here
                displayStudents();
            }
        });

        add(rollNoLabel);
        add(rollNoField);
        add(firstNameLabel);
        add(firstNameField);
        add(lastNameLabel);
        add(lastNameField);
        add(cityLabel);
        add(cityField);
        add(genderLabel);
        add(genderField);
        add(deptLabel);
        add(deptField);
        add(mobileNoLabel);
        add(mobileNoField);
        add(registerButton);
        add(updateButton);
        add(deleteButton);
        add(displayButton);

        setVisible(true);
    }

  
    public void insertStudent() {
        try {
            String insertQuery = "INSERT INTO student (roll_no, first_name, last_name, gender, city, dept, mobile_no) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            int rollNo = Integer.parseInt(rollNoField.getText());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String gender = genderField.getText();
            String city = cityField.getText();
            String dept = deptField.getText();
            String mobileNo = mobileNoField.getText();
            insertStatement.setInt(1, rollNo);
            insertStatement.setString(2, firstName);
            insertStatement.setString(3, lastName);
            insertStatement.setString(4, gender);
            insertStatement.setString(5, city);
            insertStatement.setString(6, dept);
            insertStatement.setString(7, mobileNo);

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Student registered successfully.");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to register the student.");
            }
            insertStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
private void updateStudent() {
    try {
        int rollNoToUpdate = Integer.parseInt(rollNoField.getText());
        String updatedFirstName = firstNameField.getText();
        String updatedLastName = lastNameField.getText();
        String updatedMobileNo = mobileNoField.getText();

        String updateQuery = "UPDATE student SET first_name = ?, last_name = ?, mobile_no = ? WHERE roll_no = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, updatedFirstName);
        preparedStatement.setString(2, updatedLastName);
        preparedStatement.setString(3, updatedMobileNo);
        preparedStatement.setInt(4, rollNoToUpdate); // Specify the roll_no of the student to update

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Student information updated successfully.");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update the student information.");
        }
        preparedStatement.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


    private void deleteStudent() {
        try {
            int rollNoToDelete = Integer.parseInt(rollNoField.getText());
           String deleteQuery = "DELETE FROM student WHERE roll_no = ?";

            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, rollNoToDelete);

            int deletedRows = deleteStatement.executeUpdate();
            if (deletedRows > 0) {
                JOptionPane.showMessageDialog(null, "Student with roll_no " + rollNoToDelete + " deleted successfully.");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "No student found with roll_no " + rollNoToDelete);
            }
            deleteStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayStudents() {
        try {
            String selectQuery = "SELECT * FROM student";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();

            StringBuilder result = new StringBuilder("Student Records:\n\n");
            while (resultSet.next()) {
                result.append("Roll No: ").append(resultSet.getInt("roll_no")).append("\n");
                result.append("First Name: ").append(resultSet.getString("first_name")).append("\n");
                result.append("Last Name: ").append(resultSet.getString("last_name")).append("\n");
                result.append("City: ").append(resultSet.getString("city")).append("\n");
                result.append("Gender: ").append(resultSet.getString("gender")).append("\n");
                result.append("Department: ").append(resultSet.getString("dept")).append("\n");
                result.append("Mobile No: ").append(resultSet.getString("mobile_no")).append("\n\n");
            }
            JOptionPane.showMessageDialog(null, result.toString());
            resultSet.close();
            selectStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        rollNoField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        cityField.setText("");
        genderField.setText("");
        deptField.setText("");
        mobileNoField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistrationGUI());
    }
}
