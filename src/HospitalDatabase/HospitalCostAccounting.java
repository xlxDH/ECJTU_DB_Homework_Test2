package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;

public class HospitalCostAccounting extends JFrame {
    private JComboBox<String> costTypeComboBox;
    private JComboBox<String> costNameComboBox;
    private JTextField medIdField;
    private JTextField regIdField;
    private JTextField countField;
    private JTextField remarkField;
    private JButton recordButton;

    public void launch() {
        setSize(400, 300);
        setTitle("费用记账");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public HospitalCostAccounting() {
        JPanel panel = new JPanel(new GridLayout(7, 2));

        JLabel medIdLabel = new JLabel("医保卡号：");
        medIdField = new JTextField();
        JLabel regIdLabel = new JLabel("挂号ID：");
        regIdField = new JTextField();
        JLabel costTypeLabel = new JLabel("费用类型：");
        String[] costTypes = {"手术费", "药品费", "检查费", "住院费"};
        costTypeComboBox = new JComboBox<>(costTypes);
        JLabel costNameLabel = new JLabel("费用名称：");
        costNameComboBox = new JComboBox<>();
        JLabel countLabel = new JLabel("数量：");
        countField = new JTextField("1");
        JLabel remarkLabel = new JLabel("备注：");
        remarkField = new JTextField();
        recordButton = new JButton("记账");

        panel.add(medIdLabel);
        panel.add(medIdField);
        panel.add(regIdLabel);
        panel.add(regIdField);
        panel.add(costTypeLabel);
        panel.add(costTypeComboBox);
        panel.add(costNameLabel);
        panel.add(costNameComboBox);
        panel.add(countLabel);
        panel.add(countField);
        panel.add(remarkLabel);
        panel.add(remarkField);
        panel.add(recordButton);

        add(panel);

        costTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCostNames((String) costTypeComboBox.getSelectedItem());
            }
        });

        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordCost();
            }
        });
    }

    private void loadCostNames(String costType) {
        // Clear previous items
        costNameComboBox.removeAllItems();
        if (costType.equals("手术费")) {
            // Load operation names from database
            String url = "jdbc:postgresql://124.220.162.220:8000/medical";
            String user = "gaussdb";
            String password = "Enmo@123";
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "SELECT oper_name FROM medi_schema.tb_operation";
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        costNameComboBox.addItem(resultSet.getString("oper_name"));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "查询手术名称时发生错误: " + ex.getMessage());
            }
        } else if (costType.equals("药品费")) {
            // Load drug names from database
            String url = "jdbc:postgresql://124.220.162.220:8000/medical";
            String user = "gaussdb";
            String password = "Enmo@123";
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "SELECT drug_name FROM medi_schema.tb_drug";
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        costNameComboBox.addItem(resultSet.getString("drug_name"));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "查询药品名称时发生错误: " + ex.getMessage());
            }
        } else if (costType.equals("检查费")) {
            // Load examination names from database
            String url = "jdbc:postgresql://124.220.162.220:8000/medical";
            String user = "gaussdb";
            String password = "Enmo@123";
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "SELECT exam_name FROM medi_schema.tb_examination";
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        costNameComboBox.addItem(resultSet.getString("exam_name"));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "查询检查项目名称时发生错误: " + ex.getMessage());
            }
        } else if (costType.equals("住院费")) {
            // Custom handling for inpatient costs
            costNameComboBox.removeAllItems(); // remove items - user needs to directly input
        }
    }

    private void recordCost() {
        String medId = medIdField.getText();
        String regId = regIdField.getText();
        String costType = (String) costTypeComboBox.getSelectedItem();
        String costName = (String) costNameComboBox.getSelectedItem();
        String count = countField.getText();
        String remark = remarkField.getText();
        // Validate if required fields are not empty and perform other validation checks if needed
        boolean isValid = true; // Perform validation checks here

        if (isValid) {
            String url = "jdbc:postgresql://124.220.162.220:8000/medical";
            String user = "gaussdb";
            String password = "Enmo@123";
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "INSERT INTO medi_schema.tb_hospt_cost (med_id, hospt_reg_id, cost_type, cost_name, count, all_money, time, remark) " +
                        "VALUES (?, ?, ?, ?, ?, ?, current_timestamp, ?)";
                // Calculate total cost based on the count and item cost (if applicable)
                BigDecimal totalCost;
                if (costType.equals("住院费")) {
                    // Custom handling/tracking for inpatient costs
                    totalCost = BigDecimal.ZERO; // Perform custom calculations as required
                } else {
                    // Calculate the total cost based on count and item cost
                    BigDecimal itemCost = getItemCostFromDatabase(costType, costName);
                    totalCost = itemCost.multiply(new BigDecimal(count));
                }
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, Integer.parseInt(medId));
                    statement.setInt(2, Integer.parseInt(regId));
                    statement.setString(3, costType);
                    statement.setString(4, costName);
                    statement.setInt(5, Integer.parseInt(count));
                    statement.setBigDecimal(6, totalCost);
                    statement.setString(7, remark);

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(null, "费用记账成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "费用记账失败");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "费用记账失败: " + ex.getMessage());
            }
        }
    }

    private BigDecimal getItemCostFromDatabase(String costType, String itemName) {
        BigDecimal itemCost = BigDecimal.ZERO;
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";
        String tableName = "";
        String costColumnName = "";
        String payName = "";
        if (costType.equals("手术费")) {
            tableName = "medi_schema.tb_operation";
            costColumnName = "cost";
            payName = "oper_name";
        } else if (costType.equals("药品费")) {
            tableName = "medi_schema.tb_drug";
            costColumnName = "price";
            payName = "drug_name";
        } else if (costType.equals("检查费")) {
            tableName = "medi_schema.tb_examination";
            costColumnName = "cost";
            payName = "exam_name";
        }
        // Query the database to get the cost of the item
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT " + costColumnName + " FROM " + tableName + " WHERE " + payName + " = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, itemName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        itemCost = resultSet.getBigDecimal(costColumnName);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询费用信息时出错: " + ex.getMessage());
        }
        return itemCost;
    }

}
