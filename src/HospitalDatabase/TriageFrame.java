package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TriageFrame extends JFrame {
    private JTextArea triageList;

    public void launch() {
        setSize(800, 800);
        setResizable(false);
        setTitle("叫号分诊");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public TriageFrame() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        triageList = new JTextArea(10, 30);
        triageList.setFont(new Font("宋体", Font.PLAIN, 16));
        triageList.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(triageList);

        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("刷新列表");
        refreshButton.setFont(new Font("宋体", Font.PLAIN, 16));
        panel.add(refreshButton, BorderLayout.NORTH);

        JButton triageButton = new JButton("执行分诊");
        triageButton.setFont(new Font("宋体", Font.PLAIN, 16));
        panel.add(triageButton, BorderLayout.SOUTH);

        setContentPane(panel);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTriageList();
            }
        });

        triageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triageSelected();
            }
        });
    }

    private void refreshTriageList() {
        StringBuilder sb = new StringBuilder();

        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT reg_id, state FROM medi_schema.tb_register WHERE state IN ('待就诊', '就诊中', '待回诊','回诊中')";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int regId = resultSet.getInt("reg_id");
                    String state = resultSet.getString("state");
                    sb.append("挂号单号: ").append(regId).append(", 状态: ").append(state).append("\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }

        triageList.setText(sb.toString());
    }

    private void triageSelected() {
        String input = JOptionPane.showInputDialog(null, "请输入需要执行分诊的挂号单号:");
        if (input != null && !input.isEmpty()) {
            int regId = Integer.parseInt(input);
            executeTriageProcedure(regId);
        }
    }

    private void executeTriageProcedure(int regId) {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "CALL medical.medi_schema.register_upd(?)";
            try (CallableStatement statement = connection.prepareCall(sql)) {
                statement.setInt(1, regId);
                statement.execute();
                JOptionPane.showMessageDialog(null, "分诊操作执行成功");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "执行分诊存储过程出错: " + ex.getMessage());
        }
    }

}