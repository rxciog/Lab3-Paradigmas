package utils;

public class Config {
    private boolean printFeed = false;
    private boolean computeNamedEntities = false;
    private String feedKey;
    private boolean help = false;
    // TODO: A reference to the used heuristic will be needed here
    private String heuristic;
    private String stats;

    public Config(boolean help, boolean printFeed, boolean computeNamedEntities, String feedKey, String heuristic, String stats) {
        this.help = help;
        this.printFeed = printFeed;
        this.computeNamedEntities = computeNamedEntities;
        this.feedKey = feedKey;
        this.heuristic = heuristic;
        this.stats = stats;
    }

    public boolean getHelp() {
        return help;
    }

    public boolean getPrintFeed() {
        return printFeed;
    }

    public boolean getComputeNamedEntities() {
        return computeNamedEntities;
    }

    public String getFeedKey() {
        return feedKey;
    }

    public String getHeuristic() {
        return heuristic;
    }

    public String getStats() {
        return stats;
    }
}
