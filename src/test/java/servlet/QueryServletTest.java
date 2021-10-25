package servlet;

import database.Product;
import database.ProductsDatabase;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class QueryServletTest {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final int PORT = 8082;
    private static final Server SERVER = new Server(PORT);
    private static ProductsDatabase db;

    private static final String SERVLET_PATH = "/query";

    @BeforeAll
    public static void beforeAll() throws Exception {
        File file = File.createTempFile("test", "db", new File("."));
        file.deleteOnExit();

        db = new ProductsDatabase(file.getAbsolutePath());
        db.createTable();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        SERVER.setHandler(context);

        context.addServlet(new ServletHolder(new QueryServlet(db)), SERVLET_PATH);
        SERVER.start();
    }

    private HttpResponse<String> makeRequest(String params) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + params))
                .header("accept", "text/html").build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void fillDatabase() throws SQLException {
        db.insertItem(new Product("apple", 100));
        db.insertItem(new Product("pen", 200));
        db.insertItem(new Product("pineapple", 300));
    }

    @Test
    void emptyTest() throws IOException, InterruptedException {
        HttpResponse<String> response = makeRequest("?command=count");
        assertEquals("<html><body>\nNumber of products: 0\n</body></html>\n", response.body());

        response = makeRequest("?command=sum");
        assertEquals("<html><body>\nSummary price: 0\n</body></html>\n", response.body());

        response = makeRequest("?command=min");
        assertEquals("<html><body>\n<h1>There is no products</h1>\n</body></html>\n", response.body());

        response = makeRequest("?command=max");
        assertEquals("<html><body>\n<h1>There is no products</h1>\n</body></html>\n", response.body());
    }

    @Test
    void simpleTest() throws SQLException, IOException, InterruptedException {
        fillDatabase();
        HttpResponse<String> response = makeRequest("?command=count");
        assertEquals("<html><body>\nNumber of products: 3\n</body></html>\n", response.body());

        response = makeRequest("?command=sum");
        assertEquals("<html><body>\nSummary price: 600\n</body></html>\n", response.body());

        response = makeRequest("?command=min");
        assertEquals("<html><body>\n<h1>Product with min price: </h1>\n" +
                "apple\t100<br>\n</body></html>\n", response.body());

        response = makeRequest("?command=max");
        assertEquals("<html><body>\n<h1>Product with max price: </h1>\n" +
                "pineapple\t300<br>\n</body></html>\n", response.body());
    }

    @Test
    void wrongCommandTest() throws IOException, InterruptedException, SQLException {
        fillDatabase();
        HttpResponse<String> response = makeRequest("?command=doSomething");
        assertEquals("<html><body>\nUnknown command: doSomething\n</body></html>\n", response.body());
    }

    @AfterEach
    public void afterEach() throws Exception {
        db.clearTable();
    }

    @AfterAll
    public static void afterAll() throws Exception {
        SERVER.stop();
    }
}