package servlet;

import database.ProductsDatabase;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class AddProductServletTest {
    protected static final HttpClient CLIENT = HttpClient.newHttpClient();
    protected static final int PORT = 8082;
    protected static final Server SERVER = new Server(PORT);

    private static final String SERVLET_PATH = "/add-product";
    private static final String CORRECT_RESPONSE = "<html><body>\nOK\n</body></html>\n";

    private HttpResponse<String> makeRequest(String params) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + params))
                .header("accept", "text/html").build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @BeforeAll
    public static void beforeAll() throws Exception {
        File file = File.createTempFile("test", "db", new File("."));
        file.deleteOnExit();

        ProductsDatabase db = new ProductsDatabase(file.getAbsolutePath());
        db.createTable();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        SERVER.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(db)), SERVLET_PATH);
        SERVER.start();
    }

    @Test
    void correctRequest() throws IOException, InterruptedException {
        HttpResponse<String> response = makeRequest("?name=apple&price=100");
        assertEquals(CORRECT_RESPONSE, response.body());
    }

    @Test
    void noNameRequest() throws IOException, InterruptedException {
        HttpResponse<String> response = makeRequest("?price=100");
        assertNotEquals(CORRECT_RESPONSE, response.body());
    }

    @Test
    void noPriceRequest() throws IOException, InterruptedException {
        HttpResponse<String> response = makeRequest("?name=apple");
        assertNotEquals(CORRECT_RESPONSE, response.body());
    }

    @AfterAll
    public static void afterAll() throws Exception {
        SERVER.stop();
    }
}