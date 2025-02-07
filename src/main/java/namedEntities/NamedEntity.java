package namedEntities;

import java.io.Serializable;
import java.util.List;

import namedEntities.Classification.Category;
import namedEntities.Classification.Topic;

public class NamedEntity implements Serializable {
    private String name;
    private Category category;
    private List<Topic> topic;
    private int mentions;

    public NamedEntity(String name, Category category, List<Topic> topic, int mentions) {
        this.name = name;
        this.category = category;
        this.topic = topic;
        this.mentions = mentions;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public List<Topic>  getTopic() {
        return topic;
    }

    public int getMentions(){
        return mentions;
    }

}