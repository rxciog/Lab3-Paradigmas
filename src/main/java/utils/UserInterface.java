package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import namedEntities.heuristics.HeuristicFactory;

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

    public Config handleInput(String[] args) {

        for (Integer i = 0; i < args.length; i++) {
            for (Option option : options) {
                if (option.getName().equals(args[i]) || option.getLongName().equals(args[i])) {
                    if (option.getnumValues() == 0) {
                        optionDict.put(option.getName(), null);
                    } else {
                        if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                            optionDict.put(option.getName(), args[i + 1]);
                            i++;
                        } 
                        
                    }
                }
            }
        }
        // 1
        Boolean help = optionDict.containsKey("-h");
        Boolean printFeed = optionDict.containsKey("-pf");
        Boolean computeNamedEntities = optionDict.containsKey("-ne");
        // TODO: use value for heuristic config
        String heuristic = (computeNamedEntities) ? getHeuristic() : "None";
        String feedKey = (optionDict.get("-f") == null ) ? "All" : optionDict.get("-f");
        String stats = (optionDict.get("-sf") == null) ? "None" : optionDict.get("-sf");


        if (!computeNamedEntities && !printFeed) printFeed = true;

        return new Config(help , printFeed, computeNamedEntities, feedKey, heuristic, stats);
    }

    private String getHeuristic(){
        String heuristicName = optionDict.get("-ne");

        if (!HeuristicFactory.getAvailableHeuristics().contains(heuristicName)){
            printHeuristicErrorMssg();
            System.exit(1);
        }

        return heuristicName;
    }


    public void printHelp(List<FeedsData> feedsDataArray) {
        System.out.println("Usage: make run ARGS=\"[OPTION]\"");
        System.out.println("Options:");
        System.out.println("  -h, --help: Show this help message and exit");
        System.out.println("  -f, --feed <feedKey>:                Fetch and process the feed with");
        System.out.println("                                       the specified key");
        System.out.println("                                       Available feed keys are: ");
        for (FeedsData feedData : feedsDataArray) {
            System.out.println("                                       " + feedData.getLabel());
        }
        printHeuristicErrorMssg();
        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        System.out.println("                                       cat: Category-wise stats");
        System.out.println("                                       topic: Topic-wise stats");
    }

    public void printHeuristicErrorMssg() {

        System.out.println("  -ne, --named-entity                 : Use the specified heuristic to extract");
        System.out.println("                                       named entities");
        System.out.println("                                       Available heuristic names are: ");
        System.out.println("                                       capital: <description>");
        System.out.println("                                       +capital: <description>");
        System.out.println("                                       coreNLP: <description>");

    }
}
