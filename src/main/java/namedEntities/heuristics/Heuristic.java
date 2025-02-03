package namedEntities.heuristics;

import java.io.Serializable;
import java.util.List;

public interface Heuristic extends Serializable {
    //Hacer serializable
    public List<String> extractCandidates(String text);
    
}
