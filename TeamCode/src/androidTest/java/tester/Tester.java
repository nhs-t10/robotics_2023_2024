package tester;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Tester {
    public static void main(String[] args) {
        try (InputStream resourceAsStream = Tester.class.getResourceAsStream("/dataset.json"); InputStreamReader reader = new InputStreamReader(resourceAsStream)) {
            JsonObject object = new Gson().fromJson(reader, JsonObject.class);

            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                String file = entry.getKey();
                String color = entry.getValue().getAsJsonObject().get("color").getAsString();
                String side = entry.getValue().getAsJsonObject().get("side").getAsString();
                System.out.println(file + " " + color + " " + side);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
