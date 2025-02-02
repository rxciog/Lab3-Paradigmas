package namedEntities.heuristics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class HeuristicFactory {
    private static Map<String,Supplier<Heuristic>> HEURISTIC_MAP = new HashMap<>();

    static {
        HEURISTIC_MAP.put("capital", CapitalizedWordHeuristic::new);
        HEURISTIC_MAP.put("+capital", CapitaLetterHeuristic::new);
        HEURISTIC_MAP.put("coreNLP", CoreNLP::new);
    }

    public static Heuristic createHeuristic(String heuristicName) {
        Supplier<Heuristic> heuristicSupplier = HEURISTIC_MAP.get(heuristicName);
        if (heuristicSupplier == null){
            throw new IllegalArgumentException("Heuristic not found: " + heuristicName);
        }

        return heuristicSupplier.get();
    }

    public static Set<String> getAvailableHeuristics(){
        return HEURISTIC_MAP.keySet();
    }
}
