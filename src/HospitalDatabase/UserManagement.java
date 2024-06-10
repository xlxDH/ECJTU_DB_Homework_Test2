package HospitalDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class UserManagement extends JFrame {
    private JTable userTable;

    public void launch() {
        setSize(800, 600);
        setTitle("用户管理");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public UserManagement() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("用户编号");
        columnNames.add("用户名");
        columnNames.add("真实姓名");
        columnNames.add("用户类别");
        columnNames.add("性别");
        columnNames.add("电话");
        columnNames.add("备注");

        Vector<Vector<Object>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        userTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel);

        refreshUserList();

        setTitle("User Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

//        fetchAndDisplayUserTypes();
        JButton addUserButton = new JButton("添加用户");
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField userNameField = new JTextField(10);
                JTextField realNameField = new JTextField(10);
                JComboBox<String> userTypeDropdown = new JComboBox<>();
                JComboBox<String> departmentDropdown = new JComboBox<>();
                JTextField sexField = new JTextField(10);
                JPasswordField passwordField = new JPasswordField(10);
                JTextField phoneField = new JTextField(10);
                JTextField remarkField = new JTextField(10);
                JTextField moneyField = new JTextField(10);

                fetchUserTypes(userTypeDropdown); // 获取用户类别
                fetchDepartments(departmentDropdown);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("用户名:"));
                myPanel.add(userNameField);
                myPanel.add(new JLabel("密码:"));
                myPanel.add(passwordField);
                myPanel.add(new JLabel("真实姓名:"));
                myPanel.add(realNameField);
                myPanel.add(new JLabel("用户类别:"));
                myPanel.add(userTypeDropdown);
                myPanel.add(new JLabel("科室:"));
                myPanel.add(departmentDropdown);
                myPanel.add(new JLabel("性别:"));
                myPanel.add(sexField);
                myPanel.add(new JLabel("电话:"));
                myPanel.add(phoneField);
                myPanel.add(new JLabel("月薪:"));
                myPanel.add(moneyField);
                myPanel.add(new JLabel("备注:"));
                myPanel.add(remarkField);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "添加用户", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String userName = userNameField.getText();
                    char[] password1 = passwordField.getPassword();
                    String realName = realNameField.getText();
                    String userType = (String)userTypeDropdown.getSelectedItem();
                    String department = (String)departmentDropdown.getSelectedItem();
                    String sex = sexField.getText();
                    String phone = phoneField.getText();
                    String money = moneyField.getText();
                    String remark = remarkField.getText();

                    // 插入新用户信息
                    String url = "jdbc:postgresql://124.220.162.220:8000/medical";
                    String user = "gaussdb";
                    String password = "Enmo@123";

                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        String sql = "INSERT INTO medi_schema.tb_user (uname, real_name, pwd, type_id, sex, phone, dept_id, money, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, userName);
                            statement.setString(2, realName);
                            statement.setString(3, String.valueOf(password1));
                            int typeId = getUserTypeId(userType, connection);
                            int deptId = getDepartmentId(department, connection);
                            statement.setInt(4, typeId);
                            statement.setString(5, sex);
                            statement.setString(6, phone);
                            statement.setInt(7, deptId);
                            statement.setString(8,money);
                            statement.setString(9, remark);
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(null, "新用户添加成功");
                                // 刷新用户列表
                                refreshUserList();
                            } else {
                                JOptionPane.showMessageDialog(null, "添加失败");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "添加失败: " + ex.getMessage());
                    }
                }
            }
        });

        panel.add(addUserButton, BorderLayout.EAST);
    }

    private void fetchDepartments(JComboBox<String> departmentDropdown) {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT dept_id, dept_name FROM medi_schema.tb_department";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String deptName = resultSet.getString("dept_name");
                    departmentDropdown.addItem(deptName);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

    private void refreshUserList() {
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);

        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT u.uid, u.uname, u.real_name, t.tname as user_type, u.sex, u.phone, u.remark " +
                    "FROM medi_schema.tb_user u " +
                    "JOIN medi_schema.tb_usertype t ON u.type_id = t.utid";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(resultSet.getInt("uid"));
                    row.add(resultSet.getString("uname"));
                    row.add(resultSet.getString("real_name"));
                    row.add(resultSet.getString("user_type"));
                    row.add(resultSet.getString("sex"));
                    row.add(resultSet.getString("phone"));
                    row.add(resultSet.getString("remark"));
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database query error: " + ex.getMessage());
        }
    }

    private void fetchUserTypes(JComboBox<String> userTypeDropdown) {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM medi_schema.tb_usertype";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String typeName = resultSet.getString("tname");
                    userTypeDropdown.addItem(typeName);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

    private int getUserTypeId(String typeName, Connection connection) throws SQLException {
        String sql = "SELECT utid FROM medi_schema.tb_usertype WHERE tname = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, typeName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("utid");
                }
            }
        }

        // 默认返回 -1 表示未找到
        return -1;
    }

    private int getDepartmentId(String departmentName, Connection connection) throws SQLException {
        String sql = "SELECT dept_id FROM medi_schema.tb_department WHERE dept_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, departmentName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("dept_id");
                }
            }
        }

        // 默认返回 -1 表示未找到
        return -1;
    }

}
