package servlet;

import database.Product;
import database.ProductsDatabase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class GetProductsServlet extends AbstractProductsServlet {

    public GetProductsServlet(ProductsDatabase db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Product> products = db.getAllItems();
            response.getWriter().println("<html><body>");
            for (Product p : products) {
                response.getWriter().println(p.getName() + "\t" + p.getPrice() + "</br>");
            }
            response.getWriter().println("</body></html>");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
