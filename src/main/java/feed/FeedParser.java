package feed;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class FeedParser {

    private static String getTagContent(Element element, String tagName) {
        NodeList nodeL = element.getElementsByTagName(tagName);

        if (nodeL.getLength() > 0) {
            Node node = nodeL.item(0);

            if (node != null)
                return node.getTextContent();
        }

        return "";
    }

    public static List<Article> parseXML(String xmlData) {
        List<Article> articles = new ArrayList<>();

        try {

            InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document xmldoc = docBuilder.parse(inputStream);

            NodeList itemsList = xmldoc.getElementsByTagName("item");

            for (int temp = 0; temp < itemsList.getLength(); temp++) {
                Node item = itemsList.item(temp);

                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) item;

                    Article article = new Article(
                            getTagContent(elem, "title"),
                            getTagContent(elem, "description"),
                            getTagContent(elem, "pubDate"),
                            getTagContent(elem, "link"));
                    articles.add(article);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

    public static String fetchFeed(String feedURL) throws MalformedURLException, IOException, Exception {

        URL url = new URL(feedURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        // TODO: Cambiar el user-agent al nombre de su grupo.
        // Si todos los grupos usan el mismo user-agent, el servidor puede bloquear las
        // solicitudes.
        connection.setRequestProperty("User-agent", "lab_paradigmas/100");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new Exception("HTTP error code: " + status);
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            return content.toString();
        }
    }
}
