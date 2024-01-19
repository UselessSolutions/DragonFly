package useless.dragonfly.model.block.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.NotImplementedException;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.block.data.CubeData;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.model.block.data.PositionData;

import java.lang.reflect.Type;
import java.util.Map;

public class ModelDataJsonAdapter implements JsonDeserializer<ModelData>, JsonSerializer<ModelData> {
	@Override
	public ModelData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		ModelData data = new ModelData();
		if (obj.has("parent")) data.parent = obj.get("parent").getAsString();
		if (obj.has("ambientocclusion")) data.ambientocclusion = obj.get("ambientocclusion").getAsBoolean();
		if (obj.has("display")){
			for (Map.Entry<String, JsonElement> e : obj.getAsJsonObject("display").asMap().entrySet()){
				data.display.put(e.getKey(), DragonFly.GSON.fromJson(e.getValue(), PositionData.class));

			}
		}
		if (obj.has("textures")){
			for (Map.Entry<String, JsonElement> e : obj.getAsJsonObject("textures").asMap().entrySet()){
				data.textures.put(e.getKey(), e.getValue().getAsString());
			}
		}
		if (obj.has("elements")){
			JsonArray arr = obj.getAsJsonArray("elements");
			data.elements = new CubeData[arr.size()];
			for (int i = 0; i < arr.size(); i++){
				data.elements[i] = DragonFly.GSON.fromJson(arr.get(i), CubeData.class);
			}
		}
		return data;
	}

	@Override
	public JsonElement serialize(ModelData src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
