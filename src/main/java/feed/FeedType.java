package feed;

public enum FeedType {
    P12PAIS("p12pais"),
    P12ECO("p12eco"),
    WIRED("wired"),
    ELPAISARG("elpaisArg"),
    ALL("all");

    private final String value;

    FeedType(String value){
        this.value = value;
    }

    public static FeedType fromString(String value) throws IllegalArgumentException{
        for (FeedType type: FeedType.values()){
            if (type.value.equalsIgnoreCase(value)){
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown feed key value");
    }

    public String getValue(){
        return value;
    }
}
