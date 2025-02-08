package namedEntities.heuristics;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CoreNLP  implements Heuristic {
    public List<String> extractCandidates(String text){
        List<String> candidates = new ArrayList<>();

        Properties props = new  Properties();
        props.setProperty("annotators", "tokenize, ssplit, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP();

        CoreDocument document = new CoreDocument(text);

        pipeline.annotate(document);

        for (CoreEntityMention em : document.entityMentions()){
            if ( !em.entityType().equals("DATE") && !em.entityType().equals("NUMBER") ){
                candidates.add(em.text());
            }
        }

        return candidates;
    } 
}
