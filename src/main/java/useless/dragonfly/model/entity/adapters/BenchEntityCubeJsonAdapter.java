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
import useless.dragonfly.model.entity.processor.BenchEntityCube;
import useless.dragonfly.model.entity.processor.BenchFaceUVsItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BenchEntityCubeJsonAdapter implements JsonDeserializer<BenchEntityCube>, JsonSerializer<BenchEntityCube> {
	@Override
	public BenchEntityCube deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		List<Float> origin = null;
		List<Float> pivot = null;
		List<Float> rotation = null;
		List<Float> size = null;
		float inflate = 0;
		List<Float> uv = null;
		BenchFaceUVsItem faceUv = null;
		Boolean mirror = null;
		if (json.isJsonObject()) {
			JsonObject obj = json.getAsJsonObject();

			if (obj.has("inflate")) inflate = obj.get("inflate").getAsFloat();
			if (obj.has("size")){
				JsonArray arr = obj.getAsJsonArray("size");
				size = new ArrayList<>();
				for (JsonElement e : arr){
					size.add(e.getAsFloat());
				}
			}
			if (obj.has("origin")){
				JsonArray arr = obj.getAsJsonArray("origin");
				origin = new ArrayList<>();
				for (JsonElement e : arr){
					origin.add(e.getAsFloat());
				}
			}
			if (obj.has("rotation")){
				JsonArray arr = obj.getAsJsonArray("rotation");
				rotation = new ArrayList<>();
				for (JsonElement e : arr){
					rotation.add(e.getAsFloat());
				}
			}
			if (obj.has("pivot")){
				JsonArray arr = obj.getAsJsonArray("pivot");
				pivot = new ArrayList<>();
				for (JsonElement e : arr){
					pivot.add(e.getAsFloat());
				}
			}

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
