package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Management extends JFrame {
    public Management() {
        setTitle("门诊管理系统");
        setSize(1000, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel management = new JPanel();
        management.setLayout(null);

        JButton medical_card = new JButton("医疗卡办理");
        medical_card.setFont(new Font("宋体", Font.PLAIN, 16));
        medical_card.setBounds(450, 50, 150, 40);

        JButton register = new JButton("挂号");
        register.setFont(new Font("宋体", Font.PLAIN, 16));
        register.setBounds(450, 150, 150, 40);

        JButton triage = new JButton("叫号分诊");
        triage.setFont(new Font("宋体", Font.PLAIN, 16));
        triage.setBounds(450, 250, 150, 40);

        JButton inpat_case = new JButton("病例填写");
        inpat_case.setFont(new Font("宋体", Font.PLAIN, 16));
        inpat_case.setBounds(450, 350, 150, 40);

        JButton medicial = new JButton("处方用药");
        medicial.setFont(new Font("宋体", Font.PLAIN, 16));
        medicial.setBounds(450, 450, 150, 40);

        JButton payout = new JButton("门诊缴费");
        payout.setFont(new Font("宋体", Font.PLAIN, 16));
        payout.setBounds(450, 550, 150, 40);

        JButton select = new JButton("查询窗口");
        select.setFont(new Font("宋体", Font.PLAIN, 16));
        select.setBounds(450, 650, 150, 40);

        JButton logoutItem = new JButton("返回上一级");
        logoutItem.setFont(new Font("宋体", Font.PLAIN, 16));
        logoutItem.setBounds(450, 750, 150, 40);

        JLabel winJlabel = new JLabel(new ImageIcon("img/背景图片.jpg"));
        winJlabel.setBounds(0, 0, 1000, 850);

        management.add(medical_card);
        management.add(register);
        management.add(triage);
        management.add(inpat_case);
        management.add(medicial);
        management.add(payout);
        management.add(select);
        management.add(logoutItem);
        management.add(winJlabel);

        add(management);

        select.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "查询窗口");
                SwingUtilities.invokeLater(() -> {
                    Select_diseaseFrame select_diseaseframe = new Select_diseaseFrame();
                    select_diseaseframe.lauch();
                });
            }
        });

        payout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "门诊缴费");
                SwingUtilities.invokeLater(() -> {
                    OutpatientPayment outpatientPayment = new OutpatientPayment();
                    outpatientPayment.launch();
                });
            }
        });

        medical_card.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "医保卡办理");
                dispose();
                Medical_cardFrame medical_cardframe = new Medical_cardFrame();
                medical_cardframe.lauch();
            }
        });

        medicial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "处方用药");
                SwingUtilities.invokeLater(() -> {
                    PrescriptionFrame prescriptionFrameframe = new PrescriptionFrame();
                    prescriptionFrameframe.lauch();
                });
            }
        });

        inpat_case.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "病例填写");
                SwingUtilities.invokeLater(() -> {
                    CaseFrame caseframe = new CaseFrame();
                    caseframe.lauch();
                });
            }
        });

        register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "挂号");
                dispose();
                RegistrationFrame registrationFrame = new RegistrationFrame();
                registrationFrame.launch();
            }
        });

        triage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "叫号分诊");
                TriageFrame triagingFrame = new TriageFrame();
                triagingFrame.launch();
            }
        });

        logoutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此处添加退出登录的操作
                JOptionPane.showMessageDialog(null, "返回上一级");
                dispose();
                new MenuFrame();
            }
        });

        setVisible(true);
    }


}
