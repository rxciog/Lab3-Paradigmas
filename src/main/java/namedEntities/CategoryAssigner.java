package namedEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;


public class CategoryAssigner {
    
    public List<NamedEntity> assignCategory (List<String> candidates){
        List<NamedEntity> namedEntities = new ArrayList<>();

        String text = String.join(". ", candidates);

        Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);

        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        //for (CoreEntityMention em : document.entityMentions()){
        //    NamedEntity nEntity = new NamedEntity(em.text(), em.entityType(), new ArrayList<>(), 1);
        //    namedEntities.add(nEntity);
        //}


        return namedEntities;
    }
}
