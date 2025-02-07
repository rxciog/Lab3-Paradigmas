package namedEntities.heuristics;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import utils.JSONParser;

public class CapitaLetterHeuristic implements Heuristic {

    //Extendemos la heuristica existente

    public List<String> extractCandidates(String text) {

        List<String> candidates = new ArrayList<>();

        text = text.replaceAll("[-+.^:]", "");

        // Matchear patrón 

        Pattern pattern = Pattern.compile("[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*"
                                            + "|" + "\\b[A-Z]+\\b"
                                            + "|" + "\\b[a-z]+[A-Z]+[a-z]+\\b"
                                            );


        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            candidates.add(matcher.group());
        }
        // Filtrar

        String filepath = "/stopwords.json";
        try (InputStream is = getClass().getResourceAsStream(filepath)) {
            if (is == null) {
                throw new IOException("No se encontró el archivo: " + filepath);
            }
            
            Set<String> stopWords = JSONParser.parseJsonList(is);
            candidates = candidates.stream().filter(word -> !stopWords.contains(word.toLowerCase())).collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("Error al leer archivo json: ");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return candidates;
    }
}
