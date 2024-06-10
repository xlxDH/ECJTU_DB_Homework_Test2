package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("用户登录");
        setSize(1000, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        userLabel.setForeground(Color.white);
        userLabel.setBounds(400, 400, 100, 40);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(460, 400, 100, 40);
        usernameField.setFont(new Font("宋体", Font.PLAIN, 20));
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        passwordLabel.setBounds(400, 460, 100, 40);
        passwordLabel.setForeground(Color.white);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(460, 460, 100, 40);
        passwordField.setFont(new Font("宋体", Font.PLAIN, 20));
        panel.add(passwordField);

        JButton loginButton = new JButton("登录");
        loginButton.setFont(new Font("宋体", Font.PLAIN, 16));
        loginButton.setBounds(450, 550, 100, 40);
        panel.add(loginButton);

        JLabel winJlabel = new JLabel(new ImageIcon("img/背景图片.jpg"));
        winJlabel.setBounds(0, 0, 1000, 850);
        panel.add(winJlabel);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                try {
                    Class.forName("org.postgresql.Driver");
                    Connection c = DriverManager.getConnection("jdbc:postgresql://124.220.162.220:8000/medical", "gaussdb", "Enmo@123");
                    Statement stmt = c.createStatement();
                    String query = "SELECT * FROM medi_schema.tb_user WHERE uname='" + username + "' AND pwd='" + password + "'";
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "登录成功");
                        new MenuFrame();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "用户名或密码错误");
                    }
                    rs.close();
                    stmt.close();
                    c.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "登录失败: " + ex.getMessage());
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
