package HospitalDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;

public class PostgreSqlJdbcConnAddDatas {
    public static void main(String args[]) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            /*
             * @para:url,usename,password
             * @return: null
             */
            c = DriverManager.getConnection("jdbc:postgresql://124.220.162.220:8000/medical","gaussdb","Enmo@123");
            c.setAutoCommit(false);

            System.out.println("连接数据库成功！");
            stmt = c.createStatement();


            /*
             * @功能:插入数据
             * @参数:表名称（列名1，列名2···列名n）+（数据1，数据2，···数据n）
             */
//			String sql = "INSERT INTO Course (Cno,Cname,Cpno,Ccredit) "
//					+ "VALUES (1,'数据库',5,4);";
//			stmt.executeUpdate(sql);
//			System.out.println("新增数据成功！");


            /*
             * @功能:查询数据
             * @参数:表名
             */
			ResultSet rs = stmt.executeQuery("select * from medi_schema.tb_sickroom");
			while(rs.next()){
				int room_id = rs.getInt("room_id");
				int dept_id = rs.getInt("dept_id");
				String type  = rs.getString("type");
                int price = rs.getInt("price");
				System.out.println(room_id + "," + dept_id + "," + type + "," + price);
			}
			System.out.println("查询数据成功！");
//
            /*
             * @功能:更新数据
             * @参数:
             */

//            String sql = "UPDATE sc set grade = 250 where cno=1 ";
//            stmt.executeUpdate(sql);
//            c.commit();
//
//            ResultSet rs1 = stmt.executeQuery("select * from sc order by cno");
//            while(rs1.next()){
//                int sno = rs1.getInt("Sno");
//                int cno = rs1.getInt("Cno");
//                int grade = rs1.getInt("Grade");
//                System.out.println(sno + "," + cno + "," + grade);
//            }
//            System.out.println("更新数据成功！");


            /*
             * @功能:删除数据
             * @参数:
             */
//			String sql = "Delete from sc where Cno=2 ";
//			stmt.executeUpdate(sql);
//			c.commit();
//
//			ResultSet rs1 = stmt.executeQuery("select * from sc order by cno");
//			while(rs1.next()){
//				int sno = rs1.getInt("Sno");
//				int cno = rs1.getInt("Cno");
//				int grade = rs1.getInt("Grade");
//				System.out.println(sno + "," + cno + "," + grade);
//			}
//			System.out.println("删除数据成功！");


            stmt.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
//		System.out.println("新增数据成功！");
    }
}