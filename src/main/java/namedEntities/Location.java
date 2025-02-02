package namedEntities;

public class Location {
    private String name;
    private int postCode;
    private double latitude;
    private double longitude;

    public Location(String name, int postCode, double latitude, double longitude) {
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

    public double  getLongitude() {
        return longitude;
    }

    public double  getLatitude() {
        return latitude;
    }
}
