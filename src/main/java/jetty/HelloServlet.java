package jetty;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        System.out.println("post method" + this.getClass());

        Writer writer = null;
        try {
            writer = response.getWriter();
            writer.write("Hello World - post response");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        System.out.println("get method" + this.getClass());
        Writer writer = null;
        try {
            writer = response.getWriter();
            writer.write("Hello World - get response");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
