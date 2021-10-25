package servlet;

import database.Product;
import database.ProductsDatabase;
import html.HTMLResponseBuilder;

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
        HTMLResponseBuilder builder = new HTMLResponseBuilder(response);

        try {
            List<Product> products = db.getAllItems();
            for (Product p : products) {
                builder.addLine(p.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        builder.finish();
    }
}
