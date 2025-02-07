package namedEntities.Classification;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import namedEntities.NamedEntity;

public class Classifier implements Serializable {
    
    public List<NamedEntity> classifyEntities(Iterator<String> entities){

        List<NamedEntity> namedEntities = new ArrayList<>();
        List<String> entitiesList = new ArrayList<>();
        entities.forEachRemaining(entitiesList::add);
        Set<String> entitiesSet = new HashSet<String>(entitiesList);

        for (String entity: entitiesSet){

            Category[] categories = Category.values();
            Category category = Category.valueOf(getInputLabel(entity, categories));

            Topic[] topics = Topic.values();
            Topic topic = Topic.valueOf(getInputLabel(entity, topics));
            
            int mentions = Collections.frequency(entitiesList, entity);

            NamedEntity ne = new NamedEntity(entity, category, List.of(topic), mentions);
            namedEntities.add(ne);
        }


        return namedEntities;
    }

    private String fetchAPIResponse(String entity, Object[] options) throws MalformedURLException, IOException, Exception {
        String response = "";
        int retryCount = 5;
        int retryDelay = 2000;

        for (int attempt = 0 ; attempt < retryCount; attempt++){
            String hf_api_key = System.getenv("HF_API_KEY");
            URL url = new URL("https://api-inference.huggingface.co/models/MoritzLaurer/mDeBERTa-v3-base-mnli-xnli");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer "+ hf_api_key);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(50000);

            List<String> strOptions = Arrays.stream(options).map(String::valueOf).collect(Collectors.toList());

            JSONObject json = new JSONObject();
            json.put("inputs", entity);
            json.put("parameters", new JSONObject().put("candidate_labels", new JSONArray(strOptions)));

            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()){
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input,0, input.length);
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                try (InputStream is = connection.getInputStream()){
                    response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                }
                return response;
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAVAILABLE){
                Thread.sleep(retryDelay);
            } else {
                throw new Exception("HTTP error code: " + connection.getResponseCode() + connection.getResponseMessage());
            }

            
        }
        throw new Exception("All attempts failed ");

    }

    private String getInputLabel(String input, Object[] options){
        String label, response = new String();

        try {
            response = fetchAPIResponse(input, options);
            
        } catch (Exception e){
            System.out.println("Exception occurred: "+ e.getMessage());
            System.out.println("Exiting ...");
            System.exit(1);
        }

        JSONObject jsonResponse = new JSONObject(response);
        JSONArray labels = jsonResponse.getJSONArray("labels");

        label = labels.getString(0);

        return label;
    }

}
