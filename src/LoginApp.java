import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginApp {

    static Connection connection = null;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        frame.setSize(300, 200);
        frame.setLayout(null);

        usernameLabel.setBounds(50, 30, 100, 20);
        passwordLabel.setBounds(50, 70, 100, 20);
        usernameField.setBounds(150, 30, 100, 20);
        passwordField.setBounds(150, 70, 100, 20);
        loginButton.setBounds(50, 110, 90, 20);
        registerButton.setBounds(160, 110, 90, 20);

        frame.add(usernameLabel);
        frame.add(passwordLabel);
        frame.add(usernameField);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(registerButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:db/login.db");
            Statement stmt = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)";
            stmt.execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    String insertUser = "INSERT INTO users (username, password) VALUES (?, ?)";
                    PreparedStatement pstmt = connection.prepareStatement(insertUser);
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "User registered successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "User already exists or error occurred.");
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    String queryUser = "SELECT * FROM users WHERE username = ? AND password = ?";
                    PreparedStatement pstmt = connection.prepareStatement(queryUser);
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(frame, "Login successful!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid credentials.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
