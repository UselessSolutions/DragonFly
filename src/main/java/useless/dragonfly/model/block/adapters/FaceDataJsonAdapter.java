package useless.dragonfly.model.block.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import useless.dragonfly.model.block.data.FaceData;
import useless.dragonfly.utilities.Utilities;

import java.lang.reflect.Type;

public class FaceDataJsonAdapter implements JsonDeserializer<FaceData>, JsonSerializer<FaceData> {
	@Override
	public FaceData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		FaceData data = new FaceData();

		if (obj.has("uv")) data.uv = Utilities.doubleArrFromJsonArr(obj.getAsJsonArray("uv"));
		if (obj.has("texture")) data.texture = obj.get("texture").getAsString();
		if (obj.has("cullface")) data.cullface = obj.get("cullface").getAsString();
		if (obj.has("rotation")) data.rotation = obj.get("rotation").getAsInt();
		if (obj.has("tintindex")) data.tintindex = obj.get("tintindex").getAsInt();
		if (obj.has("fullbright")) data.fullbright = obj.get("fullbright").getAsBoolean();

		return data;
	}

	@Override
	public JsonElement serialize(FaceData src, Type typeOfSrc, JsonSerializationContext context) {
		return null;
	}
}
