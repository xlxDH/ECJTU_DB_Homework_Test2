package HospitalDatabase;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PrescriptionFrame extends JFrame{
    private JTextField jtextfield1;
    private JTextField jtextfield2;
    private JTextField jtextfield3;
    private JTextField jtextfield4;
    private JTextField jtextfield5;
//    private JTextField jtextfield6;
    private JTextField jtextfield7;
    private JTextField jtextfield8;
    private JButton button;

    public PrescriptionFrame() {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(null);



        JLabel label2 = new JLabel("挂号单id:");
        label2.setFont(new Font("宋体", Font.PLAIN, 20));
        label2.setBounds(80, 100, 100, 20);
        jtextfield2 = new JTextField(20);
        jtextfield2.setFont(new Font("宋体", Font.PLAIN, 15));
        jtextfield2.setBounds(180, 100, 120, 20);
        jpanel.add(label2);
        jpanel.add(jtextfield2);

        JLabel label4 = new JLabel("药品id:");
        label4.setFont(new Font("宋体", Font.PLAIN, 20));
        label4.setBounds(80, 150, 100, 20);
        jtextfield4 = new JTextField(20);
        jtextfield4.setFont(new Font("宋体", Font.PLAIN, 15));
        jtextfield4.setBounds(180, 150, 120, 20);
        jpanel.add(label4);
        jpanel.add(jtextfield4);

        JLabel label5 = new JLabel("数量:");
        label5.setFont(new Font("宋体", Font.PLAIN, 20));
        label5.setBounds(80, 200, 100, 20);
        jtextfield5 = new JTextField(20);
        jtextfield5.setFont(new Font("宋体", Font.PLAIN, 15));
        jtextfield5.setBounds(180, 200, 120, 20);
        jpanel.add(label5);
        jpanel.add(jtextfield5);

//        JLabel label6 = new JLabel("状态:");
//        label6.setFont(new Font("宋体", Font.PLAIN, 20));
//        label6.setBounds(80, 250, 100, 20);
//        jtextfield6 = new JTextField(20);
//        jtextfield6.setFont(new Font("宋体", Font.PLAIN, 15));
//        jtextfield6.setBounds(180, 250, 120, 20);
//        jpanel.add(label6);
//        jpanel.add(jtextfield6);

        JLabel label7 = new JLabel("医嘱:");
        label7.setFont(new Font("宋体", Font.PLAIN, 20));
        label7.setBounds(80, 250, 100, 20);
        jtextfield7 = new JTextField(20);
        jtextfield7.setFont(new Font("宋体", Font.PLAIN, 15));
        jtextfield7.setBounds(180, 250, 120, 20);
        jpanel.add(label7);
        jpanel.add(jtextfield7);

        JLabel label8 = new JLabel("用法用量:");
        label8.setFont(new Font("宋体", Font.PLAIN, 20));
        label8.setBounds(80, 300, 100, 20);
        jtextfield8 = new JTextField(20);
        jtextfield8.setFont(new Font("宋体", Font.PLAIN, 15));
        jtextfield8.setBounds(180, 300, 120, 20);
        jpanel.add(label8);
        jpanel.add(jtextfield8);
        button = new JButton("开药方");
        button.setFont(new Font("宋体", Font.PLAIN, 16));
        button.setBounds(200, 350, 100, 30);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("点击成功");
                insertData();
            }
        });
        jpanel.add(button);
        setContentPane(jpanel);
    }

        public void insertData() {
            String regId = jtextfield2.getText();
            String drugId = jtextfield4.getText();
            String count = jtextfield5.getText();
            String state = "未缴费";
            String medicalAdvice = jtextfield7.getText();
            String usage = jtextfield8.getText();
            String medId = "";

            String url = "jdbc:postgresql://124.220.162.220:8000/medical";
            String user = "gaussdb";
            String password = "Enmo@123";

            try (Connection c = DriverManager.getConnection(url, user, password)) {
                String query = "SELECT med_id FROM medi_schema.tb_register WHERE reg_id = ?";
                try (PreparedStatement stmt = c.prepareStatement(query)) {
                    stmt.setString(1, regId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            medId = rs.getString("med_id");
                        } else {
                            JOptionPane.showMessageDialog(null, "单号不存在");
                            return;
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "查询单号信息时发生错误: " + ex.getMessage());
            }

            String callProcedureSQL = "{call insert_prescription_and_create_outpatient_charges(?, ?, ?, ?, ?)}";
            try (Connection connection = DriverManager.getConnection(url, user, password);
                 CallableStatement callableStatement = connection.prepareCall(callProcedureSQL)) {
                callableStatement.setInt(1, Integer.parseInt(regId));
                callableStatement.setInt(2, Integer.parseInt(medId));
                callableStatement.setInt(3, Integer.parseInt(drugId));
                callableStatement.setInt(4, Integer.parseInt(count));
                callableStatement.setString(5, state);
                callableStatement.execute();

                // 更新医嘱和用法用量
                String updateSQL = "UPDATE medi_schema.tb_prescription SET medical_advice = ?, usage = ? WHERE reg_id = ? AND drug_id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                    updateStatement.setString(1, medicalAdvice);
                    updateStatement.setString(2, usage);
                    updateStatement.setInt(3, Integer.parseInt(regId));
                    updateStatement.setInt(4, Integer.parseInt(drugId));
                    int rowsUpdated = updateStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "开药方完成");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "开药方失败，更新医嘱和用法用量失败");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "开药方失败： " + ex.getMessage());
            }
        }


    public void lauch() {
        setSize(500,600);
        setResizable(false);
        setTitle("处方填写");
        setVisible(true);
        setLocationRelativeTo(null);
    }

}
