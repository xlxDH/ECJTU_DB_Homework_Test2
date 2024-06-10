package HospitalDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

public class SickroomManagement extends JFrame {
    private JTable sickroomTable;

    public void launch() {
        setSize(800, 600);
        setTitle("病房管理");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public SickroomManagement() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("病房编号");
        columnNames.add("科室编号");
        columnNames.add("类型");
        columnNames.add("价格");

        Vector<Vector<Object>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        sickroomTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(sickroomTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("添加病房");
        panel.add(addButton, BorderLayout.SOUTH);
        add(panel);

        refreshSickroomList();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField roomIdField = new JTextField(5);
                JTextField deptIdField = new JTextField(5);
                JTextField typeField = new JTextField(10);
                JTextField priceField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("病房编号:"));
                myPanel.add(roomIdField);
                myPanel.add(new JLabel("科室编号:"));
                myPanel.add(deptIdField);
                myPanel.add(new JLabel("类型:"));
                myPanel.add(typeField);
                myPanel.add(new JLabel("价格:"));
                myPanel.add(priceField);

                int result = JOptionPane.showConfirmDialog(null, myPanel, "请输入病房信息", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    int roomId = Integer.parseInt(roomIdField.getText());
                    int deptId = Integer.parseInt(deptIdField.getText());
                    String type = typeField.getText();
                    BigDecimal price = new BigDecimal(priceField.getText());

                    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://124.220.162.220:8000/medical", "gaussdb", "Enmo@123")) {
                        String sql = "INSERT INTO medi_schema.tb_sickroom (room_id, dept_id, type, price) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setInt(1, roomId);
                            statement.setInt(2, deptId);
                            statement.setString(3, type);
                            statement.setBigDecimal(4, price);
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(null, "病房添加成功");
                                refreshSickroomList();
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

    private void refreshSickroomList() {
        DefaultTableModel model = (DefaultTableModel) sickroomTable.getModel();
        model.setRowCount(0);

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://124.220.162.220:8000/medical", "gaussdb", "Enmo@123")) {
            String sql = "SELECT room_id, dept_id, type, price FROM medi_schema.tb_sickroom";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Object[] row = new Object[4];
                    row[0] = resultSet.getInt("room_id");
                    row[1] = resultSet.getInt("dept_id");
                    row[2] = resultSet.getString("type");
                    row[3] = resultSet.getBigDecimal("price");
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

}
