import javax.servlet.*;
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
            ServletContext servletContext = this.getServletContext();
            Class.forName(servletContext.getInitParameter("driver"));
            connection = DriverManager.getConnection(
                    servletContext.getInitParameter("url"),
                    servletContext.getInitParameter("username"),
                    servletContext.getInitParameter("password"));
            statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "select MNO,MNAME,EMAIL,CRE_DATE" +
                            " from MEMBERS" +
                            " order by MNO ASC");
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html><head><title>회원목록</title></head>");
            out.println("<body><h1>회원목록</h1>");
            out.println("<p><a href='add'>신규회원</a></p>");
            while(resultSet.next()) {
                out.println(
                        resultSet.getInt("MNO") + "," +
                        "<a href='update?no=" + resultSet.getInt("MNO") + "'>" +
                        resultSet.getString("MNAME") + "</a>," +
                        resultSet.getString("EMAIL") + "," +
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