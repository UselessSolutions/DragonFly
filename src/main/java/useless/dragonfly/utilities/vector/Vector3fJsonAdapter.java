package useless.dragonfly.utilities.vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.NotImplementedException;

import java.lang.reflect.Type;

public class Vector3fJsonAdapter implements JsonDeserializer<Vector3f>, JsonSerializer<Vector3f> {
	@Override
	public Vector3f deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (json.isJsonArray()){
			JsonArray arr = json.getAsJsonArray();
			return new Vector3f(arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat());
		}
		return null;
	}

	@Override
	public JsonElement serialize(Vector3f src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
