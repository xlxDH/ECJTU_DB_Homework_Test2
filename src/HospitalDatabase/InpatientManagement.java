package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InpatientManagement extends JFrame {
    public InpatientManagement() {
        setTitle("住院管理系统");
        setSize(1000, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel management = new JPanel();
        management.setLayout(null);

        JButton admissionRegister = new JButton("住院登记");
        admissionRegister.setFont(new Font("宋体", Font.PLAIN, 16));
        admissionRegister.setBounds(250, 150, 150, 40);

        JButton bedArrangement = new JButton("床位安排");
        bedArrangement.setFont(new Font("宋体", Font.PLAIN, 16));
        bedArrangement.setBounds(250, 250, 150, 40);

        JButton wardManagement = new JButton("病房管理");
        wardManagement.setFont(new Font("宋体", Font.PLAIN, 16));
        wardManagement.setBounds(250, 350, 150, 40);

        JButton patientView = new JButton("病人查看");
        patientView.setFont(new Font("宋体", Font.PLAIN, 16));
        patientView.setBounds(250, 450, 150, 40);

        JButton expenseAccounting = new JButton("费用记账");
        expenseAccounting.setFont(new Font("宋体", Font.PLAIN, 16));
        expenseAccounting.setBounds(650, 350, 150, 40);

//        JButton expenseReminder = new JButton("费用提醒");
//        expenseReminder.setFont(new Font("宋体", Font.PLAIN, 16));
//        expenseReminder.setBounds(650, 450, 150, 40);

        JButton expenseView = new JButton("费用查看");
        expenseView.setFont(new Font("宋体", Font.PLAIN, 16));
        expenseView.setBounds(650, 450, 150, 40);

        JButton dischargeHandling = new JButton("出院办理");
        dischargeHandling.setFont(new Font("宋体", Font.PLAIN, 16));
        dischargeHandling.setBounds(650, 150, 150, 40);

        JButton dischargePayment = new JButton("出院缴费");
        dischargePayment.setFont(new Font("宋体", Font.PLAIN, 16));
        dischargePayment.setBounds(650, 250, 150, 40);

        JButton returnItem = new JButton("返回上一级");
        returnItem.setFont(new Font("宋体", Font.PLAIN, 16));
        returnItem.setBounds(450, 650, 150, 40);

        JLabel winJlabel = new JLabel(new ImageIcon("img/背景图片.jpg"));
        winJlabel.setBounds(0, 0, 1000, 850);

        management.add(admissionRegister);
        management.add(bedArrangement);
        management.add(wardManagement);
        management.add(patientView);
        management.add(expenseAccounting);
//        management.add(expenseReminder);
        management.add(expenseView);
        management.add(dischargeHandling);
        management.add(dischargePayment);
        management.add(returnItem);
        management.add(winJlabel);

        add(management);

        dischargePayment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "出院缴费");
                SwingUtilities.invokeLater(() -> {
                    DischargePayment dischargePayment = new DischargePayment();
                    dischargePayment.launch();
                });
            }
        });

        dischargeHandling.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "出院办理");
                SwingUtilities.invokeLater(() -> {
                    DischargeProcessing dischargeProcessing = new DischargeProcessing();
                    dischargeProcessing.launch();
                });
            }
        });

        expenseAccounting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "费用记账");
                SwingUtilities.invokeLater(() -> {
                    HospitalCostAccounting costAccounting = new HospitalCostAccounting();
                    costAccounting.launch();
                });
            }
        });

        patientView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "病人查看");
                SwingUtilities.invokeLater(() -> {
                    PatientView patientView = new PatientView();
                    patientView.launch();
                });
            }
        });

        bedArrangement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "床位安排");
                SwingUtilities.invokeLater(() -> {
                    SickbedStatusChange sickbedStatusChange = new SickbedStatusChange();
                    sickbedStatusChange.launch();
                });
            }
        });

        wardManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "病房管理");
                SwingUtilities.invokeLater(() -> {
                    SickroomManagement management = new SickroomManagement();
                    management.launch();
                });
            }
        });

        expenseView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "费用查看");
                SwingUtilities.invokeLater(() -> {
                    HospitalCostQuery hospitalCostQuery = new HospitalCostQuery();
                    hospitalCostQuery.launch();
                });
                // 在此处添加住院登记的页面
            }
        });

        admissionRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "住院登记");
                SwingUtilities.invokeLater(() -> {
                    HospitalRegistration registration = new HospitalRegistration();
                    registration.setVisible(true);
                });
                // 在此处添加住院登记的页面
            }
        });

        // 其余按钮的 ActionListener 部分省略

        returnItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回上一级
                JOptionPane.showMessageDialog(null, "返回上一级");
                dispose();
                new MenuFrame();
            }
        });

        setVisible(true);
    }

}
