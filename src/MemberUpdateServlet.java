import com.sun.scenario.effect.impl.sw.java.JSWBlend_EXCLUSIONPeer;
import sun.reflect.annotation.ExceptionProxy;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by show9 on 2016-12-29.
 */
@WebServlet("/member/update")
public class MemberUpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            ServletContext servletContext = this.getServletContext();
            Class.forName(servletContext.getInitParameter("driver"));
            connection = DriverManager.getConnection(
                    servletContext.getInitParameter("url"),
                    servletContext.getInitParameter("username"),
                    servletContext.getInitParameter("password"));
            statement = connection.prepareStatement(
                    "update MEMBERS set EMAIL=?,MNAME=?,MOD_DATE=now()"
                    + " where MNO=?");
            statement.setString(1, request.getParameter("email"));
            statement.setString(2, request.getParameter("name"));
            statement.setString(3, request.getParameter("no"));

            statement.executeUpdate();
            response.sendRedirect("list");
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            try {if (statement != null) statement.close();} catch(Exception e) {}
            try {if (connection != null) connection.close();} catch(Exception e) {}
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    "select MNO,EMAIL,MNAME,CRE_DATE from MEMBERS" +
                    " where MNO=" + request.getParameter("no"));
            resultSet.next();

            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html><head><title>회원정보</title></head>");
            out.println("<body><h1>회원정보</h1>");
            out.println("<form action='update' method='post'>");
            // 회원번호는 readonly로 부름
            out.println("번호: <input type='text' name='no' value='" +
                        request.getParameter("no") + "'readonly><br>");
            out.println("이름: <input type='text' name='name'" + " value='"
                        + resultSet.getString("MNAME") + "'><br>");
            out.println("이메일: <input type='text' name='email'" + " value='"
                        + resultSet.getString("EMAIL") + "'><br>");
            out.println("가입일: " + resultSet.getDate("CRE_DATE") + "<br>");
            out.println("<input type='submit' value='저장'>");
            out.println("<input type='button' value='취소'" +
                        " onclick='location.href=\"list\"'>");
            out.println("</form>");
            out.println("</body></html>");
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            try {if (resultSet != null) resultSet.close();} catch(Exception e) {}
            try {if (statement != null) statement.close();} catch(Exception e) {}
            try {if (connection != null) connection.close();} catch(Exception e) {}
        }
    }
}
