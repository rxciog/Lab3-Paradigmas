package namedEntities;

import java.util.List;

import namedEntities.Classification.Category;
import namedEntities.Classification.Topic;

public class Person extends NamedEntity {
    private String name;
    private int age;
    private String pob;

    public Person(String name, Category category, List<Topic> topic, int mentions, List<String> keywords, int age,
            String pob) {
        super(name, category, topic, mentions, keywords);
        this.name = name;
        this.age = age;
        this.pob = pob;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPOB() {
        return pob;
    }
}
