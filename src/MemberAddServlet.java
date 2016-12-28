import sun.reflect.annotation.ExceptionProxy;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by show9 on 2016-12-27.
 */
@WebServlet("/member/add")
public class MemberAddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            ServletContext servletContext = this.getServletContext();
            Class.forName(servletContext.getInitParameter("driver"));
            connection = DriverManager.getConnection(
                    servletContext.getInitParameter("url"),
                    servletContext.getInitParameter("username"),
                    servletContext.getInitParameter("password"));
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO MEMBERS(EMAIL,PWD,MNAME,CRE_DATE,MOD_DATE)"
            + " VALUE (?,?,?,NOW(),NOW())");
            preparedStatement.setString(1, request.getParameter("email"));
            preparedStatement.setString(2, request.getParameter("password"));
            preparedStatement.setString(3, request.getParameter("name"));
            preparedStatement.executeUpdate();

            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html><head><title>회원등록결과</title></head>");
            //out.println("<meta http-equiv='Refresh' content='1; url=list'>");
            out.println("<body>");
            out.println("<p>등록 성공입니다!</p>");
            out.println("</body></html>");

            // 1초뒤에 새로고침, url은 list로
            response.addHeader("Refresh", "1;url=list");
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            try {if (preparedStatement != null) preparedStatement.close();} catch (Exception e) {}
            try {if (connection != null) connection.close();} catch (Exception e) {}
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>회원등록</title></head>");
        out.println("<body><h1>회원등록</h1>");
        out.println("<form action='add' method='post'>");
        out.println("이름: <input type='text' name='name'><br>");
        out.println("이메일: <input type='text' name='email'><br>");
        out.println("암호: <input type='password' name='password'><br>");
        out.println("<input type='submit' value='추가'>");
        out.println("<input type='reset' value='취소'>");
        out.println("</form>");
        out.println("</body></html>");
    }
}
