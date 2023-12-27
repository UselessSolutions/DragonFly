package useless.dragonfly.model.item.data;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ItemModelData {
	@SerializedName("parent")
	public String parent = null;
	@SerializedName("textures")
	public HashMap<String, String> textures = new HashMap<>();
}
