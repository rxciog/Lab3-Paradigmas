package namedEntities.stats;

import java.util.ArrayList;
import java.util.List;

import namedEntities.NamedEntity;
import namedEntities.Classification.Category;
import namedEntities.Classification.Topic;

public class Stats {
    
    public void  getStatsByCategory(List<NamedEntity> namedEntities){
        List<NamedEntity> localEntities = new ArrayList<>(namedEntities);

        for (Category category: Category.values()){
            System.out.println("Category: " + category.toString());
            for (NamedEntity ne: localEntities){
                if (ne.getCategory() == category){
                    System.out.println("\t" + ne.getName() + "(" + ne.getMentions() + ")");
                }
            }
        }
    }

    public void  getStatsByTopic(List<NamedEntity> namedEntities){
        List<NamedEntity> localEntities = new ArrayList<>(namedEntities);

        for (Topic topic: Topic.values()){
            System.out.println("Topic: " + topic.toString());
            for (NamedEntity ne: localEntities){
                if (ne.getTopic().contains(topic)){
                    System.out.println("\t" + ne.getName() + "(" + ne.getMentions() + ")");
                }
            }
        }
    }


}
