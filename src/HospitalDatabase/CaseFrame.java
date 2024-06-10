package HospitalDatabase;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CaseFrame extends JFrame{

    private JTextField jtextfield4 = new JTextField();
    private JTextField jtextfield5 = new JTextField();
    private JTextField jtextfield6 = new JTextField();
    private JTextField jtextfield7 = new JTextField();
    private JTextField jtextfield8 = new JTextField();
    private JTextField jtextfield9 = new JTextField();
    private JTextField jtextfield10 = new JTextField();
    private JTextField jtextfield11 = new JTextField();
    private JTextField jtextfield12 = new JTextField();
    private JTextField jtextfield13 = new JTextField();
    private JTextField jtextfield14 = new JTextField();




    public CaseFrame() {
        JPanel jpanel = new JPanel(new GridLayout(14, 1));
        setSize(800, 800);

        Font font = new Font("宋体", Font.PLAIN, 20);

        jtextfield4 = setupLabelAndTextField(jpanel, "挂号单 i d:", font);
        jtextfield5 = setupLabelAndTextField(jpanel, "病 情 :", font);
        jtextfield6 = setupLabelAndTextField(jpanel, "主 诉 :", font);
        jtextfield7 = setupLabelAndTextField(jpanel, "现 病 史:", font);
        jtextfield8 = setupLabelAndTextField(jpanel, "既往病史:", font);
        jtextfield9 = setupLabelAndTextField(jpanel, "过敏史:", font);
        jtextfield10 = setupLabelAndTextField(jpanel, "个人史:", font);
        jtextfield11 = setupLabelAndTextField(jpanel, "家庭史:", font);
        jtextfield12 = setupLabelAndTextField(jpanel, "体 检 :", font);
        jtextfield13 = setupLabelAndTextField(jpanel, "辅助检查:", font);
        jtextfield14 = setupLabelAndTextField(jpanel, "治疗建议:", font);

        JButton button = new JButton("提交");
        button.setFont(new Font("宋体", Font.PLAIN, 20));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("点击成功");
                insertData();
            }
        });

        setLayout(new BorderLayout());
        add(jpanel, BorderLayout.CENTER);  // 将标签和文本框部分放置在中间
        add(button, BorderLayout.SOUTH);   // 将提交按钮放置在底部中间
    }

    private JTextField setupLabelAndTextField(JPanel panel, String labelText, Font font) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        panel.add(label);
        JTextField textField = new JTextField(20);
        textField.setFont(font);
        panel.add(textField);
        return textField; // 返回创建的 JTextField
    }

    public void insertData() {

        String reg_id = jtextfield4.getText();
        String condition = jtextfield5.getText();
        String chief_complaint = jtextfield6.getText();
        String present_med_history = jtextfield7.getText();
        String past_med_history = jtextfield8.getText();
        String allergy = jtextfield9.getText();
        String personal_history = jtextfield10.getText();
        String family_history = jtextfield11.getText();
        String examination = jtextfield12.getText();
        String supplementary = jtextfield13.getText();
        String suggest = jtextfield14.getText();
        String med_id = "";

        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection c = DriverManager.getConnection("jdbc:postgresql://124.220.162.220:8000/medical", "gaussdb", "Enmo@123")) {
            String query = "SELECT med_id FROM medi_schema.tb_register WHERE reg_id = ?"; // 修改查询表名
            try (PreparedStatement stmt = c.prepareStatement(query)) {
                stmt.setString(1, reg_id); // 设置参数
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        med_id = rs.getString("med_id");
                    } else {
                        JOptionPane.showMessageDialog(null, "单号不存在");
                        return; // 在此处返回避免执行后续代码
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // 打印异常信息
            JOptionPane.showMessageDialog(null, "查询单号信息时发生错误: " + ex.getMessage()); // 显示错误信息给用户
        }


        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO medi_schema.tb_case (med_id, reg_id, condition, chief_complaint, present_med_history, past_med_history, allergy, personal_history, family_history, examination, supplementary, suggest, creat_time)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, med_id);
                statement.setString(2, reg_id);
                statement.setString(3, condition);
                statement.setString(4, chief_complaint);
                statement.setString(5, present_med_history);
                statement.setString(6, past_med_history);
                statement.setString(7, allergy);
                statement.setString(8, personal_history);
                statement.setString(9, family_history);
                statement.setString(10, examination);
                statement.setString(11, supplementary);
                statement.setString(12, suggest);
                statement.setDate(13, new java.sql.Date(System.currentTimeMillis()));
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null,"填写完成");
                    dispose();
                    //new Management();
                } else {
                    JOptionPane.showMessageDialog(null,"填写失败");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void lauch() {
        setSize(400,600);
        setResizable(false);
        setTitle("病例填写");
        setVisible(true);
        setLocationRelativeTo(null);
    }


}
