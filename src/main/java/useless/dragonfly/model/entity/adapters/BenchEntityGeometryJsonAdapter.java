package useless.dragonfly.model.entity.adapters;

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
import useless.dragonfly.model.entity.processor.BenchEntityBones;
import useless.dragonfly.model.entity.processor.BenchEntityGeometry;
import useless.dragonfly.model.entity.processor.Description;

import java.lang.reflect.Type;

public class BenchEntityGeometryJsonAdapter implements JsonDeserializer<BenchEntityGeometry>, JsonSerializer<BenchEntityGeometry> {
	@Override
	public BenchEntityGeometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		Description description = null;
		if (obj.has("description")) description = DragonFly.GSON.fromJson(obj.getAsJsonObject("description"), Description.class);
		BenchEntityBones[] bones = null;
		if (obj.has("bones")){
			JsonArray arr = obj.getAsJsonArray("bones");
			bones = new BenchEntityBones[arr.size()];
			for (int i = 0; i < bones.length; i++) {
				bones[i] = DragonFly.GSON.fromJson(arr.get(i), BenchEntityBones.class);
			}
		}
		return new BenchEntityGeometry(description, bones);
	}

	@Override
	public JsonElement serialize(BenchEntityGeometry src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
