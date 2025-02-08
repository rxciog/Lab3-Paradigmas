package namedEntities;

import java.io.Serializable;
import java.util.List;

import namedEntities.Classification.Category;
import namedEntities.Classification.Topic;

public class NamedEntity implements Serializable {
    private String name;
    private Category category;
    private List<Topic> topic;
    private Integer mentions;
    private List<String> keywords;

    public NamedEntity(String name, Category category, List<Topic> topic, Integer mentions, List<String> keywords) {
        this.name = name;
        this.category = category;
        this.topic = topic;
        this.mentions = mentions;
        this.keywords = keywords;
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

    public void setMentions(int mentions){
         this.mentions = mentions;
    }

    public List<String> getKeywords (){
        return keywords;
    }
}