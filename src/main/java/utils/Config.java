package utils;

import feed.FeedType;
import namedEntities.heuristics.HeuristicType;
import namedEntities.stats.StatsFormat;

public class Config {
    private boolean printFeed = false;
    private boolean computeNamedEntities = false;
    private FeedType feedKey;
    private boolean help = false;
    private HeuristicType heuristic;
    private StatsFormat stats;

    public Config(boolean help, boolean printFeed, boolean computeNamedEntities, FeedType feedKey, HeuristicType heuristic, StatsFormat stats) {
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

    public FeedType getFeedKey() {
        return feedKey;
    }

    public HeuristicType getHeuristic() {
        return heuristic;
    }

    public StatsFormat getStats() {
        return stats;
    }
}
