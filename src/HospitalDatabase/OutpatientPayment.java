package HospitalDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OutpatientPayment extends JFrame {
    private JTable paymentTable;

    public void launch() {
        setSize(800, 600);
        setTitle("门诊缴费");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public OutpatientPayment() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("缴费单号");
        columnNames.add("医疗卡号");
        columnNames.add("挂号单号");
        columnNames.add("费用名称");
        columnNames.add("数量");
        columnNames.add("总金额");
        columnNames.add("已缴金额");
        columnNames.add("找零");
        columnNames.add("缴费时间");
        columnNames.add("状态");

        Vector<Vector<Object>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        paymentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 添加刷新和缴费按钮
        JButton refreshButton = new JButton("刷新");
        JButton payButton = new JButton("缴费");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(payButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String regId = JOptionPane.showInputDialog("请输入挂号单号:");
                refreshPaymentList(regId);
            }
        });

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = paymentTable.getSelectedRow();
                if (selectedRow != -1) {
                    int outId = (int) paymentTable.getValueAt(selectedRow, 0);
                    BigDecimal totalMoney = (BigDecimal) paymentTable.getValueAt(selectedRow, 5);
                    BigDecimal payMoney = (BigDecimal) paymentTable.getValueAt(selectedRow, 6);
                    BigDecimal changeMoney = (BigDecimal) paymentTable.getValueAt(selectedRow, 7);
                    String state = (String) paymentTable.getValueAt(selectedRow, 9);

                    PaymentDialog paymentDialog = new PaymentDialog(outId, totalMoney, payMoney, changeMoney, state);
                    paymentDialog.setVisible(true);
                    int regId = (int) paymentTable.getValueAt(selectedRow, 2);
                    int medId = (int) paymentTable.getValueAt(selectedRow, 1);
                    // TODO: 更新药方状态为已缴费
                    updatePrescriptionPaymentStatus(regId,medId);
                } else {
                    JOptionPane.showMessageDialog(null, "请选择要缴费的项目");
                }
            }
        });

        add(panel);
    }

    private void refreshPaymentList(String regId) {
        DefaultTableModel model = (DefaultTableModel) paymentTable.getModel();
        model.setRowCount(0); // 清空表格数据

        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM medi_schema.tb_outpatient_charges WHERE reg_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(regId));
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Object[] row = new Object[10];
                        row[0] = resultSet.getInt("out_id");
                        row[1] = resultSet.getInt("med_id");
                        row[2] = resultSet.getInt("reg_id");
                        row[3] = resultSet.getString("cost_name");
                        row[4] = resultSet.getInt("count");
                        row[5] = resultSet.getBigDecimal("total_money");
                        row[6] = resultSet.getBigDecimal("pay_money");
                        row[7] = resultSet.getBigDecimal("change_money");
                        row[8] = resultSet.getTimestamp("time");
                        row[9] = resultSet.getString("state");
                        model.addRow(row);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

    private void updatePrescriptionPaymentStatus(int regId, int medId) {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setAutoCommit(false); // 设置为手动提交事务
            String selectSql = "SELECT * FROM medi_schema.tb_prescription WHERE reg_id = ? AND med_id = ?";
            String updateSql = "UPDATE medi_schema.tb_prescription SET state = '已缴费' WHERE reg_id = ? AND med_id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                 PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                // 设置参数并执行查询
                selectStatement.setInt(1, regId);
                selectStatement.setInt(2, medId);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // 执行更新操作
                        updateStatement.setInt(1, regId);
                        updateStatement.setInt(2, medId);
                        updateStatement.executeUpdate();
                    }
                }
            }
            connection.commit(); // 手动提交事务
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

}