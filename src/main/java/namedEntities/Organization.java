package namedEntities;

import java.util.List;

import namedEntities.Classification.Category;
import namedEntities.Classification.Topic;

public class Organization extends NamedEntity {
    private String name;
    private String type;

    public Organization(String name, Category category, List<Topic> topic, int mentions, List<String> keywords, String type) {
        super(name, category, topic, mentions, keywords);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
