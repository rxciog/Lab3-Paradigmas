package namedEntities;

import java.util.List;

import namedEntities.Classification.Category;
import namedEntities.Classification.Topic;

public class Location extends NamedEntity {
    private String name;
    private int postCode;
    private double latitude;
    private double longitude;

    public Location(String name, Category category, List<Topic> topic, int mentions, List<String> keywords,
            int postCode, double latitude, double longitude) {
        super(name, category, topic, mentions, keywords); // Assuming NamedEntity has a constructor that takes a name
        this.name = name;
        this.postCode = postCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public int getPostCode() {
        return postCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
