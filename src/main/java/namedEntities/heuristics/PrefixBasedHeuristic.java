package namedEntities.heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrefixBasedHeuristic implements Heuristic {

    private static List<String> prefixes = Arrays.asList(
            "Rey", "Reina", "Presidente", "Ministro", "Ministra", "Senador", "Senadora", "Gobernador", "Gobernadora",
            "Juez", "Jueza",
            "Gobierno de", "Ministerio de", "Secretaría de", "Consejo Nacional de",
            "Cámara de Diputados", "Congreso de", "Universidad de", "Colegio de", "Escuela Nacional de",
            "Instituto Nacional de", "Academia de", "Universidad Nacional de", "Grupo", "Corporación", "Banco",
            "Compañía", "Editorial", "Ciudad de", "Municipio de", "Estado de", "República de",
            "Cerro", "Monte", "Río", "Lago", "Valle");

    public List<String> extractCandidates(String text) {
        List<String> candidates = new ArrayList<>();

        String regex = "\\b(" + String.join("|", prefixes) + ")\\b\\s[A-Z][a-z]+(?:\\\\s[A-Z][a-z]+)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            candidates.add(matcher.group());
        }

        return candidates;
    }
}