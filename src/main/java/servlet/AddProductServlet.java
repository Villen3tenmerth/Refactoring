package servlet;

import database.Product;
import database.ProductsDatabase;
import html.HTMLResponseBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AddProductServlet extends AbstractProductsServlet {

    public AddProductServlet(ProductsDatabase db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HTMLResponseBuilder builder = new HTMLResponseBuilder(response);

        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        try {
            db.insertItem(new Product(name, price));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        builder.addHeader("OK", 0);
        builder.finish();
    }
}
