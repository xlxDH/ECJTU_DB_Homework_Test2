package HospitalDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;
public class ExaminationManagement extends JFrame {
    private JTable examinationTable;

    public void launch() {
        setSize(800, 600);
        setTitle("检查项目管理");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public ExaminationManagement() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("检查项目编号");
        columnNames.add("检查项目名称");
        columnNames.add("费用");
        columnNames.add("地址");
        columnNames.add("备注");

        Vector<Vector<Object>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        examinationTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(examinationTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("添加检查项目");
        panel.add(addButton, BorderLayout.SOUTH);
        add(panel);

        refreshExaminationList();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField nameField = new JTextField(10);
                JTextField costField = new JTextField(10);
                JTextField addressField = new JTextField(10);
                JTextField remarkField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("检查项目名称:"));
                myPanel.add(nameField);
                myPanel.add(new JLabel("费用:"));
                myPanel.add(costField);
                myPanel.add(new JLabel("地址:"));
                myPanel.add(addressField);
                myPanel.add(new JLabel("备注:"));
                myPanel.add(remarkField);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "添加检查项目", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String examName = nameField.getText();
                    BigDecimal cost = new BigDecimal(costField.getText());
                    String address = addressField.getText();
                    String remark = remarkField.getText();

                    // Insert new examination information
                    String url = "jdbc:postgresql://124.220.162.220:8000/medical";
                    String user = "gaussdb";
                    String password = "Enmo@123";

                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        String sql = "INSERT INTO medi_schema.tb_examination (exam_name, cost, addr, remark) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, examName);
                            statement.setBigDecimal(2, cost);
                            statement.setString(3, address);
                            statement.setString(4, remark);
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(null, "新检查项目添加成功");
                                // Refresh examination list after successful addition
                                refreshExaminationList();
                            } else {
                                JOptionPane.showMessageDialog(null, "Addition failed");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Addition failed: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void refreshExaminationList() {
        DefaultTableModel model = (DefaultTableModel) examinationTable.getModel();
        model.setRowCount(0); // Clear table data

        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT exam_id, exam_name, cost, addr, remark FROM medi_schema.tb_examination";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Object[] row = new Object[5];
                    row[0] = resultSet.getInt("exam_id");
                    row[1] = resultSet.getString("exam_name");
                    row[2] = resultSet.getBigDecimal("cost");
                    row[3] = resultSet.getString("addr");
                    row[4] = resultSet.getString("remark");
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database query error: " + ex.getMessage());
        }
    }

}
