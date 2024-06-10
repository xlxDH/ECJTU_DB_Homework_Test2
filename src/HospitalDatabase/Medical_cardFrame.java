package HospitalDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;

public class Medical_cardFrame extends JFrame{

    private JTextField jtextfield1;
    private JTextField jtextfield2;
    private JTextField jtextfield3;
    private JTextField jtextfield4;
    private JTextField jtextfield5;
    private JTextField jtextfield6;
    private JTextField jtextfield7;
    private JTextField jtextfield8;
    private JTextField jtextfield9;
    private JTextField jtextfield10;
    private JTextField jtextfield11;
    private JTextField jtextfield12;
    private JTextField jtextfield13;

    public void lauch() {
        setSize(800, 800);
        setResizable(false);
        setTitle("医疗卡办理");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public Medical_cardFrame() {

        JPanel jpanel = new JPanel();
        jpanel.setLayout(null);
        JLabel textLable1 = new JLabel("医疗卡号: ");
        textLable1.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable1.setBounds(280, 50, 200, 20);
        jpanel.add(textLable1);
        jtextfield1 = new JTextField(20);
        jtextfield1.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield1.setBounds(380, 50, 200, 20);
        jpanel.add(jtextfield1);

        JLabel textLable2 = new JLabel(" 姓 名 :");
        textLable2.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable2.setBounds(280, 100, 200, 20);
        jpanel.add(textLable2);
        jtextfield2 = new JTextField(20);
        jtextfield2.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield2.setBounds(380, 100, 200, 20);
        jpanel.add(jtextfield2);

        JLabel textLable3 = new JLabel(" 性 别 :");
        textLable3.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable3.setBounds(280, 150, 200, 20);
        jpanel.add(textLable3);
        jtextfield3 = new JTextField(20);
        jtextfield3.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield3.setBounds(380, 150, 200, 20);
        jpanel.add(jtextfield3);

        JLabel textLable4 = new JLabel(" 年 龄 :");
        textLable4.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable4.setBounds(280, 200, 200, 20);
        jpanel.add(textLable4);
        jtextfield4 = new JTextField(20);
        jtextfield4.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield4.setBounds(380, 200, 200, 20);
        jpanel.add(jtextfield4);

        JLabel textLable5 = new JLabel(" 生 日 :");
        textLable5.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable5.setBounds(280, 250, 200, 20);
        jpanel.add(textLable5);
        jtextfield5 = new JTextField(20);
        jtextfield5.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield5.setBounds(380, 250, 200, 20);
        jpanel.add(jtextfield5);

        JLabel textLable6 = new JLabel(" 住 址 :");
        textLable6.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable6.setBounds(280, 300, 200, 20);
        jpanel.add(textLable6);
        jtextfield6 = new JTextField(20);
        jtextfield6.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield6.setBounds(380, 300, 200, 20);
        jpanel.add(jtextfield6);

        JLabel textLable7 = new JLabel(" 电 话 :");
        textLable7.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable7.setBounds(280, 350, 200, 20);
        jpanel.add(textLable7);
        jtextfield7 = new JTextField(20);
        jtextfield7.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield7.setBounds(380, 350, 200, 20);
        jpanel.add(jtextfield7);

        JLabel textLable8 = new JLabel(" 民 族:");
        textLable8.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable8.setBounds(280, 400, 200, 20);
        jpanel.add(textLable8);
        jtextfield8 = new JTextField(20);
        jtextfield8.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield8.setBounds(380, 400, 200, 20);
        jpanel.add(jtextfield8);

        JLabel textLable9 = new JLabel(" 文化水平:");
        textLable9.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable9.setBounds(280, 450, 200, 20);
        jpanel.add(textLable9);
        jtextfield9 = new JTextField(20);
        jtextfield9.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield9.setBounds(380, 450, 200, 20);
        jpanel.add(jtextfield9);

        JLabel textLable10 = new JLabel("婚姻状况:");
        textLable10.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable10.setBounds(280, 500, 200, 20);
        jpanel.add(textLable10);
        jtextfield10 = new JTextField(20);
        jtextfield10.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield10.setBounds(380, 500, 200, 20);
        jpanel.add(jtextfield10);

        JLabel textLable11 = new JLabel(" 工 作 ");
        textLable11.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable11.setBounds(280, 550, 200, 20);
        jpanel.add(textLable11);
        jtextfield11 = new JTextField(20);
        jtextfield11.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield11.setBounds(380, 550, 200, 20);
        jpanel.add(jtextfield11);

        JLabel textLable12 = new JLabel(" 邮 编 :");
        textLable12.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable12.setBounds(280, 600, 200, 20);
        jpanel.add(textLable12);
        jtextfield12 = new JTextField(20);
        jtextfield12.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield12.setBounds(380, 600, 200, 20);
        jpanel.add(jtextfield12);

        JLabel textLable13 = new JLabel("身份证号:");
        textLable13.setFont(new Font("宋体", Font.PLAIN, 20));
        textLable13.setBounds(280, 650, 200, 20);
        jpanel.add(textLable13);
        jtextfield13 = new JTextField(20);
        jtextfield13.setFont(new Font("宋体", Font.PLAIN, 20));
        jtextfield13.setBounds(380, 650, 200, 20);
        jpanel.add(jtextfield13);
        JButton registrationButton = new JButton(" 办 理 ");
        registrationButton.setFont(new Font("宋体", Font.PLAIN, 20));
        registrationButton.setBounds(380, 700, 200, 40);
        registrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("点击成功");
                insertData();
            }
        });
        jpanel.add(registrationButton, BorderLayout.EAST);
        setContentPane(jpanel);

    }
    private void insertData() {
        String med_id = jtextfield1.getText();
        String name = jtextfield2.getText();
        String sex = jtextfield3.getText();
        String age = jtextfield4.getText();
        String birthday = jtextfield5.getText();
        String address = jtextfield6.getText();
        String phone = jtextfield7.getText();
        String nation = jtextfield8.getText();
        String cultrue = jtextfield9.getText();
        String marriage = jtextfield10.getText();
        String work = jtextfield11.getText();
        String postcode = jtextfield12.getText();
        String id_card = jtextfield13.getText();

        String url = "jdbc:postgresql://124.220.162.220:8000/medical";
        String user = "gaussdb";
        String password = "Enmo@123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO medi_schema.tb_medical_card (med_id, name, sex, age, birthday, address, phone, nation, cultrue, marriage, work, postcode, id_card) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, med_id);
                statement.setString(2, name);
                statement.setString(3, sex);
                statement.setString(4, age);
                statement.setString(5, birthday);
                statement.setString(6, address);
                statement.setString(7, phone);
                statement.setString(8, nation);
                statement.setString(9, cultrue);
                statement.setString(10, marriage);
                statement.setString(11, work);
                statement.setString(12, postcode);
                statement.setString(13, id_card);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null,"办理完成");
                    dispose();
                    new Management();
                } else {
                    JOptionPane.showMessageDialog(null,"办理失败");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex.getMessage());
        }
    }

}