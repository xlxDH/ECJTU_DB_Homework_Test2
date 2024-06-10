package HospitalDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class DrugManagement extends JFrame {
    private JTable drugTable;

    public void launch() {
        setSize(800, 600);
        setTitle("药品管理");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public DrugManagement() {
        JPanel panel = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("药品编号");
        columnNames.add("药品名称");
        columnNames.add("药品类型");
        columnNames.add("成本名称");
        columnNames.add("规格");
        columnNames.add("剂型");
        columnNames.add("价格");
        columnNames.add("库存");
        columnNames.add("最大库存");
        columnNames.add("最小库存");
        columnNames.add("生产日期");
        columnNames.add("有效日期");
        columnNames.add("功效");

        Vector<Vector<Object>> data = new Vector<>();
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        drugTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(drugTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("添加药品");
        panel.add(addButton, BorderLayout.SOUTH);
        add(panel);

        refreshDrugList();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField drugNameField = new JTextField(10);
                JTextField drugTypeField = new JTextField(10);
                JTextField costNameField = new JTextField(10);
                JTextField specField = new JTextField(10);
                JTextField dosageFormField = new JTextField(10);
                JTextField priceField = new JTextField(10);
                JTextField stockField = new JTextField(10);
                JTextField stockMaxField = new JTextField(10);
                JTextField stockMinField = new JTextField(10);
                JTextField productDateField = new JTextField(10);
                JTextField effectDateField = new JTextField(10);
                JTextField efficacyField = new JTextField(10);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("药品名称:"));
                myPanel.add(drugNameField);
                myPanel.add(new JLabel("药品类型:"));
                myPanel.add(drugTypeField);
                myPanel.add(new JLabel("成本名称:"));
                myPanel.add(costNameField);
                myPanel.add(new JLabel("规格:"));
                myPanel.add(specField);
                myPanel.add(new JLabel("剂型:"));
                myPanel.add(dosageFormField);

                myPanel.add(new JLabel("价格:"));
                myPanel.add(priceField);
                myPanel.add(new JLabel("库存:"));
                myPanel.add(stockField);
                myPanel.add(new JLabel("最大库存:"));
                myPanel.add(stockMaxField);
                myPanel.add(new JLabel("最小库存:"));
                myPanel.add(stockMinField);
                myPanel.add(new JLabel("生产日期(yyyy-MM-dd):"));
                myPanel.add(productDateField);
                myPanel.add(new JLabel("有效日期(yyyy-MM-dd):"));
                myPanel.add(effectDateField);
                myPanel.add(new JLabel("功效:"));
                myPanel.add(efficacyField);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "请输入新药品的信息", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String drugName = drugNameField.getText();
                    String drugType = drugTypeField.getText();
                    String costName = costNameField.getText();
                    String spec = specField.getText();
                    String dosageForm = dosageFormField.getText();
                    BigDecimal price = new BigDecimal(priceField.getText());
                    int stock = Integer.parseInt(stockField.getText());
                    int stockMax = Integer.parseInt(stockMaxField.getText());
                    int stockMin = Integer.parseInt(stockMinField.getText());
                    Date productDate = null;
                    Date effectDate = null;
                    try {
                        productDate = new SimpleDateFormat("yyyy-MM-dd").parse(productDateField.getText());
                        effectDate = new SimpleDateFormat("yyyy-MM-dd").parse(effectDateField.getText());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "日期格式错误");
                    }
                    String efficacy = efficacyField.getText();

                    // 将新药品的信息插入数据库中
                    String url = "jdbc:postgresql://124.220.162.220:8000/medical";
                    String user = "gaussdb";
                    String password = "Enmo@123";

                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        String sql = "INSERT INTO medi_schema.tb_drug (drug_name, drug_type, cost_name, spec, dosage_form, price, stock, stock_max, stock_min, product_date, effect_date, efficay) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, drugName);
                            statement.setString(2, drugType);
                            statement.setString(3, costName);
                            statement.setString(4, spec);
                            statement.setString(5, dosageForm);
                            statement.setBigDecimal(6, price);
                            statement.setInt(7, stock);
                            statement.setInt(8, stockMax);
                            statement.setInt(9, stockMin);
                            statement.setDate(10, new java.sql.Date(productDate.getTime()));
                            statement.setDate(11, new java.sql.Date(effectDate.getTime()));
                            statement.setString(12, efficacy);
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(null, "新药品添加成功");
                                refreshDrugList();
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

    private void refreshDrugList() {
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM medi_schema.tb_drug";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(resultSet.getInt("drug_id"));
                    row.add(resultSet.getString("drug_name"));
                    row.add(resultSet.getString("drug_type"));
                    row.add(resultSet.getString("cost_name"));
                    row.add(resultSet.getString("spec"));
                    row.add(resultSet.getString("dosage_form"));
                    row.add(resultSet.getBigDecimal("price"));
                    row.add(resultSet.getInt("stock"));
                    row.add(resultSet.getInt("stock_max"));
                    row.add(resultSet.getInt("stock_min"));
                    row.add(resultSet.getDate("product_date"));
                    row.add(resultSet.getDate("effect_date"));
                    row.add(resultSet.getString("efficay"));
                    ((DefaultTableModel) drugTable.getModel()).addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库查询出错: " + ex.getMessage());
        }
    }

}
