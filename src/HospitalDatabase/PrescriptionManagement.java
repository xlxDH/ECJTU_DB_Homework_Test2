package HospitalDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class PrescriptionManagement extends JFrame {
    private JTable prescriptionTable;

    public void launch() {
        setSize(800, 600);
        setTitle("药方查看");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public PrescriptionManagement() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("药方编号");
        columnNames.add("挂号编号");
        columnNames.add("医保卡编号");
        columnNames.add("药品编号");
        columnNames.add("数量");
        columnNames.add("状态");
        columnNames.add("医嘱");
        columnNames.add("用法");

        Vector<Vector<Object>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        prescriptionTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(prescriptionTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        refreshPrescriptionList();
    }

    private void refreshPrescriptionList() {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM medi_schema.tb_prescription";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(resultSet.getInt("pre_id"));
                    row.add(resultSet.getInt("reg_id"));
                    row.add(resultSet.getInt("med_id"));
                    row.add(resultSet.getInt("drug_id"));
                    row.add(resultSet.getInt("count"));
                    row.add(resultSet.getString("state"));
                    row.add(resultSet.getString("medical_advice"));
                    row.add(resultSet.getString("usage"));
                    ((DefaultTableModel) prescriptionTable.getModel()).addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }


}
