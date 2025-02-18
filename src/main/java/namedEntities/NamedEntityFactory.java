package namedEntities;

import java.util.List;

import namedEntities.Classification.Category;
import namedEntities.Classification.Topic;

public class NamedEntityFactory {
    public static NamedEntity createNamedEntity(String name, Category category, List<Topic> topic, int mentions,
            List<String> keywords) {
        switch (category) {
            case ORGANIZATION:
                return new Organization(name, category, topic, mentions, keywords, "UNK");
            case OTHER:
                return new Other(name, category, topic, mentions, keywords);
            case PERSON:
                return new Person(name, category, topic, mentions, keywords, 0, "UNK");
            case LOCATION:
                return new Location(name, category, topic, mentions, keywords, 0, 0, 0);
            default:
                return new Other(name, category, topic, mentions, keywords);
        }
    }
}
