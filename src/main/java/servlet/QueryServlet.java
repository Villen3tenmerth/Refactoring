package servlet;

import database.Product;
import database.ProductsDatabase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class QueryServlet extends AbstractProductsServlet {

    public QueryServlet(ProductsDatabase db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            try {
                response.getWriter().println("<html><body>");
                Product p = db.getMaxPriceItem();
                if (p == null) {
                    response.getWriter().println("<h1>There is no products</h1>");
                } else {
                    response.getWriter().println("<h1>Product with max price: </h1>");
                    response.getWriter().println(p.getName() + "\t" + p.getPrice() + "</br>");
                }
                response.getWriter().println("</body></html>");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                response.getWriter().println("<html><body>");
                Product p = db.getMinPriceItem();
                if (p == null) {
                    response.getWriter().println("<h1>There is no products</h1>");
                } else {
                    response.getWriter().println("<h1>Product with min price: </h1>");
                    response.getWriter().println(p.getName() + "\t" + p.getPrice() + "</br>");
                }
                response.getWriter().println("</body></html>");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                response.getWriter().println("<html><body>");
                response.getWriter().println("Summary price: ");
                long sum = db.getSumPrice();
                response.getWriter().println(sum);
                response.getWriter().println("</body></html>");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                response.getWriter().println("<html><body>");
                response.getWriter().println("Number of products: ");
                int cnt = db.getCount();
                response.getWriter().println(cnt);
                response.getWriter().println("</body></html>");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
