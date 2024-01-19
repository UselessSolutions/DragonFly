package useless.dragonfly.model.blockstates.adapters;

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
import useless.dragonfly.model.blockstates.data.BlockStateData;
import useless.dragonfly.model.blockstates.data.ModelPart;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BlockStateJsonAdapter implements JsonDeserializer<BlockStateData>, JsonSerializer<BlockStateData> {
	@Override
	public BlockStateData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		BlockStateData data = null;

		if (obj.has("variants")){
			Map<String, JsonElement> rawVariantData = obj.getAsJsonObject("variants").asMap();
			Map<String, ModelPart> v = new HashMap<>();
			for (Map.Entry<String, JsonElement> e : rawVariantData.entrySet()){
				v.put(e.getKey(), DragonFly.GSON.fromJson(e.getValue(), ModelPart.class));
			}
			return new BlockStateData(v);

		} else if (obj.has("multipart")){
			JsonArray arr = obj.getAsJsonArray("multipart");
			ModelPart[] parts = new ModelPart[arr.size()];
			for (int i = 0; i < arr.size(); i++) {
				parts[i] = DragonFly.GSON.fromJson(arr.get(i), ModelPart.class);
			}
			return new BlockStateData(parts);
		}
		throw new IllegalArgumentException("Blockstate is missing a multipart or a variants! " + json.getAsString());
	}

	@Override
	public JsonElement serialize(BlockStateData src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
