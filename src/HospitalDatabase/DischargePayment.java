package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;

public class DischargePayment extends JFrame {
    private int hosptRegId;
    private JTextField paymentField;
    private JButton refreshButton;
    private JButton payButton;
    private JTextArea resultTextArea;

    public void launch() {
        setSize(400, 300);
        setTitle("出院缴费");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public DischargePayment() {
        JTextField hosptRegIdField = new JTextField(10);
        JLabel hosptRegIdLabel = new JLabel("住院编号：");
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(hosptRegIdLabel);
        topPanel.add(hosptRegIdField);
        add(topPanel, BorderLayout.NORTH);

        paymentField = new JTextField(); // 输入缴费金额的文本框
        paymentField.setColumns(10);
        JLabel paymentLabel = new JLabel("缴费金额：");
        JPanel paymentPanel = new JPanel(new FlowLayout());
        paymentPanel.add(paymentLabel);
        paymentPanel.add(paymentField);
        add(paymentPanel, BorderLayout.CENTER);

        refreshButton = new JButton("刷新查询");
        payButton = new JButton("缴费");
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(refreshButton);
        bottomPanel.add(payButton);
        add(bottomPanel, BorderLayout.SOUTH);

        resultTextArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        add(scrollPane, BorderLayout.CENTER);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hosptRegId = Integer.parseInt(hosptRegIdField.getText());
                refreshSettlementInfo(hosptRegId);
            }
        });

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = JOptionPane.showInputDialog("请输入缴费金额：");

                if (userInput != null && !userInput.trim().isEmpty()) {
                    BigDecimal paymentAmount = new BigDecimal(userInput);
                    if (paymentAmount.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal totalMoney = retrieveTotalMoney(hosptRegId);
                        if (totalMoney.compareTo(BigDecimal.ZERO) > 0) {
                            if (paymentAmount.compareTo(totalMoney) >= 0) {
                                updateSettlement(paymentAmount, totalMoney, hosptRegId);
                            } else {
                                JOptionPane.showMessageDialog(null, "缴费金额不足，请重新输入缴费金额");
                            }

                            refreshSettlementInfo(hosptRegId); // 刷新显示缴费信息
                        }
                    }
                }
            }
        });
    }

    private void refreshSettlementInfo(int hosptRegId) {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT med_id, dept_id, total_money, pay_money, change_money, state, time " +
                    "FROM medi_schema.tb_hospt_settlement WHERE hospt_reg_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, hosptRegId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String medId = resultSet.getString("med_id");
                        String deptId = resultSet.getString("dept_id");
                        String totalMoney = resultSet.getString("total_money");
                        String payMoney = resultSet.getString("pay_money");
                        String changeMoney = resultSet.getString("change_money");
                        String state = resultSet.getString("state");
                        String time = resultSet.getString("time");

                        StringBuilder sb = new StringBuilder();
                        sb.append("医保卡号: ").append(medId).append("\n");
                        sb.append("部门ID: ").append(deptId).append("\n");
                        sb.append("总费用: ").append(totalMoney).append("\n");
                        sb.append("实缴金额: ").append(payMoney).append("\n");
                        sb.append("找零金额: ").append(changeMoney).append("\n");
                        sb.append("状态: ").append(state).append("\n");
                        sb.append("时间: ").append(time).append("\n");

                        resultTextArea.setText(sb.toString());
                    } else {
                        resultTextArea.setText("未找到相关的结算信息");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询结算信息时出错: " + ex.getMessage());
        }
    }

    private BigDecimal retrieveTotalMoney(int hosptRegId) {
        BigDecimal totalMoney = BigDecimal.ZERO;
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT total_money FROM medi_schema.tb_hospt_settlement WHERE hospt_reg_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, hosptRegId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        totalMoney = resultSet.getBigDecimal("total_money");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询应缴总金额时出错: " + ex.getMessage());
        }

        return totalMoney;
    }

    private void updateSettlement(BigDecimal paymentAmount, BigDecimal totalMoney, int hosptRegId) {
        if (paymentAmount.compareTo(totalMoney) >= 0) {
            BigDecimal changeAmount = paymentAmount.subtract(totalMoney); // 计算找零金额

            String url = "jdbc:postgresql://124.220.162.220:8000/medical";
            String user = "gaussdb";
            String password = "Enmo@123";

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "UPDATE medi_schema.tb_hospt_settlement SET pay_money = ?, change_money = ?, state = ?, time = current_timestamp " +
                        "WHERE hospt_reg_id = ?";
                String state = "已缴费";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setBigDecimal(1, paymentAmount);
                    statement.setBigDecimal(2, changeAmount);
                    statement.setString(3, state);
                    statement.setInt(4, hosptRegId);
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "缴费成功");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "缴费失败: " + ex.getMessage());
            }
        }
    }

}