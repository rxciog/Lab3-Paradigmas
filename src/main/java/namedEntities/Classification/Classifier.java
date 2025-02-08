package namedEntities.Classification;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

import utils.JSONParser;
import namedEntities.NamedEntity;
import scala.Tuple2;

public class Classifier implements Serializable {
    
    public List<NamedEntity> runClassifier(JavaRDD<String> entities, SparkSession spark){
        List<NamedEntity> result = new ArrayList<>();
        List<NamedEntity> entitiesInDict = new ArrayList<>();

        try {
            entitiesInDict = JSONParser.parseJsonDict("/data/dictionary.json");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        JavaPairRDD<String, Integer> ones = entities.mapToPair(s -> new Tuple2<>(s, 1));
        JavaPairRDD<String, Integer> countedEntities = ones.reduceByKey((i1, i2) -> i1 + i2);

        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
        Broadcast<List<NamedEntity>> broadcastDict = jsc.broadcast(entitiesInDict);

        result = countedEntities.mapPartitions(partition -> {
                return classifyEntities(partition, broadcastDict.value()).iterator();
            }).collect();

        
        jsc.close();
        return result;
    }

    private static List<NamedEntity>  classifyEntities (Iterator<Tuple2<String, Integer>> entities, List<NamedEntity> entitiesInDict){
        List<NamedEntity> result = new ArrayList<>();
        List<Tuple2<String, Integer>> entitiesList = new ArrayList<>();
        entities.forEachRemaining(entitiesList::add);

        // Distributed: classify list of entities
        boolean added = false;
        // Classify each entity
        for (Tuple2<String, Integer> entity: entitiesList){
            for (NamedEntity ne: entitiesInDict){
                if (ne.getKeywords().contains(entity._1())){
                    ne.setMentions(entity._2());
                    result.add(ne);
                    added = true;
                    break;
                }
            }
            if (!added){
                result.add(new NamedEntity(entity._1(), Category.OTHER, List.of(Topic.OTHER), entity._2(), List.of(entity._1()) ));
            }
            added = false;
        }

        return result;

    }

    
}