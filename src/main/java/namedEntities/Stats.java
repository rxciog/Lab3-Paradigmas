package namedEntities;

import java.util.ArrayList;
import java.util.List;

import namedEntities.Classification.Category;
import namedEntities.Classification.Topic;

public class Stats {
    
    public void  getStatsByCategory(List<NamedEntity> namedEntities){
        //debería añadir un counter a named entity porque sino se hace un lío sacar el total acá ...
        //modificar para que el arreglo de entidades no se recorra todo para cada cat/topic
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
            System.out.println("Category: " + topic.toString());
            for (NamedEntity ne: localEntities){
                if (ne.getTopic().contains(topic)){
                    System.out.println("\t" + ne.getName() + "(" + ne.getMentions() + ")");
                }
            }
        }
    }


}
