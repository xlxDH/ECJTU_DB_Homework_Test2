package HospitalDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class SickbedManagement extends JFrame {
    private JTable sickbedTable;

    public void launch() {
        setSize(800, 600);
        setTitle("病床管理");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public SickbedManagement() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("病床编号");
        columnNames.add("病房编号");
        columnNames.add("状态");

        Vector<Vector<Object>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        sickbedTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(sickbedTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("添加病床");
        panel.add(addButton, BorderLayout.SOUTH);
        add(panel);

        refreshSickbedList();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField bedIdField = new JTextField(5);
                JTextField roomIdField = new JTextField(5);
                JTextField stateField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("病床编号:"));
                myPanel.add(bedIdField);
                myPanel.add(new JLabel("病房编号:"));
                myPanel.add(roomIdField);
                myPanel.add(new JLabel("状态:"));
                myPanel.add(stateField);

                int result = JOptionPane.showConfirmDialog(null, myPanel, "请输入病床信息", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    int bedId = Integer.parseInt(bedIdField.getText());
                    int roomId = Integer.parseInt(roomIdField.getText());
                    String state = stateField.getText();

                    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://124.220.162.220:8000/medical", "gaussdb", "Enmo@123")) {
                        String sql = "INSERT INTO medi_schema.tb_sickbed (bed_id, room_id, state) VALUES (?, ?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setInt(1, bedId);
                            statement.setInt(2, roomId);
                            statement.setString(3, state);
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(null, "病床添加成功");
                                refreshSickbedList();
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

    private void refreshSickbedList() {
        DefaultTableModel model = (DefaultTableModel) sickbedTable.getModel();
        model.setRowCount(0);

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://124.220.162.220:8000/medical", "gaussdb", "Enmo@123")) {
            String sql = "SELECT bed_id, room_id, state FROM medi_schema.tb_sickbed";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Object[] row = new Object[3];
                    row[0] = resultSet.getInt("bed_id");
                    row[1] = resultSet.getInt("room_id");
                    row[2] = resultSet.getString("state");
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

}
