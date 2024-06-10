package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PatientView extends JFrame {
    private JTextField medIdField;
    private JTextArea resultArea;

    public void launch() {
        setSize(400, 300);
        setTitle("病人信息查询");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public PatientView() {
        JPanel panel = new JPanel();

        medIdField = new JTextField(10);
        JButton queryButton = new JButton("查询");

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(new JLabel("医保卡号："));
        panel.add(medIdField);
        panel.add(queryButton);
        add(panel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String medId = medIdField.getText();
                if(medId.equals("")) {
                    JOptionPane.showMessageDialog(null, "请输入医保卡号");
                    return;
                }
                showRegisterInfo(medId);
            }
        });
    }

    private void showRegisterInfo(String medId) {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        resultArea.setText(""); // 清空先前的显示

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM medi_schema.tb_register WHERE med_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(medId));
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        resultArea.append("挂号ID: " + resultSet.getInt("reg_id") + "\n");
                        resultArea.append("医保卡号: " + resultSet.getInt("med_id") + "\n");
                        resultArea.append("挂号类型: " + resultSet.getString("reg_type") + "\n");
                        resultArea.append("挂号费用: " + resultSet.getBigDecimal("reg_price") + "\n");
                        resultArea.append("其他费用: " + resultSet.getBigDecimal("other_price") + "\n");
                        resultArea.append("科室ID: " + resultSet.getInt("dept_id") + "\n");
                        resultArea.append("用户ID: " + resultSet.getInt("uid") + "\n");
                        resultArea.append("挂号时间: " + resultSet.getTimestamp("rerister_time") + "\n");
                        resultArea.append("就诊时间: " + resultSet.getDate("visit_time") + "\n");
                        resultArea.append("状态: " + resultSet.getString("state") + "\n\n");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询病人信息时出错: " + ex.getMessage());
        }
    }


}
