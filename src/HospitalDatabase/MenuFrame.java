package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuFrame extends JFrame {
    public MenuFrame() {
        setTitle("管理系统");
        setSize(1000, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menuBar = new JPanel();
        menuBar.setLayout(null);

        JButton managementMenu = new JButton("门诊管理");
        managementMenu.setFont(new Font("宋体", Font.PLAIN, 16));
        managementMenu.setBounds(450, 250, 100, 40);

        JButton inpatientMenu = new JButton("住院管理");
        inpatientMenu.setFont(new Font("宋体", Font.PLAIN, 16));
        inpatientMenu.setBounds(450, 350, 100, 40);

        JButton pharmacyMenu = new JButton("药房管理");
        pharmacyMenu.setFont(new Font("宋体", Font.PLAIN, 16));
        pharmacyMenu.setBounds(450, 450, 100, 40);

        JButton systemMenu = new JButton("系统管理");
        systemMenu.setFont(new Font("宋体", Font.PLAIN, 16));
        systemMenu.setBounds(450, 550, 100, 40);

        JButton logoutItem = new JButton("退出登录");
        logoutItem.setFont(new Font("宋体", Font.PLAIN, 16));
        logoutItem.setBounds(450, 650, 100, 40);

        JLabel winJlabel = new JLabel(new ImageIcon("img/背景图片.jpg"));
        winJlabel.setBounds(0, 0, 1000, 850);

        menuBar.add(managementMenu);
        menuBar.add(inpatientMenu);
        menuBar.add(pharmacyMenu);
        menuBar.add(systemMenu);
        menuBar.add(logoutItem);
        menuBar.add(winJlabel);

        add(menuBar);

        systemMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此处添加退出登录的操作
                JOptionPane.showMessageDialog(null, "系统管理");
                dispose();
                new SystemManagement();
            }
        });

        pharmacyMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此处添加退出登录的操作
                JOptionPane.showMessageDialog(null, "药房管理");
                dispose();
                new PharmacyManagement();
            }
        });

        inpatientMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此处添加退出登录的操作
                JOptionPane.showMessageDialog(null, "住院管理");
                dispose();
                new InpatientManagement();
            }
        });

        managementMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此处添加退出登录的操作
                JOptionPane.showMessageDialog(null, "门诊管理");
                dispose();
                new Management();
            }
        });


        logoutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此处添加退出登录的操作
                JOptionPane.showMessageDialog(null, "退出登录");
                dispose();
                new LoginFrame();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new MenuFrame();
    }
}