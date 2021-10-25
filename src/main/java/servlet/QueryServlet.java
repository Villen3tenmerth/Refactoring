package servlet;

import database.Product;
import database.ProductsDatabase;
import html.HTMLResponseBuilder;

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
        HTMLResponseBuilder builder = new HTMLResponseBuilder(response);

        try {
            if ("max".equals(command)) {
                Product p = db.getMaxPriceItem();
                if (p == null) {
                    builder.addHeader("There is no products", 1);
                } else {
                    builder.addHeader("Product with max price: ", 1);
                    builder.addLine(p.toString());
                }
            } else if ("min".equals(command)) {
                Product p = db.getMinPriceItem();
                if (p == null) {
                    builder.addHeader("There is no products", 1);
                } else {
                    builder.addHeader("Product with min price: ", 1);
                    builder.addLine(p.toString());
                }
            } else if ("sum".equals(command)) {
                long sum = db.getSumPrice();
                builder.addHeader("Summary price: " + sum, 0);
            } else if ("count".equals(command)) {
                int cnt = db.getCount();
                builder.addHeader("Number of products: " + cnt, 0);
            } else {
                builder.addHeader("Unknown command: " + command, 0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        builder.finish();
    }

}
