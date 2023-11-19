package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.utilities.Utilities;

import java.util.HashMap;

public class ModelData {
	@SerializedName("parent")
	public String parent = null; // Currently unused
	@SerializedName("ambientocclusion")
	public boolean ambientocclusion = true;
	@SerializedName("display")
	public HashMap<String, PositionData> display = new HashMap<>();
	@SerializedName("textures")
	public HashMap<String, String> textures = new HashMap<>();
	@SerializedName("elements")
	public CubeData[] elements = new CubeData[0];

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
	public String toString(){
		StringBuilder builder = new StringBuilder("\n");
		builder.append("parent: ").append(parent).append("\n");
		builder.append("ambientocclusion: ").append(ambientocclusion).append("\n");
		builder.append("display: ").append("\n");
		for (String key: display.keySet()) {
			builder.append("\t").append(key).append("\n");;
			builder.append(Utilities.tabBlock(display.get(key).toString(), 2));
		}
		builder.append("textures: ").append("\n");
		for (String key: textures.keySet()) {
			builder.append("\t").append(key).append(": ").append(textures.get(key)).append("\n");
		}
		builder.append("elements: ").append("\n");
		for (CubeData cube: elements) {
			builder.append("\t{\n");
			builder.append(Utilities.tabBlock(cube.toString(),1));
			builder.append("\t}\n");
		}
		return builder.toString();
	}
}
