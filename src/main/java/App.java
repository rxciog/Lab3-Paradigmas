import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import feed.Article;
import feed.FeedParser;
import feed.FeedType;
import namedEntities.NamedEntity;
import namedEntities.Classification.Classifier;
import namedEntities.heuristics.Heuristic;
import namedEntities.heuristics.HeuristicFactory;
import namedEntities.heuristics.HeuristicType;
import namedEntities.stats.Stats;
import namedEntities.stats.StatsFormat;
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

        try {
            Config config = ui.handleInput(args);
            run(config, feedsDataArray);
        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
            UserInterface.printHelp(feedsDataArray);
            System.exit(1);
        }

    }

    private static void run(Config config, List<FeedsData> feedsDataArray) {

        if (feedsDataArray == null || feedsDataArray.size() == 0) {
            System.out.println("No se encontraron feeds");
            return;
        }
        if (config.getHelp()){
            UserInterface.printHelp(feedsDataArray);
            return;
        }

        // Guardamos en allArticles los artículos de los feeds correspondientes
        List<String> feedList = extractURL(config, feedsDataArray);
        if (feedList.isEmpty()){
            System.out.println("No se encontró el feed especificado... ");
            return;
        }
        List<Article> allArticles = getAllArticles(feedList);

        if (config.getPrintFeed()) {printFeed(allArticles);}


        if (config.getComputeNamedEntities() && config.getHeuristic().isPresent()) {
            //Creamos el punto de entrada para poder usar la API de Spark
            SparkSession spark = SparkSession.builder().appName("NER&Classification").getOrCreate();
            JavaRDD<String> articles = loadArticles(allArticles, spark);

            JavaRDD<String> namedEntities = computeNamedEntities(articles, config.getHeuristic().get(), spark);
            List<NamedEntity> entitiesList = computeStats(namedEntities, spark);
            printStats(entitiesList, config.getStats().get());

            spark.close();
        }

    }

    private static List<String> extractURL (Config config, List<FeedsData> feedsDataArray) {
        List<String> urList = new ArrayList<>();

        for (FeedsData feed : feedsDataArray){
            if (feed.getLabel().equals(config.getFeedKey().getValue()) || config.getFeedKey().equals(FeedType.ALL)){
                urList.add(feed.getUrl());
                break;
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

    private static void printFeed(List<Article> allArticles){
        System.out.println("Printing feed(s): ");
        for (Article article : allArticles) {
            article.print();
        }
    }

    private static JavaRDD<String> loadArticles(List<Article> allArticles, SparkSession spark){
        String path = "./src/main/resources/bigData.txt";
        createFeedFile(allArticles, path);
        JavaRDD<String> articles = spark.read().textFile("./src/main/resources/wiki_dump_parcial.txt").javaRDD();
        //JavaRDD<String> articles = spark.read().textFile(path).javaRDD();
        return articles;
    }

    private static JavaRDD<String> computeNamedEntities (JavaRDD<String> allArticles, HeuristicType heuristicName, SparkSession spark){
        // TODO: complete the message with the selected heuristic name
        System.out.println("Computing named entities using " + heuristicName);
        JavaRDD<String> namedEntities = null;

        try {
            Heuristic heuristic = HeuristicFactory.createHeuristic(heuristicName);
            namedEntities = allArticles.flatMap(a -> heuristic.extractCandidates(a).iterator());
                                        
        } catch (IllegalArgumentException e ){
            UserInterface.printHeuristicHelpMssg();
            System.exit(1);
        }
        return namedEntities; 
    }

    private static void createFeedFile(List<Article> allArticles , String strPath){
        Path path = Paths.get(strPath);

        for ( Article article : allArticles){
            try {
                Files.write(path, article.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private static List<NamedEntity> computeStats(JavaRDD<String> namedEntities, SparkSession spark){
        // TODO: compute named entities using the selected heuristic
        System.out.println("Computing stats on named entities...");

        List<NamedEntity> entities = Classifier.classifyEntitiesWithSpark(namedEntities, spark);

        return entities;
    }

    private static void printStats(List<NamedEntity> entities, StatsFormat statType ){
        // TODO: Print stats
        System.out.println("\nStats: ");
        if ( statType.equals(StatsFormat.TOP)){
            stat.getStatsByTopic(entities);
        } else {
            if (!statType.equals(StatsFormat.CAT)) System.out.println("Defaulting to stats by category ...");
            stat.getStatsByCategory(entities);
        }
        System.out.println("-".repeat(80));
    }
}
