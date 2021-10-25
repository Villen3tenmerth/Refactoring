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

    public void addLine(String line) {
        lines.add(line + "<br>");
    }

    public void addHeader(String header, int level) {
        if (level == 0) {
            lines.add(header);
        } else {
            lines.add("<h" + level + ">" + header + "</h" + level + ">");
        }
    }

    public String getResponseBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>\n");
        for (String line : lines) {
            sb.append(line).append('\n');
        }
        sb.append("</body></html>\n");
        return sb.toString();
    }

    public void finish() throws IOException {
        response.getWriter().print(getResponseBody());
        response.setContentType("test/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
