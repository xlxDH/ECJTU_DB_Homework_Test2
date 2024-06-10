package HospitalDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

public class DepartmentManagement extends JFrame {
    private JTable departmentTable;

    public void launch() {
        setSize(800, 600);
        setTitle("科室管理");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public DepartmentManagement() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("科室编号");
        columnNames.add("科室名称");
        columnNames.add("地址");
        columnNames.add("价格");
        columnNames.add("备注");

        Vector<Vector<Object>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        departmentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(departmentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("添加科室");
        panel.add(addButton, BorderLayout.SOUTH);

        add(panel);

        refreshDepartmentList();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField deptNameField = new JTextField(10);
                JTextField addrField = new JTextField(10);
                JTextField priceField = new JTextField(10);
                JTextField remarkField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("科室名称:"));
                myPanel.add(deptNameField);
                myPanel.add(new JLabel("地址:"));
                myPanel.add(addrField);
                myPanel.add(new JLabel("价格:"));
                myPanel.add(priceField);
                myPanel.add(new JLabel("备注:"));
                myPanel.add(remarkField);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "请输入新科室的信息", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String deptName = deptNameField.getText();
                    String addr = addrField.getText();
                    BigDecimal price = new BigDecimal(priceField.getText());
                    String remark = remarkField.getText();

                    // 将新科室的信息插入数据库中
                    String url = "jdbc:postgresql://124.220.162.220:8000/medical";
                    String user = "gaussdb";
                    String password = "Enmo@123";

                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        String sql = "INSERT INTO medi_schema.tb_department (dept_name, addr, price, remark) " +
                                "VALUES (?, ?, ?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, deptName);
                            statement.setString(2, addr);
                            statement.setBigDecimal(3, price);
                            statement.setString(4, remark);

                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(null, "新科室添加成功");
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

    private void refreshDepartmentList() {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM medi_schema.tb_department";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(resultSet.getInt("dept_id"));
                    row.add(resultSet.getString("dept_name"));
                    row.add(resultSet.getString("addr"));
                    row.add(resultSet.getBigDecimal("price"));
                    row.add(resultSet.getString("remark"));
                    ((DefaultTableModel) departmentTable.getModel()).addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

}
