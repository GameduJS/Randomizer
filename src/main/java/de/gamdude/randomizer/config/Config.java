package de.gamdude.randomizer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final Plugin plugin;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, JsonElement> properties;
    private final File file;

    public Config(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.properties = new HashMap<>();
        this.file = getOrCreateFile(fileName);

        if(file.length() == 0)
            writeToFile("{}");
        this.loadPropertiesFromFile();
        this.loadDefaultProperties();
    }

    private void loadPropertiesFromFile() {
        try (FileReader fileReader = new FileReader(file)) {
            JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);

            for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                properties.put(entry.getKey(), entry.getValue());
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void savePropertiesToFile() {
        JsonObject jsonObject = new JsonObject();

        for(Map.Entry<String, JsonElement> entry : properties.entrySet()) {
            jsonObject.add(entry.getKey(), entry.getValue());
        }

        String json = gson.toJson(jsonObject);
        writeToFile(json);
    }

    public JsonElement getProperty(String key) {
        return this.properties.get(key);
    }

    public void addProperty(String key, Object obj) {
        JsonElement element = gson.toJsonTree(obj);
        this.properties.put(key, element);
    }

    public void removeProperty(String key) {
        properties.remove(key);
    }

    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    private File getOrCreateFile(String fileName) {
        File file = new File(plugin.getDataFolder(), "/" + fileName + ".json");
        file.getParentFile().mkdirs();
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) { throw new RuntimeException(e); }
        }
        return file;
    }

    private void writeToFile(String content) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultProperties() {
        Map<String, Object> defaultProperties = new HashMap<>();
        defaultProperties.put("itemCooldown", 15);
        defaultProperties.put("firstItemDropDelay", 5);
        defaultProperties.put("excludedItems", new Material[]{Material.CAVE_AIR, Material.VOID_AIR});
        defaultProperties.put("canBreakBlock", true);

        defaultProperties.forEach((key, object) -> {
            if(!hasProperty(key))
                addProperty(key, object);
        });
    }

}
