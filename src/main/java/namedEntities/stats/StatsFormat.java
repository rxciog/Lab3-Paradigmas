package namedEntities.stats;

public enum StatsFormat {
    CAT("cat","Category-wise stats"),
    TOP("top", "Topic-wise stats");

    private final String value;
    private final String description;

    StatsFormat(String value, String description){
        this.value = value;
        this.description = description;
    }

    public String getValue(){
        return value;
    }
    public String getDescription(){
        return description;
    }

}