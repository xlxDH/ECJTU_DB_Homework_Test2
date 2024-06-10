package HospitalDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class DiseaseManagement extends JFrame {
    private JTable diseaseTable;

    public void launch() {
        setSize(800, 600);
        setTitle("疾病管理");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public DiseaseManagement() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("疾病编号");
        columnNames.add("科室编号");
        columnNames.add("科室名称");
        columnNames.add("疾病名称");

        Vector<Vector<Object>> data = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        diseaseTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(diseaseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("添加疾病");
        panel.add(addButton, BorderLayout.SOUTH);
        add(panel);

        refreshDiseaseList();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField deptNameField = new JTextField(10);
                JTextField diseaseNameField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("科室名称:"));
                myPanel.add(deptNameField);
                myPanel.add(new JLabel("疾病名称:"));
                myPanel.add(diseaseNameField);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "请输入疾病信息", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String deptName = deptNameField.getText();
                    String diseaseName = diseaseNameField.getText();
                    int deptId = 0;

                    // 查询科室id
                    String url = "jdbc:postgresql://124.220.162.220:8000/medical";
                    String user = "gaussdb";
                    String password = "Enmo@123";

                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        String query = "SELECT dept_id FROM medi_schema.tb_department WHERE dept_name = ?";
                        try (PreparedStatement stmt = connection.prepareStatement(query)) {
                            stmt.setString(1, deptName);
                            try (ResultSet rs = stmt.executeQuery()) {
                                if (rs.next()) {
                                    deptId = rs.getInt("dept_id");
                                } else {
                                    JOptionPane.showMessageDialog(null, "未找到该科室");
                                    return;
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "查询科室信息时发生错误: " + ex.getMessage());
                    }

                    // 插入新疾病信息
                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        String sql = "INSERT INTO medi_schema.tb_disease (dept_id, disease_name) VALUES (?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setInt(1, deptId);
                            statement.setString(2, diseaseName);
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(null, "新疾病添加成功");

                                // 添加成功后刷新疾病列表
                                refreshDiseaseList();
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

    private void refreshDiseaseList() {
        DefaultTableModel model = (DefaultTableModel) diseaseTable.getModel();
        model.setRowCount(0); // 清空表格数据

        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT d.disease_id, d.dept_id, de.dept_name, d.disease_name " +
                    "FROM medi_schema.tb_disease d " +
                    "JOIN medi_schema.tb_department de " +
                    "ON d.dept_id = de.dept_id";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet =  statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Object[] row = new Object[4];
                    row[0] = resultSet.getInt("disease_id");
                    row[1] = resultSet.getInt("dept_id");
                    row[2] = resultSet.getString("dept_name");
                    row[3] = resultSet.getString("disease_name");
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

}