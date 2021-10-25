package html;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTMLResponseBuilder {
    private final HttpServletResponse response;
    private final List<String> lines;

    public HTMLResponseBuilder(HttpServletResponse response) {
        this.response = response;
        lines = new ArrayList<>();
    }

    private void print(String s) throws IOException {
        response.getWriter().println(s);
    }

    public void addLine(String line) {
        lines.add(line + "<br>");
    }

    public void addHeader(String header, int level) {
        if (level == 0) {
            lines.add(header);
        } else {
            lines.add("<h" + level + ">" + header + "<h" + level + "/>");
        }
    }

    public void finish() throws IOException {
        print("<html><body>");
        for (String line : lines) {
            print(line);
        }
        print("</body></html>");
        response.setContentType("test/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
