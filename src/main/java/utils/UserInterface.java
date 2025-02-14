package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import feed.FeedType;
import namedEntities.heuristics.HeuristicType;
import namedEntities.stats.StatsFormat;

public class UserInterface {

    private HashMap<String, String> optionDict;

    private List<Option> options;

    public UserInterface() {
        options = new ArrayList<Option>();
        options.add(new Option("-h", "--help", 0));
        options.add(new Option("-f", "--feed", 1));
        options.add(new Option("-ne", "--named-entity", 1));
        options.add(new Option("-pf", "--print-feed", 0));
        options.add(new Option("-sf", "--stats-format", 1));

        optionDict = new HashMap<String, String>();
    }

    public Config handleInput(String[] args) throws IllegalArgumentException {

        for (Integer i = 0; i < args.length; i++) {
            boolean valid = false;
            for (Option option : options) {
                if (option.getName().equals(args[i]) || option.getLongName().equals(args[i])) {
                    valid = true;
                    if (option.getnumValues() == 0) {
                        optionDict.put(option.getName(), null);
                    } else {
                        if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                            optionDict.put(option.getName(), args[i + 1]);
                            i++;
                        }
                    }
                    break;
                }
            }
            if (!valid) {
                throw new IllegalArgumentException("Invalid option: " + args[i]);
            }
        }
        if (optionDict.isEmpty()){
            throw new IllegalArgumentException("No valid arguments provided.");
        }

        Boolean help = optionDict.containsKey("-h");
        Boolean printFeed = optionDict.containsKey("-pf");
        Boolean computeNamedEntities = optionDict.containsKey("-ne");

        if (!computeNamedEntities && optionDict.containsKey("-sf") ) throw new IllegalArgumentException("Stats format specified without named entities computation.");
        
        HeuristicType heuristic = (computeNamedEntities) ? getHeuristic(optionDict.get("-ne")) : HeuristicType.NONE;
        FeedType feedKey = (optionDict.get("-f") == null ) ? FeedType.ALL : getFeedKey(optionDict.get("-f"));
        StatsFormat stats = (optionDict.get("-sf") == null) ?  StatsFormat.NONE: getStatForm(optionDict.get("-sf"));

        if (!computeNamedEntities && !printFeed) printFeed = true;

        return new Config(help , printFeed, computeNamedEntities, feedKey, heuristic, stats);
    }


    private static HeuristicType getHeuristic(String heuristicName){
        HeuristicType heuristic = HeuristicType.NONE;

        try {
            heuristic = HeuristicType.fromString(heuristicName);
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong heuristic name");
            printHeuristicHelpMssg();
            System.exit(1);
        }
        return heuristic;
    }

    private static FeedType getFeedKey(String feedName){
        FeedType feedKey = FeedType.ALL;

        try {
            feedKey = FeedType.fromString(feedName);
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong feed name: " +feedName + ", use -h for help");
            System.exit(1);
        }
        return feedKey;
    }

    private static StatsFormat getStatForm(String statName){
        StatsFormat statsFormat = StatsFormat.NONE;

        try {
            statsFormat = StatsFormat.valueOf(statName.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong stats name, use -h for help");
            System.exit(1);
        }
        return statsFormat;
    }

    public static void printHelp(List<FeedsData> feedsDataArray) {
        System.out.println("Usage: make run ARGS=\"[OPTION]\"");
        System.out.println("Options:");
        System.out.println("  -h, --help: Show this help message and exit");
        printFeedHelpMssg(feedsDataArray);
        printHeuristicHelpMssg();
        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        printStatsHelpMssg();
        
    }

    public static void printHeuristicHelpMssg() {
        System.out.println("  -ne, --named-entity                 : Use the specified heuristic to extract");
        System.out.println("                                       named entities");
        System.out.println("                                       Available heuristic names are: ");
        for (HeuristicType hType: HeuristicType.values()) {
            System.out.println("                                       " + hType.getValue() + 
                                ":" + hType.getDescription()
                              );
        }

    }

    public static void printFeedHelpMssg(List<FeedsData> feedsDataArray) {
        System.out.println("  -f, --feed <feedKey>:                Fetch and process the feed with");
        System.out.println("                                       the specified key");
        System.out.println("                                       Available feed keys are: ");
        for (FeedsData feedData : feedsDataArray) {
            System.out.println("                                       " + feedData.getLabel());
        }
    }

    public static void printStatsHelpMssg() {
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        for (StatsFormat sFormat: StatsFormat.values()) {
            System.out.println("                                       " + sFormat.getValue() + 
                                ":" + sFormat.getDescription()
                              );
        }
    }

}