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
import useless.dragonfly.model.entity.processor.BenchEntityCube;
import useless.dragonfly.utilities.vector.Vector3f;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BenchEntityBonesJsonAdapter implements JsonDeserializer<BenchEntityBones>, JsonSerializer<BenchEntityBones> {
	@Override
	public BenchEntityBones deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		List<BenchEntityCube> cubes = new ArrayList<>();
		String parent = null;
		String name = null;
		Vector3f pivot = null;
		Vector3f rotation = null;
		boolean mirror = false;
		if (obj.has("cubes")){
			JsonArray arr = obj.getAsJsonArray("cubes");
			for (JsonElement e : arr){
				cubes.add(DragonFly.GSON.fromJson(e, BenchEntityCube.class));
			}
		}
		if (obj.has("name")) name = obj.get("name").getAsString();
		if (obj.has("parent")) parent = obj.get("parent").getAsString();
		if (obj.has("pivot")) pivot = DragonFly.GSON.fromJson(obj.get("pivot"), Vector3f.class);
		if (obj.has("rotation")) rotation = DragonFly.GSON.fromJson(obj.get("rotation"), Vector3f.class);
		if (obj.has("mirror")) mirror = obj.get("mirror").getAsBoolean();

		return new BenchEntityBones(cubes, name, pivot, rotation, parent, mirror);
	}

	@Override
	public JsonElement serialize(BenchEntityBones src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
