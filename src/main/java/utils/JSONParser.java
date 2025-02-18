package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import namedEntities.NamedEntity;
import namedEntities.Classification.Category;
import namedEntities.Classification.Topic;
import namedEntities.NamedEntityFactory;

public class JSONParser {

    static public List<FeedsData> parseJsonFeedsData(String jsonFilePath) throws IOException {
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        List<FeedsData> feedsList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String label = jsonObject.getString("label");
            String url = jsonObject.getString("url");
            String type = jsonObject.getString("type");
            feedsList.add(new FeedsData(label, url, type));
        }
        return feedsList;
    }

    static public Set<String> parseJsonList(InputStream inputStream) throws IOException {
        String jsonData = "";
        try ( BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))){
            jsonData = buffer.lines().collect(Collectors.joining("\n"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        Set<String> list = new HashSet<>();

        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            String word = jsonArray.getString(i);
            list.add(word);
        }
        return list;
    }

    static public List<NamedEntity> parseJsonDict(String filepath) throws IOException {
        InputStream inputStream = JSONParser.class.getResourceAsStream(filepath);
        String jsonData = "";
        
        //Leemos todas las líneas del stream de input y lo guardamos en un solo string
        try ( BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))){
            jsonData = buffer.lines().collect(Collectors.joining("\n"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        //Convertimos el string en un arreglo json con el cual podemos trabajar
        JSONArray jsonArray = new JSONArray(jsonData);
        List<NamedEntity> dict = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            //Para cada objeto del arreglo extraemos los valores de los campos que nos interesan
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String label = jsonObject.getString("label");
            Category category = Category.valueOf(jsonObject.getString("Category"));

            JSONArray array = jsonObject.getJSONArray("Topics");
            List<Topic> topics = new ArrayList<>();
            for (int j = 0; j < array.length(); j++){
                topics.add(Topic.valueOf(array.getString(j)));
            }

            array = jsonObject.getJSONArray("keywords");
            List<String> keywords = new ArrayList<>();
            for (int j = 0; j < array.length(); j++){
                keywords.add(array.getString(j));
            }

            dict.add(NamedEntityFactory.createNamedEntity(label, category, topics, 1, keywords));

        }

        return dict;
    }
}
