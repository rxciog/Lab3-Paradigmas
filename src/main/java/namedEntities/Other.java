package namedEntities;

import java.util.List;

import namedEntities.Classification.Category;
import namedEntities.Classification.Topic;

public class Other extends NamedEntity {

    public Other(String name, Category category, List<Topic> topic, int mentions, List<String> keywords) {
        super(name, category, topic, mentions, keywords);
    }

}
