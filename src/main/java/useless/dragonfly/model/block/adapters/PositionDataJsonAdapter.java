package useless.dragonfly.model.block.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.NotImplementedException;
import useless.dragonfly.model.block.data.PositionData;
import useless.dragonfly.utilities.Utilities;

import java.lang.reflect.Type;

public class PositionDataJsonAdapter implements JsonDeserializer<PositionData>, JsonSerializer<PositionData> {
	@Override
	public PositionData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		double[] rotation = new double[3];
		double[] translation = new double[3];
		double[] scale = new double[]{1,1,1};
		if (obj.has("rotation")) rotation = Utilities.doubleArrFromJsonArr(obj.getAsJsonArray("rotation"));
		if (obj.has("translation")) translation = Utilities.doubleArrFromJsonArr(obj.getAsJsonArray("translation"));
		if (obj.has("scale")) scale = Utilities.doubleArrFromJsonArr(obj.getAsJsonArray("scale"));
		return new PositionData(rotation, translation, scale);
	}
	@Override
	public JsonElement serialize(PositionData src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
