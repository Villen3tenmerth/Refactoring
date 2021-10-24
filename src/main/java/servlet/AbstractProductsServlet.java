package servlet;

import database.ProductsDatabase;

import javax.servlet.http.HttpServlet;

public abstract class AbstractProductsServlet extends HttpServlet {
    protected final ProductsDatabase db;

    protected AbstractProductsServlet(ProductsDatabase db) {
        this.db = db;
    }

}
