package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;

public class HospitalRegistration extends JFrame {
    private JTextField medicalCardField;
    private JComboBox<String> deptComboBox;
    private JComboBox<Integer> userIdComboBox;
    private JComboBox<Integer> roomComboBox;
    private JComboBox<Integer> bedComboBox;

    private static final String DB_URL = "jdbc:postgresql://124.220.162.220:8000/medical";
    private static final String USER = "gaussdb";
    private static final String PASSWORD = "Enmo@123";


    public HospitalRegistration() {
        setTitle("住院登记");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        formPanel.add(new JLabel("医保卡号："));
        medicalCardField = new JTextField(10);
        formPanel.add(medicalCardField);

        formPanel.add(new JLabel("科室名："));
        deptComboBox = new JComboBox<>();
        fetchDepartmentNamesAndIds(deptComboBox); // 从数据库中获取科室名称
        formPanel.add(deptComboBox);

        formPanel.add(new JLabel("用户ID："));
        userIdComboBox = new JComboBox<>();
        fetchUserIds(userIdComboBox);
        formPanel.add(userIdComboBox);

        formPanel.add(new JLabel("病房号："));
        roomComboBox = new JComboBox<>();
        fetchRoomNumbers(roomComboBox);
        formPanel.add(roomComboBox);

        formPanel.add(new JLabel("病床号："));
        bedComboBox = new JComboBox<>();
        fetchBedNumbers(bedComboBox);
        formPanel.add(bedComboBox);

        add(formPanel, BorderLayout.CENTER);

        JButton registerButton = new JButton("登记");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String medicalCard = medicalCardField.getText();
                String deptAndId = (String) deptComboBox.getSelectedItem();
                int indexOfDash = deptAndId.indexOf("-");
                int deptId = Integer.parseInt(deptAndId.substring(0, indexOfDash).trim());
                int userId = (int) userIdComboBox.getSelectedItem();
                int roomId = (int) roomComboBox.getSelectedItem();
                int bedId = (int) bedComboBox.getSelectedItem();

                // 将数据插入到数据库
                try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                    String sql = "INSERT INTO medi_schema.tb_hospt_register (med_id, dept_id, uid, room_id, bed_id, state) " +
                            "VALUES (?, ?, ?, ?, ?, ?) RETURNING hospt_reg_id"; // 添加 RETURNING 以便返回住院单号
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, medicalCard);
                        statement.setInt(2, deptId);
                        statement.setInt(3, userId);
                        statement.setInt(4, roomId);
                        statement.setInt(5, bedId);
                        statement.setString(6, "待入院"); // 示例状态
                        ResultSet resultSet = statement.executeQuery();

                        // 提取住院单号
                        int hosptRegId = 0;
                        if(resultSet.next()) {
                            hosptRegId = resultSet.getInt(1);
                        }

                        // 向医院账单表中添加一组数据
                        sql = "INSERT INTO medi_schema.tb_hospt_cost (med_id, hospt_reg_id, cost_type, cost_name, count, all_money, time, remark) " +
                                "VALUES (?, ?, ?, ?, ?, ?, current_timestamp, ?)";
                        try (PreparedStatement costStatement = connection.prepareStatement(sql)) {
                            costStatement.setString(1, medicalCard);
                            costStatement.setInt(2, hosptRegId);
                            costStatement.setString(3, "住院费");
                            costStatement.setString(4, "住院费");
                            costStatement.setInt(5, 1);
                            costStatement.setBigDecimal(6, new BigDecimal(100));
                            costStatement.setString(7, "无"); // 设置默认的备注信息

                            int costRowsInserted = costStatement.executeUpdate();
                            if (costRowsInserted > 0) {
                                JOptionPane.showMessageDialog(null, "住院登记成功，账单添加成功");
                            } else {
                                JOptionPane.showMessageDialog(null, "住院登记成功，账单添加失败");
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "住院登记失败: " + ex.getMessage());
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void fetchBedNumbers(JComboBox<Integer> comboBox) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT bed_id FROM medi_schema.tb_sickbed";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int bedNumber = resultSet.getInt("bed_id");
                    comboBox.addItem(bedNumber);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

    // 从数据库中加载病房号
    public void fetchRoomNumbers(JComboBox<Integer> comboBox) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT room_id FROM medi_schema.tb_sickroom";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_id");
                    comboBox.addItem(roomNumber);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

    // 从数据库中加载用户ID
    public void fetchUserIds(JComboBox<Integer> comboBox) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT uid FROM medi_schema.tb_user";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("uid");
                    comboBox.addItem(userId);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

    // 从数据库中获取科室名称并添加到下拉框中
    public void fetchDepartmentNamesAndIds(JComboBox<String> comboBox) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT dept_id, dept_name FROM medi_schema.tb_department";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int deptId = resultSet.getInt("dept_id");
                    String deptName = resultSet.getString("dept_name");
                    comboBox.addItem(deptId + " - " + deptName);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

}