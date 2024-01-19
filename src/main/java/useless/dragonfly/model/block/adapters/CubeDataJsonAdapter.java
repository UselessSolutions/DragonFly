package useless.dragonfly.model.block.adapters;

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
import useless.dragonfly.model.block.data.FaceData;
import useless.dragonfly.model.block.data.RotationData;
import useless.dragonfly.utilities.Utilities;

import java.lang.reflect.Type;
import java.util.Map;

public class CubeDataJsonAdapter implements JsonDeserializer<CubeData>, JsonSerializer<CubeData> {
	@Override
	public CubeData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		CubeData data = new CubeData();

		if (obj.has("from")) data.from = Utilities.floatArrFromJsonArr(obj.getAsJsonArray("from"));
		if (obj.has("to")) data.to = Utilities.floatArrFromJsonArr(obj.getAsJsonArray("to"));
		if (obj.has("rotation")) data.rotation = DragonFly.GSON.fromJson(obj.getAsJsonObject("rotation"), RotationData.class);
		if (obj.has("shade")) data.shade = obj.get("shade").getAsBoolean();
		if (obj.has("faces")){
			for (Map.Entry<String, JsonElement> e : obj.getAsJsonObject("faces").asMap().entrySet()){
				data.faces.put(e.getKey(), DragonFly.GSON.fromJson(e.getValue(), FaceData.class));
			}
		}
		return data;
	}

	@Override
	public JsonElement serialize(CubeData src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
