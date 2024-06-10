package HospitalDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class HospitalCostQuery extends JFrame {
    private JTable costTable;
    private JTextField medIdField;

    public void launch() {
        setSize(800, 600);
        setTitle("费用查询");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public HospitalCostQuery() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("费用编号");
        columnNames.add("医保卡号");
        columnNames.add("挂号ID");
        columnNames.add("费用类型");
        columnNames.add("费用名称");
        columnNames.add("数量");
        columnNames.add("总金额");
        columnNames.add("时间");
        columnNames.add("备注");

        Vector<Vector<Object>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        costTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(costTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        medIdField = new JTextField(10);
        JButton queryButton = new JButton("查询费用");
        inputPanel.add(new JLabel("医保卡号: "));
        inputPanel.add(medIdField);
        inputPanel.add(queryButton);
        panel.add(inputPanel, BorderLayout.NORTH);

        add(panel);

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryCostByMedId(medIdField.getText());
            }
        });
    }

    private void queryCostByMedId(String medId) {
        // 清空费用表格
        ((DefaultTableModel) costTable.getModel()).setRowCount(0);

        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM medi_schema.tb_hospt_cost WHERE med_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(medId));
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Vector<Object> row = new Vector<>();
                        row.add(resultSet.getInt("hospt_cost_id"));
                        row.add(resultSet.getInt("med_id"));
                        row.add(resultSet.getInt("hospt_reg_id"));
                        row.add(resultSet.getString("cost_type"));
                        row.add(resultSet.getString("cost_name"));
                        row.add(resultSet.getInt("count"));
                        row.add(resultSet.getBigDecimal("all_money"));
                        row.add(resultSet.getTimestamp("time"));
                        row.add(resultSet.getString("remark"));
                        ((DefaultTableModel) costTable.getModel()).addRow(row);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

}
