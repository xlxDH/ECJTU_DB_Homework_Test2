package HospitalDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class PaymentDialog extends JDialog {
    public PaymentDialog(int outId, BigDecimal totalMoney, BigDecimal payMoney, BigDecimal changeMoney, String state) {
        // 初始化对话框内容和布局
        // ...

        JButton confirmButton = new JButton("确认缴费");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("请输入缴费金额:");
                try {
                    BigDecimal paid = new BigDecimal(input);
                    // 执行缴费逻辑
                    if (paid.compareTo(totalMoney.subtract(payMoney)) >= 0) {
                        BigDecimal newPayMoney = payMoney.add(paid);
                        BigDecimal newChangeMoney = paid.subtract(totalMoney.subtract(payMoney));

                        // 更新数据库信息
                        updatePaymentInfo(outId, newPayMoney, newChangeMoney, "已缴费");
                    } else {
                        JOptionPane.showMessageDialog(null, "缴费金额不足");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "请输入有效的缴费金额");
                }
            }
        });

        add(confirmButton, BorderLayout.SOUTH);

        // 设置对话框大小和可见性
        setSize(300, 150);
        setLocationRelativeTo(null);
        setModal(true);
        setTitle("缴费确认");
    }

    private void updatePaymentInfo(int outId, BigDecimal newPayMoney, BigDecimal newChangeMoney, String newState) {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE medi_schema.tb_outpatient_charges SET pay_money = ?, change_money = ?, state = ? WHERE out_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setBigDecimal(1, newPayMoney);
                statement.setBigDecimal(2, newChangeMoney);
                statement.setString(3, newState);
                statement.setInt(4, outId);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "缴费成功");
                    // 刷新缴费列表
                    // ...
                } else {
                    JOptionPane.showMessageDialog(null, "缴费失败");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库更新出错: " + ex.getMessage());
        }
    }
}
