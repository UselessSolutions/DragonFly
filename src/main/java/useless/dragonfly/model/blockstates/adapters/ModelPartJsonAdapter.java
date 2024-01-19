package useless.dragonfly.model.blockstates.adapters;

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
import useless.dragonfly.model.blockstates.data.Condition;
import useless.dragonfly.model.blockstates.data.ModelPart;
import useless.dragonfly.model.blockstates.data.VariantData;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ModelPartJsonAdapter implements JsonDeserializer<ModelPart>, JsonSerializer<ModelPart> {
	@Override
	public ModelPart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		ModelPart data = new ModelPart();
		if (json.isJsonObject()){
			JsonObject obj = json.getAsJsonObject();
			if (obj.has("apply")){
				double accumulated = 0;
				if (obj.getAsJsonObject("apply").isJsonArray()){
					JsonArray arr = obj.getAsJsonArray("apply");
					data.apply = new VariantData[arr.size()];
					for (int i = 0; i < data.apply.length; i++) {
						VariantData var = DragonFly.GSON.fromJson(arr.get(i), VariantData.class);
						accumulated += var.weight;
						var.weightAccum = accumulated;
						data.apply[i] = var;
					}
				} else {
					data.apply = new VariantData[]{DragonFly.GSON.fromJson(obj.get("apply"), VariantData.class)};
				}
				data.weightAccum = accumulated;

			}
			if (obj.has("when")) {
				JsonObject when = obj.getAsJsonObject("when");
				Map<String, String> conditions = new HashMap<>();
				String id = "and";
				if (when.has("AND")){
					// is and
					JsonArray arr = when.getAsJsonArray("AND");
					for (JsonElement e : arr){
						for (Map.Entry<String, JsonElement> f : e.getAsJsonObject().asMap().entrySet()){
							conditions.put(f.getKey(), f.getValue().getAsString());
						}
					}
				} else if (when.has("OR")) {
					// is or
					id = "or";
					JsonArray arr = when.getAsJsonArray("OR");
					for (JsonElement e : arr){
						for (Map.Entry<String, JsonElement> f : e.getAsJsonObject().asMap().entrySet()){
							conditions.put(f.getKey(), f.getValue().getAsString());
						}
					}
				} else {
					// is single
					JsonObject ob = when.getAsJsonObject();
					for (Map.Entry<String, JsonElement> e : ob.asMap().entrySet()){
						conditions.put(e.getKey(), e.getValue().getAsString());
					}
				}
				data.when = new Condition(id, conditions);
			}
			if (data.apply == null){
				if (json.isJsonArray()){
					JsonArray arr = json.getAsJsonArray();
					data.apply = new VariantData[arr.size()];
					double weightAccum = 0;
					for (int i = 0; i < data.apply.length; i++) {
						data.apply[i] = DragonFly.GSON.fromJson(arr.get(i), VariantData.class);
						weightAccum += data.apply[i].weight;
						data.apply[i].weightAccum = weightAccum;
					}
					data.weightAccum = weightAccum;
				} else {
					data.apply = new VariantData[]{DragonFly.GSON.fromJson(json, VariantData.class)};
				}
			}
		} else {
			if (json.isJsonArray()){
				JsonArray arr = json.getAsJsonArray();
				data.apply = new VariantData[arr.size()];
				double weightAccum = 0;
				for (int i = 0; i < data.apply.length; i++) {
					data.apply[i] = DragonFly.GSON.fromJson(arr.get(i), VariantData.class);
					weightAccum += data.apply[i].weight;
					data.apply[i].weightAccum = weightAccum;
				}
				data.weightAccum = weightAccum;
			} else {
				data.apply = new VariantData[]{DragonFly.GSON.fromJson(json, VariantData.class)};
			}
		}


		return data;
	}

	@Override
	public JsonElement serialize(ModelPart src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
