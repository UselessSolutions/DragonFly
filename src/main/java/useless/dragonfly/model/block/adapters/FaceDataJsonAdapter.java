package useless.dragonfly.model.block.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.model.block.data.FaceData;
import useless.dragonfly.utilities.Utilities;

import java.lang.reflect.Type;
import java.util.HashMap;

public class FaceDataJsonAdapter implements JsonDeserializer<FaceData>, JsonSerializer<FaceData> {
	public static final HashMap<String, Side> keyToSide = new HashMap<>();
	public static final HashMap<Side, String> sideToKey = new HashMap<>();
	private static void registerSide(Side side, String key){
		keyToSide.put(key, side);
		sideToKey.put(side, key);
	}
	static {
		registerSide(Side.BOTTOM, "down");
		registerSide(Side.TOP, "up");
		registerSide(Side.NORTH, "north");
		registerSide(Side.SOUTH, "south");
		registerSide(Side.WEST, "west");
		registerSide(Side.EAST, "east");
	}
	@Override
	public FaceData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		FaceData data = new FaceData();

		if (obj.has("uv")) data.uv = Utilities.doubleArrFromJsonArr(obj.getAsJsonArray("uv"));
		if (obj.has("texture")) data.texture = obj.get("texture").getAsString();
		if (obj.has("cullface")) data.cullface = keyToSide.get(obj.get("cullface").getAsString());
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
