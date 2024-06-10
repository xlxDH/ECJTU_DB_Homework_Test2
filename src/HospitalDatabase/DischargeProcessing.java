package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;

public class DischargeProcessing extends JFrame {
    private int hosptRegId;
    private JTextArea costDetailArea;
    private JButton refreshButton;
    private JButton settleButton;

    public void launch() {
        setSize(400, 300);
        setTitle("出院办理");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public DischargeProcessing() {
        JTextField hosptRegIdField = new JTextField(10);
        JLabel hosptRegIdLabel = new JLabel("住院编号：");
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(hosptRegIdLabel);
        topPanel.add(hosptRegIdField);
        add(topPanel, BorderLayout.NORTH);

        costDetailArea = new JTextArea("费用明细...");
        JScrollPane scrollPane = new JScrollPane(costDetailArea);
        add(scrollPane, BorderLayout.CENTER);

        refreshButton = new JButton("刷新费用明细");
        settleButton = new JButton("结算出院费用");
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(refreshButton);
        bottomPanel.add(settleButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hosptRegId = Integer.parseInt(hosptRegIdField.getText());
                refreshCostDetails(hosptRegId);
            }
        });

        settleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settleDischargeCosts(hosptRegId);
            }
        });
    }

    private void refreshCostDetails(int hosptRegId) {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        costDetailArea.setText(""); // 清空先前的显示

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM medi_schema.tb_hospt_cost WHERE hospt_reg_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, hosptRegId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        costDetailArea.append("费用类型: " + resultSet.getString("cost_type") + "\n");
                        costDetailArea.append("费用名称: " + resultSet.getString("cost_name") + "\n");
                        costDetailArea.append("数量: " + resultSet.getInt("count") + "\n");
                        costDetailArea.append("总金额: " + resultSet.getBigDecimal("all_money") + "\n");
                        costDetailArea.append("时间: " + resultSet.getTimestamp("time") + "\n");
                        costDetailArea.append("备注: " + resultSet.getString("remark") + "\n\n");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询费用信息时出错: " + ex.getMessage());
        }
    }

    private int retrieveDeptId(int hosptRegId) {
        int deptId = 0;
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT dept_id FROM medi_schema.tb_hospt_register WHERE hospt_reg_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, hosptRegId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        deptId = resultSet.getInt("dept_id");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询部门ID时发生错误: " + ex.getMessage());
        }
        return deptId;
    }

    private void settleDischargeCosts(int hosptRegId) {
        int medId = getMedIdByHosptRegId(hosptRegId); // 获取住院编号对应的医保卡号
        int dept_id = retrieveDeptId(hosptRegId);
        if (medId != 0) {
            BigDecimal totalCost = calculateTotalCost(hosptRegId); // 计算总费用

            if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
                String url = "jdbc:postgresql://124.220.162.220:8000/medical";
                String user = "gaussdb";
                String password = "Enmo@123";
                try (Connection connection = DriverManager.getConnection(url, user, password)) {
                    String sql = "INSERT INTO medi_schema.tb_hospt_settlement (med_id, hospt_reg_id, dept_id, total_money, pay_money, change_money, state, time) " +
                            "VALUES (?, ?, ?, ?, ? , ? , ? , current_timestamp)";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, medId);
                        statement.setInt(2, hosptRegId);
                        statement.setInt(3, dept_id);
                        statement.setBigDecimal(4, totalCost);
                        statement.setInt(5, 0);
                        statement.setInt(6,0);
                        statement.setString(7, "未缴费");
                        statement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "出院办理成功");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "费用结算失败: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "总费用为0，无需结算");
            }
        } else {
            JOptionPane.showMessageDialog(null, "无法获取医保卡号，无法结算费用");
        }
    }

    private int getMedIdByHosptRegId(int hosptRegId) {
        int medId = 0;
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT med_id FROM medi_schema.tb_hospt_register WHERE hospt_reg_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, hosptRegId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        medId = resultSet.getInt("med_id");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询医保卡号时出错: " + ex.getMessage());
        }
        return medId;
    }

    private BigDecimal calculateTotalCost(int hosptRegId) {
        BigDecimal totalCost = BigDecimal.ZERO;
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT SUM(all_money) AS total_cost FROM medi_schema.tb_hospt_cost WHERE hospt_reg_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, hosptRegId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        totalCost = resultSet.getBigDecimal("total_cost");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "计算总费用时发生错误: " + ex.getMessage());
        }
        return totalCost;
    }


}
