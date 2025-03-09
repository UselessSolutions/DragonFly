package org.useless.dragonfly.data.block.mojang.state.adapter;

import com.google.gson.*;
import org.apache.commons.lang3.NotImplementedException;
import org.useless.dragonfly.data.block.mojang.state.BlockstateData;
import org.useless.dragonfly.data.block.mojang.state.ModelPart;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BlockStateJsonAdapter implements JsonDeserializer<BlockstateData>, JsonSerializer<BlockstateData> {
	@Override
	public BlockstateData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		BlockstateData data = null;

		if (obj.has("variants")){
			Map<String, JsonElement> rawAppliedData = obj.getAsJsonObject("variants").asMap();
			Map<String, ModelPart> v = new HashMap<>();
			for (Map.Entry<String, JsonElement> e : rawAppliedData.entrySet()){
				v.put(e.getKey(), BlockstateData.GSON.fromJson(e.getValue(), ModelPart.class));
			}
			return new BlockstateData(v);

		} else if (obj.has("multipart")){
			JsonArray arr = obj.getAsJsonArray("multipart");
			ModelPart[] parts = new ModelPart[arr.size()];
			for (int i = 0; i < arr.size(); i++) {
				parts[i] = BlockstateData.GSON.fromJson(arr.get(i), ModelPart.class);
			}
			return new BlockstateData(parts);
		}
		throw new IllegalArgumentException("Blockstate is missing a multipart or a variants! " + json.getAsString());
	}

	@Override
	public JsonElement serialize(BlockstateData src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
