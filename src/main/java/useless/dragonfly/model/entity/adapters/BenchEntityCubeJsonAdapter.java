package useless.dragonfly.model.entity.adapters;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.NotImplementedException;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.entity.processor.BenchEntityCube;
import useless.dragonfly.model.entity.processor.BenchFaceUVsItem;
import useless.dragonfly.utilities.vector.Vector3f;

import java.lang.reflect.Type;
import java.util.List;

public class BenchEntityCubeJsonAdapter implements JsonDeserializer<BenchEntityCube>, JsonSerializer<BenchEntityCube> {
	@Override
	public BenchEntityCube deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Vector3f origin = null;
		Vector3f pivot = null;
		Vector3f rotation = null;
		Vector3f size = null;
		float inflate = 0;
		List<Float> uv = null;
		BenchFaceUVsItem faceUv = null;
		Boolean mirror = null;
		if (json.isJsonObject()) {
			JsonObject obj = json.getAsJsonObject();

			if (obj.has("inflate")) inflate = obj.get("inflate").getAsFloat();
			if (obj.has("size")) size = DragonFly.GSON.fromJson(obj.get("size"), Vector3f.class);
			if (obj.has("origin")) origin = DragonFly.GSON.fromJson(obj.get("origin"), Vector3f.class);
			if (obj.has("rotation")) rotation = DragonFly.GSON.fromJson(obj.get("rotation"), Vector3f.class);
			if (obj.has("pivot")) pivot = DragonFly.GSON.fromJson(obj.get("pivot"), Vector3f.class);

			JsonElement uvElement = obj.get("uv");
			if (uvElement.isJsonArray()) {
				uv = Lists.newArrayList();
				JsonArray array = uvElement.getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					uv.add(array.get(i).getAsFloat());
				}
			}
			if (uvElement.isJsonObject()) {
				faceUv = new Gson().fromJson(uvElement, BenchFaceUVsItem.class);
			}

			JsonElement mirrorElement = obj.get("mirror");
			if (mirrorElement != null && mirrorElement.isJsonPrimitive()) {
				JsonPrimitive primitive = mirrorElement.getAsJsonPrimitive();
				if (primitive.isBoolean()) {
					mirror = primitive.getAsBoolean();
				}
			}
		}
		return new BenchEntityCube(origin, pivot, rotation, size, inflate, uv, faceUv, mirror);
	}

	@Override
	public JsonElement serialize(BenchEntityCube src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
