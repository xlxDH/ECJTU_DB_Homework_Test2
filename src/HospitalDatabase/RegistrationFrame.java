package HospitalDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationFrame extends JFrame {
	private JTextField jtextfield2;
	private JTextField jtextfield3;
	private JTextField jtextfield5;
	private JTextField jtextfield6;
	private JTextField jtextfield7;
	private JTextField jtextfield8;
	private JTextField jtextfield9;
	private JTextField jtextfield10;

	public void launch() {
		setSize(800, 800);
		setResizable(false);
		setTitle("挂号");
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public RegistrationFrame() {
		JPanel panel = new JPanel();
		panel.setLayout(null);

		JLabel label2 = new JLabel("医保卡号:");
		label2.setFont(new Font("宋体", Font.PLAIN, 20));
		label2.setBounds(280, 150, 100, 20);
		jtextfield2 = new JTextField(20);
		jtextfield2.setFont(new Font("宋体", Font.PLAIN, 20));
		jtextfield2.setBounds(380, 150, 100, 20);
		panel.add(label2);
		panel.add(jtextfield2);

		JLabel label3 = new JLabel("挂号类型:");
		label3.setFont(new Font("宋体", Font.PLAIN, 20));
		label3.setBounds(280, 250, 100, 20);
		jtextfield3 = new JTextField(20);
		jtextfield3.setFont(new Font("宋体", Font.PLAIN, 20));
		jtextfield3.setBounds(380, 250, 100, 20);
		panel.add(label3);
		panel.add(jtextfield3);

		JLabel label5 = new JLabel("就诊科室:");
		label5.setFont(new Font("宋体", Font.PLAIN, 20));
		label5.setBounds(280, 350, 100, 20);
		jtextfield5 = new JTextField(20);
		jtextfield5.setFont(new Font("宋体", Font.PLAIN, 20));
		jtextfield5.setBounds(380, 350, 100, 20);
		panel.add(label5);
		panel.add(jtextfield5);

		JLabel label6 = new JLabel("工号:");
		label6.setFont(new Font("宋体", Font.PLAIN, 20));
		label6.setBounds(280, 450, 100, 20);
		jtextfield6 = new JTextField(20);
		jtextfield6.setFont(new Font("宋体", Font.PLAIN, 20));
		jtextfield6.setBounds(380, 450, 100, 20);
		panel.add(label6);
		panel.add(jtextfield6);

		// ... (其他文本框的类似设置)

		JButton registrationButton = new JButton("挂号");
		registrationButton.setFont(new Font("宋体", Font.PLAIN, 16));
		registrationButton.setBounds(380, 620, 100, 40);
		registrationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertData();
			}
		});

		panel.add(registrationButton);
		setContentPane(panel);
	}

	private void insertOutpatientCharges(String med_id,int reg_id, String costName, int count, double totalMoney, double payMoney, double changeMoney) {
		String url = "jdbc:postgresql://124.220.162.220:8000/medical";
		String user = "gaussdb";
		String password = "Enmo@123";
		String sql = "INSERT INTO medi_schema.tb_outpatient_charges (med_id, reg_id, cost_name, count, total_money, pay_money, change_money, time, state) " +
				"VALUES (?,?,?,?,?,?,?,current_timestamp,?)";
		try (Connection connection = DriverManager.getConnection(url, user, password);
			 PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, med_id); // 设置医保卡号
			statement.setInt(2, reg_id); // 使用挂号ID
			statement.setString(3, costName);
			statement.setInt(4, count);
			statement.setDouble(5, totalMoney);
			statement.setDouble(6, payMoney);
			statement.setDouble(7, changeMoney);
			statement.setString(8, "待缴费"); // 设置状态值

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				JOptionPane.showMessageDialog(null, "费用信息写入成功");
			} else {
				JOptionPane.showMessageDialog(null, "费用信息写入失败");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "写入费用信息出错: " + ex.getMessage());
		}
	}

	private int fetchLatestRegisterId() {
		int registerId = 0;
		String url = "jdbc:postgresql://124.220.162.220:8000/medical";
		String user = "gaussdb";
		String password = "Enmo@123";
		String sql = "SELECT reg_id FROM medi_schema.tb_register ORDER BY register_time DESC LIMIT 1";
		try (Connection connection = DriverManager.getConnection(url, user, password);
			 PreparedStatement statement = connection.prepareStatement(sql);
			 ResultSet resultSet = statement.executeQuery()) {
			if (resultSet.next()) {
				registerId = resultSet.getInt("reg_id");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "获取挂号ID时发生错误: " + ex.getMessage());
		}
		return registerId;
	}

	private int insertRegistrationAndGetId(String med_id, String reg_type, String reg_price, String dep_id, String uid) {
		int regId = 0;
		String url = "jdbc:postgresql://124.220.162.220:8000/medical";
		String user = "gaussdb";
		String password = "Enmo@123";
		String sql = "INSERT INTO medi_schema.tb_register (med_id, reg_type, reg_price, other_price, dept_id, uid, rerister_time, visit_time, state) " +
				"VALUES (?,?,?,?,?,?,current_timestamp,?,?) RETURNING reg_id";
		try (Connection connection = DriverManager.getConnection(url, user, password);
			 PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, med_id);
			statement.setString(2, reg_type);
			statement.setString(3, reg_price);
			statement.setDouble(4, 0); // 使用 setDouble 设置默认值 0
			statement.setString(5, dep_id);
			statement.setString(6, uid);
			statement.setDate(7, new java.sql.Date(System.currentTimeMillis())); // 设置访问时间
			statement.setString(8, "待就诊"); // 设置状态值

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				regId = resultSet.getInt(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,"挂号失败: " + ex.getMessage());
		}
		return regId;
	}

	private void insertData() {
		String med_id = jtextfield2.getText();
		String reg_type = jtextfield3.getText();
		String costName = "挂号费"; // 替换为实际的费用名称
		int count = 1;  // 替换为实际的数量
		double payMoney = 0.00;  // 替换为实际的支付金额
		double changeMoney = 0.00;  // 替换为实际的找零金额
		String reg_price = "";
		String dept_name = jtextfield5.getText();
		String dep_id = "";
		String uid = jtextfield6.getText();
		// 获取其他文本框的数据...

		String url = "jdbc:postgresql://124.220.162.220:8000/medical";
		String user = "gaussdb";
		String password = "Enmo@123";

		try (Connection c = DriverManager.getConnection("jdbc:postgresql://124.220.162.220:8000/medical", "gaussdb", "Enmo@123")) {
			String query = "SELECT dept_id FROM medi_schema.tb_department WHERE dept_name = ?"; // 修改查询表名
			try (PreparedStatement stmt = c.prepareStatement(query)) {
				stmt.setString(1, dept_name); // 设置参数
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						dep_id = rs.getString("dept_id");
					} else {
						JOptionPane.showMessageDialog(null, "科室不存在");
						return; // 在此处返回避免执行后续代码
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // 打印异常信息
			JOptionPane.showMessageDialog(null, "查询科室信息时发生错误: " + ex.getMessage()); // 显示错误信息给用户
		}

		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			String sql = "SELECT get_department_price_by_id(?) AS reg_price";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, Integer.parseInt(dep_id));
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						reg_price = resultSet.getString("reg_price");
						// 设置挂号费用到文本框
					} else {
						JOptionPane.showMessageDialog(null, "找不到科室费用");
						return;
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "查询科室费用时发生错误: " + ex.getMessage());
		}

		int regId = insertRegistrationAndGetId(med_id, reg_type, reg_price, dep_id, uid);
		double totalMoney = Double.parseDouble(reg_price);
		insertOutpatientCharges(med_id,regId, "挂号费", 1, totalMoney, totalMoney, 0.0);
	}

}