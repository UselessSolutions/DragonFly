package org.useless.dragonfly.model.newmodels.generic.data;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.model.newmodels.generic.ModelLoadException;
import org.useless.dragonfly.model.newmodels.generic.StaticModel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StaticModelDeserializer {
    private static final Map<String, ModelDeserializer<StaticModel>> modelFormatDeserializers = new HashMap<>();
    private static final JsonDeserializer<StaticModel> mojangDeserializer = new MojangModelDeserializer();
    static {
        modelFormatDeserializers.put("bta_block_model", new BTABlockModelDeserializer());
    }
    @NotNull
    public static StaticModel decodeStream(Minecraft mc, String resourceLocation) throws ModelLoadException {
        try (JsonReader reader =  new JsonReader(new InputStreamReader(mc.texturePackList.getResourceAsStream(resourceLocation)))) {
            JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
            if (object.has("type")){ // BTA Model Format
                String type = object.get("type").getAsString().toLowerCase(Locale.ROOT);
                int version = object.get("version").getAsInt();
                JsonObject data = object.getAsJsonObject("data");
                return modelFormatDeserializers.get(type).deserialize(data, version);
            } else {
                return mojangDeserializer.deserialize(object, StaticModel.class, null);
            }
        } catch (IOException e) {
            throw new ModelLoadException("Encountered IO exception for '" + resourceLocation + "'.", e);
        } catch (JsonParseException e){
            throw new ModelLoadException("Failed to parse json for '" + resourceLocation + "'.", e);
        } catch (NullPointerException e){
            throw new ModelLoadException("No resource file for '" + resourceLocation + "' could be found", e);
        } catch (Exception e){
            throw new ModelLoadException("Could not load model for '" + resourceLocation + "'.", e);
        }
    }
}
