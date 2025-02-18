package namedEntities.heuristics;

public enum HeuristicType {
    CAPITAL("capital", "Capitalized words are recognized as named entities"),
    PLUS_CAPITAL("+capital", "Capitalized words who are not stopwords are recognized as named entities"),
    CORE_NLP("coreNLP", "Use CoreNLP pipeline for NER, most precise heuristic."),
    PREFIX("prefix", "Recognizes named entities with prefixes (Gobierno de ..., Lic. , Rio ...)");

    private final String value;
    private final String description;

    HeuristicType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static HeuristicType fromString(String value) throws IllegalArgumentException {
        for (HeuristicType type : HeuristicType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown heuristic value");
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
