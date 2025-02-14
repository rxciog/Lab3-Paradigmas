package namedEntities.heuristics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class HeuristicFactory {
    private static Map<HeuristicType,Supplier<Heuristic>> HEURISTIC_MAP = new HashMap<>();

    static {
        HEURISTIC_MAP.put(HeuristicType.CAPITAL, CapitalizedWordHeuristic::new);
        HEURISTIC_MAP.put(HeuristicType.PLUS_CAPITAL, CapitaLetterHeuristic::new);
        HEURISTIC_MAP.put(HeuristicType.CORE_NLP, CoreNLP::new);
        HEURISTIC_MAP.put(HeuristicType.PREFIX, PrefixBasedHeuristic::new);
    }

    public static Heuristic createHeuristic(HeuristicType heuristicName) {
        Supplier<Heuristic> heuristicSupplier = HEURISTIC_MAP.get(heuristicName);
        if (heuristicSupplier == null){
            throw new IllegalArgumentException("Heuristic not found: " + heuristicName);
        }

        return heuristicSupplier.get();
    }

    public static Set<HeuristicType> getAvailableHeuristics(){
        return HEURISTIC_MAP.keySet();
    }
}
