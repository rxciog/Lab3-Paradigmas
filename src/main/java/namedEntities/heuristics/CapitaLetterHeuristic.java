package namedEntities.heuristics;
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

        String filepath= "src/data/stopwords.json";

        // Filtrar
        try {
            Set<String> stopWords = JSONParser.parseJsonList(filepath);

            candidates = candidates.stream().filter(word -> !stopWords.contains(word.toLowerCase())).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }
}
