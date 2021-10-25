package html;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTMLResponseBuilderTest {
    private HTMLResponseBuilder responseBuilder;
    private static final String HEADER = "<html><body>\n";
    private static final String FOOTER = "</body></html>\n";

    @BeforeEach
    void beforeEach() {
        responseBuilder = new HTMLResponseBuilder(null);
    }

    @Test
    void emptyTest() {
        String body = responseBuilder.getResponseBody();
        assertEquals(HEADER + FOOTER, body);
    }

    @Test
    void headersTest() {
        String line1 = "Some text";
        String line2 = "More text";

        responseBuilder.addHeader(line1, 1);
        responseBuilder.addHeader(line2, 2);
        String body = responseBuilder.getResponseBody();
        String expected = HEADER
                + "<h1>" + line1 + "</h1>\n"
                + "<h2>" + line2 + "</h2>\n"
                + FOOTER;
        assertEquals(expected, body);
    }

    @Test
    void linesTest() {
        String line0 = "A header: ";
        String line1 = "Some text";
        String line2 = "More text";
        responseBuilder.addHeader(line0, 0);
        responseBuilder.addLine(line1);
        responseBuilder.addLine(line2);
        String body = responseBuilder.getResponseBody();
        String expected = HEADER
                + line0 + "\n"
                + line1 + "<br>\n"
                + line2 + "<br>\n"
                + FOOTER;
        assertEquals(expected, body);
    }
}