package HospitalDatabase;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Select_diseaseFrame extends JFrame{
    private JTextField jtextfield1;
    private JTextField jtextfield2;
    private JTextField jtextfield3;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    public Select_diseaseFrame() {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(null);
        //查询相关疾病
        JLabel label1 = new JLabel("疾病名:");
        label1.setFont(new Font("宋体", Font.PLAIN, 20));
        label1.setBounds(80, 50, 100, 20);;
        jtextfield1 = new JTextField(30);
        jtextfield1.setFont(new Font("宋体", Font.PLAIN, 15));
        jtextfield1.setBounds(180, 50, 120, 20);
        jpanel.add(label1);
        jpanel.add(jtextfield1);

        button1 = new JButton("查询");
        button1.setFont(new Font("宋体", Font.PLAIN, 12));
        button1.setBounds(330, 50, 80, 25);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                select_disease_name();
            }
        });
        jpanel.add(button1);

        //查询患者既往病历
        JLabel label2 = new JLabel("医疗卡id:");
        label2.setFont(new Font("宋体", Font.PLAIN, 20));
        label2.setBounds(80, 150, 100, 20);
        jtextfield2 = new JTextField(20);
        jtextfield2.setFont(new Font("宋体", Font.PLAIN, 15));
        jtextfield2.setBounds(180, 150, 120, 20);
        jpanel.add(label2);
        jpanel.add(jtextfield2);

        button2 = new JButton("查询");
        button2.setFont(new Font("宋体", Font.PLAIN, 12));
        button2.setBounds(330, 150, 80, 25);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                select_med_id();
            }
        });
        jpanel.add(button2);

        //查询相关药物
        JLabel label3 = new JLabel("药品名:");
        label3.setFont(new Font("宋体", Font.PLAIN, 20));
        label3.setBounds(80, 250, 100, 20);
        jtextfield3 = new JTextField(20);
        jtextfield3.setFont(new Font("宋体", Font.PLAIN, 15));
        jtextfield3.setBounds(180, 250, 120, 20);
        jpanel.add(label3);
        jpanel.add(jtextfield3);
        button3 = new JButton("查询");
        button3.setFont(new Font("宋体", Font.PLAIN, 12));
        button3.setBounds(330, 250, 80, 25);
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                select_drug();
            }
        });
        jpanel.add(button3);

        setContentPane(jpanel);
    }
    public void  select_disease_name() {
        String name = jtextfield1.getText();
        String disease_name = "";
        String dept_id = "";
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection c = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM medi_schema.tb_disease WHERE disease_name LIKE ?";
            try (PreparedStatement stmt = c.prepareStatement(query)) {
                stmt.setString(1, "%" + name + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    boolean found = false;
                    StringBuilder resultStringBuilder = new StringBuilder();
                    while (rs.next()) {
                        found = true;
                        disease_name = rs.getString("disease_name");
                        dept_id = rs.getString("dept_id");
                        resultStringBuilder.append("疾病名称:").append(disease_name).append("\t").append("科室id：").append(dept_id).append("\n");
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(null, "暂无这类疾病");
                        return;
                    }
                    JFrame resultFrame = new JFrame("查询结果");
                    JTextArea resultTextArea = new JTextArea(resultStringBuilder.toString());
                    resultTextArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(resultTextArea);
                    resultFrame.add(scrollPane);
                    resultFrame.setSize(400, 300);
                    resultFrame.setLocationRelativeTo(null);
                    resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    resultFrame.setVisible(true);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询疾病信息时发生错误: " + ex.getMessage());
        }
    }

    public void  select_med_id() {
        String med_id = jtextfield2.getText();
        String condition = "";
        String chief_complaint = "";
        String present_med_history = "";
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection c = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM medi_schema.tb_case WHERE med_id = ?";
            try (PreparedStatement stmt = c.prepareStatement(query)) {
                stmt.setString(1,med_id);
                try (ResultSet rs = stmt.executeQuery()) {
                    boolean found = false;
                    StringBuilder resultStringBuilder = new StringBuilder();
                    while (rs.next()) {
                        found = true;
                        condition = rs.getString("condition");
                        chief_complaint = rs.getString("chief_complaint");
                        present_med_history = rs.getString("present_med_history");
                        resultStringBuilder.append("现状:").append(condition).append("\n").append("主诉:").append(chief_complaint).append("\n").append("既往病史:").append(present_med_history).append("\n");
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(null, "暂无该用户");
                        return;
                    }
                    JFrame resultFrame = new JFrame("查询结果");
                    JTextArea resultTextArea = new JTextArea(resultStringBuilder.toString());
                    resultTextArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(resultTextArea);
                    resultFrame.add(scrollPane);
                    resultFrame.setSize(400, 300);
                    resultFrame.setLocationRelativeTo(null);
                    resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    resultFrame.setVisible(true);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询疾病信息时发生错误: " + ex.getMessage());
        }
    }
    public void select_drug() {
        String name = jtextfield3.getText();
        String drug_name = "";
        String drug_id = "";
        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection c = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM medi_schema.tb_drug WHERE drug_name LIKE ?";
            try (PreparedStatement stmt = c.prepareStatement(query)) {
                stmt.setString(1, "%" + name + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    boolean found = false;
                    StringBuilder resultStringBuilder = new StringBuilder();
                    while (rs.next()) {
                        found = true;
                        drug_id = rs.getString("drug_id");
                        drug_name = rs.getString("drug_name");
                        resultStringBuilder.append("药品id:").append(drug_id).append("\t").append("药品名称:").append(drug_name).append("\n");
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(null, "暂无这类药品");
                        return;
                    }
                    JFrame resultFrame = new JFrame("查询结果");
                    JTextArea resultTextArea = new JTextArea(resultStringBuilder.toString());
                    resultTextArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(resultTextArea);
                    resultFrame.add(scrollPane);
                    resultFrame.setSize(400, 300);
                    resultFrame.setLocationRelativeTo(null);
                    resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    resultFrame.setVisible(true);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询疾病信息时发生错误: " + ex.getMessage());
        }
    }

    public void lauch() {
        setSize(500,400);
        setResizable(false);
        setTitle("查询窗口");
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Select_diseaseFrame select_diseaseframe = new Select_diseaseFrame();
            select_diseaseframe.lauch();
        });
    }
}
