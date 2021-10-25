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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetProductsServletTest {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final int PORT = 8082;
    private static final Server SERVER = new Server(PORT);
    private static final String HEADER = "<html><body>";
    private static final String FOOTER = "</body></html>";
    private static ProductsDatabase db;

    private static final String SERVLET_PATH = "/get-products";

    @BeforeAll
    public static void beforeAll() throws Exception {
        File file = File.createTempFile("test", "db", new File("."));
        file.deleteOnExit();

        db = new ProductsDatabase(file.getAbsolutePath());
        db.createTable();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        SERVER.setHandler(context);

        context.addServlet(new ServletHolder(new GetProductsServlet(db)), SERVLET_PATH);
        SERVER.start();
    }

    private HttpResponse<String> makeRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH))
                .header("accept", "text/html").build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void emptyTest() throws IOException, InterruptedException {
        HttpResponse<String> response = makeRequest();
        assertEquals(HEADER + "\n" + FOOTER + "\n", response.body());
    }

    @Test
    void simpleTest() throws Exception {
        Product p1 = new Product("apple", 100);
        Product p2 = new Product("pen", 200);
        Product p3 = new Product("pineapple", 300);
        List<Product> products = List.of(p1, p2, p3);

        for (Product p : products) {
            db.insertItem(p);
        }

        HttpResponse<String> response = makeRequest();
        List<String> lines = List.of(response.body().split("\n"));
        assertEquals(HEADER, lines.get(0));
        assertEquals(FOOTER, lines.get(lines.size() - 1));
        for (Product p : products) {
            assertTrue(lines.contains(p + "<br>"));
        }
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