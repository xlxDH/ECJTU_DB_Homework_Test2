package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SystemManagement extends JFrame {
    public SystemManagement() {
        setTitle("系统管理");
        setSize(1000, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel management = new JPanel();
        management.setLayout(null);

        JButton admissionRegister = new JButton("住院登记");
        admissionRegister.setFont(new Font("宋体", Font.PLAIN, 16));
        admissionRegister.setBounds(250, 150, 150, 40);

        JButton bedArrangement = new JButton("床位管理");
        bedArrangement.setFont(new Font("宋体", Font.PLAIN, 16));
        bedArrangement.setBounds(250, 250, 150, 40);

        JButton wardManagement = new JButton("病房管理");
        wardManagement.setFont(new Font("宋体", Font.PLAIN, 16));
        wardManagement.setBounds(250, 350, 150, 40);

//        JButton patientView = new JButton("病人查看");
//        patientView.setFont(new Font("宋体", Font.PLAIN, 16));
//        patientView.setBounds(250, 450, 150, 40);

        JButton surgeryManagement = new JButton("手术项目管理");
        surgeryManagement.setFont(new Font("宋体", Font.PLAIN, 16));
        surgeryManagement.setBounds(650, 150, 150, 40);

        JButton examinationManagement = new JButton("检查项目管理");
        examinationManagement.setFont(new Font("宋体", Font.PLAIN, 16));
        examinationManagement.setBounds(650, 250, 150, 40);

        JButton userManagement = new JButton("用户管理");
        userManagement.setFont(new Font("宋体", Font.PLAIN, 16));
        userManagement.setBounds(650, 350, 150, 40);

        JButton departmentManagement = new JButton("科室管理");
        departmentManagement.setFont(new Font("宋体", Font.PLAIN, 16));
        departmentManagement.setBounds(650, 450, 150, 40);

        JButton diseaseManagement = new JButton("疾病管理");
        diseaseManagement.setFont(new Font("宋体", Font.PLAIN, 16));
        diseaseManagement.setBounds(250, 450, 150, 40);

//        JButton databaseManagement = new JButton("数据库备份与恢复");
//        databaseManagement.setFont(new Font("宋体", Font.PLAIN, 16));
//        databaseManagement.setBounds(250, 550, 200, 40);

        JButton returnItem = new JButton("返回上一级");
        returnItem.setFont(new Font("宋体", Font.PLAIN, 16));
        returnItem.setBounds(450, 650, 150, 40);

        JLabel winJlabel = new JLabel(new ImageIcon("img/背景图片.jpg"));
        winJlabel.setBounds(0, 0, 1000, 850);

        management.add(admissionRegister);
        management.add(bedArrangement);
        management.add(wardManagement);
//        management.add(patientView);
        management.add(surgeryManagement);
        management.add(examinationManagement);
        management.add(userManagement);
        management.add(departmentManagement);
        management.add(diseaseManagement);
//        management.add(databaseManagement);
        management.add(returnItem);
        management.add(winJlabel);

        add(management);

        // ActionListener 部分省略

        admissionRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回上一级
                JOptionPane.showMessageDialog(null, "住院登记");
                SwingUtilities.invokeLater(() -> {
                    HospitalRegistration registration = new HospitalRegistration();
                    registration.setVisible(true);
                });
            }
        });

        bedArrangement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回上一级
                JOptionPane.showMessageDialog(null, "床位管理");
                SwingUtilities.invokeLater(() -> {
                    SickbedManagement management = new SickbedManagement();
                    management.launch();
                });
            }
        });

        wardManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回上一级
                JOptionPane.showMessageDialog(null, "病房管理");
                SwingUtilities.invokeLater(() -> {
                    SickbedManagement management = new SickbedManagement();
                    management.launch();
                });
            }
        });

        userManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回上一级
                JOptionPane.showMessageDialog(null, "用户管理");
                SwingUtilities.invokeLater(() -> {
                    UserManagement management = new UserManagement();
                    management.launch();
                });
            }
        });

        examinationManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回上一级
                JOptionPane.showMessageDialog(null, "检查项目管理");
                SwingUtilities.invokeLater(() -> {
                    ExaminationManagement management = new ExaminationManagement();
                    management.launch();
                });
            }
        });

        surgeryManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回上一级
                JOptionPane.showMessageDialog(null, "手术项目管理");
                SwingUtilities.invokeLater(() -> {
                    OperationManagement operationManagement = new OperationManagement();
                    operationManagement.launch();
                });
            }
        });

        diseaseManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回上一级
                JOptionPane.showMessageDialog(null, "疾病管理");
                SwingUtilities.invokeLater(() -> {
                    DiseaseManagement diseaseManagement = new DiseaseManagement();
                    diseaseManagement.launch();
                });
            }
        });

        departmentManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 返回上一级
                JOptionPane.showMessageDialog(null, "科室管理");
                SwingUtilities.invokeLater(() -> {
                    DepartmentManagement departmentManagement = new DepartmentManagement();
                    departmentManagement.launch();
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
