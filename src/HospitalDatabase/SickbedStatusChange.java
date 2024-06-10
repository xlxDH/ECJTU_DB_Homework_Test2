package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SickbedStatusChange extends JFrame {
    private JTextField bedIdField;

    public void launch() {
        setSize(400, 200);
        setTitle("床位状态修改");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public SickbedStatusChange() {
        JPanel panel = new JPanel();

        bedIdField = new JTextField(10);
        JButton changeStatusButton = new JButton("修改床位状态");

        panel.add(new JLabel("床位号："));
        panel.add(bedIdField);
        panel.add(changeStatusButton);

        add(panel);

        changeStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bedId = bedIdField.getText();
                if(bedId.equals("")) {
                    JOptionPane.showMessageDialog(null, "请输入床位号");
                    return;
                }
                // 调用方法更新床位状态
                updateBedStatus(bedId);
            }
        });
    }

    private void updateBedStatus(String bedId) {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";
        String currentState = getCurrentStateFromDatabase(bedId, url, user, password);
        if(currentState != null) {
            String updatedState = currentState.equals("空闲") ? "占用" : "空闲";
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "UPDATE medi_schema.tb_sickbed SET state = ? WHERE bed_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, updatedState);
                    statement.setInt(2, Integer.parseInt(bedId));
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "床位状态修改成功，当前状态为：" + updatedState);
                    } else {
                        JOptionPane.showMessageDialog(null, "床位状态修改失败");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "床位状态修改出错: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "床位号不存在或发生错误");
        }
    }

    private String getCurrentStateFromDatabase(String bedId, String url, String user, String password) {
        String currentState = null;
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT state FROM medi_schema.tb_sickbed WHERE bed_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(bedId));
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        currentState = resultSet.getString("state");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询床位状态时发生错误: " + ex.getMessage());
        }
        return currentState;
    }

}
