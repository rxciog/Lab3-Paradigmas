import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import feed.Article;
import feed.FeedParser;
import namedEntities.heuristics.Heuristic;
import namedEntities.heuristics.HeuristicFactory;
import namedEntities.Classifier;
import namedEntities.NamedEntity;
import namedEntities.Stats;
import utils.Config;
import utils.FeedsData;
import utils.JSONParser;
import utils.UserInterface;

public class App {
    private static UserInterface ui = new UserInterface();
    private static Stats stat = new Stats();

    public static void main(String[] args) {

        List<FeedsData> feedsDataArray = new ArrayList<>();
        try {
            feedsDataArray = JSONParser.parseJsonFeedsData("src/main/resources/data/feeds.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Config config = ui.handleInput(args);

        run(config, feedsDataArray);
    }

    // TODO: Change the signature of this function if needed
    // TODO: Modularizar 
    private static void run(Config config, List<FeedsData> feedsDataArray) {

        List<String> feedList = new ArrayList<>();
        List<Article> allArticles = new ArrayList<>();
        
        if (feedsDataArray == null || feedsDataArray.size() == 0) {
            System.out.println("No feeds data found");
            return;
        }
        if (config.getHelp()){
            ui.printHelp(feedsDataArray);
            return;
        }
        // Get feed URL
        feedList = extractURL(config, feedsDataArray);

        // TODO: Populate allArticles with articles from corresponding feeds
        allArticles = getAllArticles(feedList);
        
        // TODO: Print the fetched feed
        if (config.getPrintFeed()) {
            System.out.println("Printing feed(s) ");
            for (Article article : allArticles) {
                article.print();
            }
        }

        if (config.getComputeNamedEntities()) {
            List<String> namedEntities = computeNamedEntities(allArticles, config.getHeuristic());
            computeStats(namedEntities, config.getStats());
            
        }
    }

    private static List<String> extractURL (Config config, List<FeedsData> feedsDataArray) {
        
        List<String> urList = new ArrayList<>();
        if (!config.getFeedKey().equals("All")) {
            for (FeedsData feed : feedsDataArray){
                if (feed.getLabel().equals(config.getFeedKey())){
                    urList.add(feed.getUrl());
                    break;
                } 
            }
        } else {
            for (FeedsData feed : feedsDataArray){
                urList.add(feed.getUrl());
            }
        }
        return urList;
    }

    private static List<Article> getAllArticles (List<String> feedList) {

        List<Article>  allArticles = new ArrayList<>();
        try {
            for (String feedURL : feedList) {
                String feed = FeedParser.fetchFeed(feedURL);
                allArticles.addAll(FeedParser.parseXML(feed));
            }
            
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }

        return allArticles;
    }

    private static List<String> computeNamedEntities (List<Article> allArticles, String heuristicName){
        List<String> namedEntities = new ArrayList<>();
        Heuristic heuristic;

        // TODO: complete the message with the selected heuristic name
        System.out.println("Computing named entities using " + heuristicName);

        try {
            heuristic = HeuristicFactory.createHeuristic(heuristicName);
            String articles = converToString(allArticles);
            namedEntities.addAll(heuristic.extractCandidates(articles));

        } catch (IllegalArgumentException e ){
            ui.printHeuristicErrorMssg();
        }
        
        return namedEntities; 
    }

    private static String converToString(List<Article> allArticles){
        String articles = "";
        for ( Article article : allArticles){
            articles = articles + " --- " + article.toString();
        }

        return articles;
    }

    private static void computeStats(List<String> namedEntities, String statType ){
        
        // TODO: compute named entities using the selected heuristic
        Classifier assigner = new Classifier();
        List<NamedEntity> entities = assigner.classifyEntities(namedEntities);
        // TODO: Print stats
        System.out.println("\nStats: ");
        if ( statType.contentEquals("top")){
            stat.getStatsByTopic(entities);
        } else {
            if (!statType.contentEquals("cat")) System.out.println("Defaulting to stats by category ...");
            stat.getStatsByCategory(entities);
        }
        System.out.println("-".repeat(80));
    }
}
