package useless.dragonfly.model.entity.animation;

import com.google.common.collect.Maps;
import com.google.gson.*;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class AnimationDeserializer implements JsonDeserializer<Animation> {
	@Override
	public Animation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Map<String, AnimationData> animations = Maps.newHashMap();

		if (json.isJsonObject()) {
			JsonObject obj = (JsonObject) json;
			if (obj.has("animations")) {
				for (Map.Entry<String, JsonElement> entry : obj.getAsJsonObject("animations").entrySet()) {
					animations.put(entry.getKey(), makeAnimation((JsonObject) entry.getValue()));
				}
			}
			return new Animation(animations);
		}
		return new Animation(animations);
	}


	private AnimationData makeAnimation(JsonObject object) {
		boolean loop = object.has("loop") && object.getAsJsonPrimitive("loop").getAsBoolean();

		float length = object.has("animation_length") ? object.getAsJsonPrimitive("animation_length").getAsFloat() : 0;
		Map<String, BoneData> boneMap = object.has("bones") ? makeBoneMap(object.getAsJsonObject("bones")) : Maps.newHashMap();

		return new AnimationData(loop, length, boneMap);
	}

	private Map<String, BoneData> makeBoneMap(JsonObject object) {
		Map<String, BoneData> boneMap = Maps.newHashMap();
		for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
			boneMap.put(entry.getKey(), makeBone(entry.getValue().getAsJsonObject()));
		}
		return boneMap;
	}

	private BoneData makeBone(JsonObject object) {
		Map<String, PostData> rotateMap = object.has("rotation") ? makePostMap(object.get("rotation")) : Maps.newHashMap();
		Map<String, PostData> positionMap = object.has("position") ? makePostMap(object.get("position")) : Maps.newHashMap();

		return new BoneData(rotateMap, positionMap);
	}

	private Map<String, PostData> makePostMap(JsonElement element) {
		Map<String, PostData> postMap = Maps.newHashMap();
		if (element instanceof JsonArray) {
			List<Float> floats = Lists.newArrayList();
			floats.add(element.getAsJsonArray().get(0).getAsFloat());
			floats.add(element.getAsJsonArray().get(1).getAsFloat());
			floats.add(element.getAsJsonArray().get(2).getAsFloat());
			postMap.put("0", new PostData(floats, "catmullrom"));
			return postMap;
		}
		if (element instanceof JsonObject) {
			for (Map.Entry<String, JsonElement> entry : ((JsonObject) element).entrySet()) {
				postMap.put(entry.getKey(), makePost(entry.getValue()));
			}
		}
		return postMap;
	}

	private PostData makePost(JsonElement element) {
		List<Float> list = Lists.newArrayList();
		if (element instanceof JsonPrimitive) {
			JsonArray array = new JsonArray(3);

			array.add(element);
			array.add(element);
			array.add(element);

			element = array;
		}

		if (element instanceof JsonArray) {
			list.add(0, ((JsonArray) element).get(0).getAsFloat());
			list.add(1, ((JsonArray) element).get(1).getAsFloat());
			list.add(2, ((JsonArray) element).get(2).getAsFloat());
			return new PostData(list, "catmullrom");
		}

		if (element instanceof JsonObject) {
			JsonObject jsonObject = (JsonObject) element;
			list.add(0, jsonObject.getAsJsonArray("post").get(0).getAsFloat());
			list.add(1, jsonObject.getAsJsonArray("post").get(1).getAsFloat());
			list.add(2, jsonObject.getAsJsonArray("post").get(2).getAsFloat());
			return new PostData(list, jsonObject.getAsJsonPrimitive("lerp_mode").getAsString());
		}
		return null;
	}
}
