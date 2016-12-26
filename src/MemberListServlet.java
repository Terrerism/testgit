import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * Created by show9 on 2016-12-26.sf
 */

@WebServlet("/member/list")
public class MemberListServlet extends GenericServlet {
    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/studydb",
                    "study",
                    "study");
            statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "select MNO,MNAME,EMAIL,CRE_DATE" +
                            " from MEMBERS" +
                            " order by MNO ASC");
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html><head><title>회원목록</title></head>");
            out.println("<body><h1>회원목록</h1>");
            while(resultSet.next()) {
                out.println(
                        resultSet.getInt("MNO") + "," +
                                resultSet.getString("MNAME") + "," +
                                resultSet.getString("EMAIL") + "," +ㅌ
                                resultSet.getDate("CRE_DATE") + "<br>");
            }
            out.println("</body></html>");
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            try {if (resultSet != null) resultSet.close();} catch (Exception e) {}
            try {if (statement != null) statement.close();} catch (Exception e) {}
            try {if (connection != null) connection.close();} catch (Exception e) {}
        }
    }
}