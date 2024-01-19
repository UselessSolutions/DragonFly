package useless.dragonfly.model.blockstates.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.NotImplementedException;
import useless.dragonfly.model.blockstates.data.VariantData;

import java.lang.reflect.Type;

public class VariantDataJsonAdapter implements JsonDeserializer<VariantData>, JsonSerializer<VariantData> {
	@Override
	public VariantData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		VariantData data = new VariantData();
		if (obj.has("model")) data.model = obj.get("model").getAsString();
		if (obj.has("x")) data.x = obj.get("x").getAsInt();
		if (obj.has("y")) data.y = obj.get("y").getAsInt();
		if (obj.has("uvlock")) data.uvlock = obj.get("uvlock").getAsBoolean();
		if (obj.has("weight")) data.weight = obj.get("weight").getAsInt();
		return data;
	}

	@Override
	public JsonElement serialize(VariantData src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
