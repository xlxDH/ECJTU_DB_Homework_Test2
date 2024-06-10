package HospitalDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

public class OperationManagement extends JFrame {
    private JTable operationTable;

    public void launch() {
        setSize(800, 600);
        setTitle("手术项目管理");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public OperationManagement() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("手术项目编号");
        columnNames.add("手术项目名称");
        columnNames.add("费用");
        columnNames.add("备注");

        Vector<Vector<Object>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        operationTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(operationTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("添加手术项目");
        panel.add(addButton, BorderLayout.SOUTH);
        add(panel);

        refreshOperationList();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField nameField = new JTextField(10);
                JTextField costField = new JTextField(10);
                JTextField remarkField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("手术项目名称:"));
                myPanel.add(nameField);
                myPanel.add(new JLabel("费用:"));
                myPanel.add(costField);
                myPanel.add(new JLabel("备注:"));
                myPanel.add(remarkField);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "添加手术项目", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String operationName = nameField.getText();
                    BigDecimal cost = new BigDecimal(costField.getText());
                    String remark = remarkField.getText();

                    // 插入新手术项目信息
                    String url = "jdbc:postgresql://124.220.162.220:8000/medical";
                    String user = "gaussdb";
                    String password = "Enmo@123";

                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        String sql = "INSERT INTO medi_schema.tb_operation (oper_name, cost, remark) VALUES (?, ?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, operationName);
                            statement.setBigDecimal(2, cost);
                            statement.setString(3, remark);
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(null, "新手术项目添加成功");

                                // 添加成功后刷新手术项目列表
                                refreshOperationList();
                            } else {
                                JOptionPane.showMessageDialog(null, "添加失败");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "添加失败: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void refreshOperationList() {
        DefaultTableModel model = (DefaultTableModel) operationTable.getModel();
        model.setRowCount(0); // 清空表格数据

        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT oper_id, oper_name, cost, remark FROM medi_schema.tb_operation";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet =  statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Object[] row = new Object[4];
                    row[0] = resultSet.getInt("oper_id");
                    row[1] = resultSet.getString("oper_name");
                    row[2] = resultSet.getBigDecimal("cost");
                    row[3] = resultSet.getString("remark");
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

}
