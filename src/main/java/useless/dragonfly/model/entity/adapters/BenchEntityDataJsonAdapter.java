package useless.dragonfly.model.entity.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.NotImplementedException;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.entity.processor.BenchEntityModelData;
import useless.dragonfly.model.entity.processor.BenchEntityGeometry;

import java.lang.reflect.Type;

public class BenchEntityDataJsonAdapter implements JsonDeserializer<BenchEntityModelData>, JsonSerializer<BenchEntityModelData> {
	@Override
	public BenchEntityModelData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		BenchEntityModelData data = new BenchEntityModelData();
		if (obj.has("format_version")) data.formatVersion = obj.get("format_version").getAsString();
		if (data.formatVersion.equals("1.10.0")) throw new IllegalArgumentException("DragonFly does not support legacy entity formats like 1.10.0!");
		if (obj.has("minecraft:geometry")) {
			for (JsonElement e : obj.getAsJsonArray("minecraft:geometry")){
				data.benchEntityGeometry.add(DragonFly.GSON.fromJson(e, BenchEntityGeometry.class));
			}
		}
		return data;
	}

	@Override
	public JsonElement serialize(BenchEntityModelData src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
