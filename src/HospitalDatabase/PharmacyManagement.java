package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PharmacyManagement extends JFrame {
    public PharmacyManagement() {
        setTitle("药房管理系统");
        setSize(1000, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel management = new JPanel();
        management.setLayout(null);

        JButton prescriptionView = new JButton("药方查看");
        prescriptionView.setFont(new Font("宋体", Font.PLAIN, 16));
        prescriptionView.setBounds(450, 250, 150, 40);

//        JButton drugIssue = new JButton("药品发放");
//        drugIssue.setFont(new Font("宋体", Font.PLAIN, 16));
//        drugIssue.setBounds(450, 350, 150, 40);

        JButton drugManagement = new JButton("药品管理");
        drugManagement.setFont(new Font("宋体", Font.PLAIN, 16));
        drugManagement.setBounds(450, 450, 150, 40);

        JButton returnItem = new JButton("返回上一级");
        returnItem.setFont(new Font("宋体", Font.PLAIN, 16));
        returnItem.setBounds(450, 650, 150, 40);

        JLabel winJlabel = new JLabel(new ImageIcon("img/背景图片.jpg"));
        winJlabel.setBounds(0, 0, 1000, 850);

        management.add(prescriptionView);
//        management.add(drugIssue);
        management.add(drugManagement);
        management.add(returnItem);
        management.add(winJlabel);

        add(management);

        // 其余按钮的 ActionListener 部分
        prescriptionView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "药方查看");
                SwingUtilities.invokeLater(() -> {
                    PrescriptionManagement prescriptionManagement = new PrescriptionManagement();
                    prescriptionManagement.launch();
                });
            }
        });

//        drugIssue.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null, "药品发放");
//                // 在此处添加药品发放的页面
//            }
//        });

        drugManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "药品管理");
                SwingUtilities.invokeLater(() -> {
                    DrugManagement drugManagement = new DrugManagement();
                    drugManagement.launch();
                });
            }
        });

        returnItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回上一级
                JOptionPane.showMessageDialog(null, "返回上一级");
                dispose();
                new MenuFrame(); // 修改为对应的上一级界面
            }
        });

        setVisible(true);
    }


}