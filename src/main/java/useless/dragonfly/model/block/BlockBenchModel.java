package useless.dragonfly.model.block;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.util.helper.Side;

import java.util.Arrays;
import java.util.HashMap;

public class BlockBenchModel {
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
	public static final float textureSize = 16;
	public boolean[] hasFaceToRenderOnSide;
	public boolean hasFaceToRender(Side side){
		return hasFaceToRenderOnSide[side.getId()];
	}
	public String toString(){
		StringBuilder builder = new StringBuilder(this.credit);
		for (BenchCube cube: elements) {
			builder.append(Arrays.toString(cube.from));
			builder.append(Arrays.toString(cube.to));
			builder.append(cube.color);
			for (String key: cube.faces.keySet()) {
				builder.append(key);
				BenchFace face = cube.faces.get(key);
				builder.append(face.texture);
				builder.append(Arrays.toString(face.uv));
			}
		}
		return builder.toString();
	}
	@SerializedName("credit")
	public String credit = "";
	@SerializedName("elements")
	public BenchCube[] elements;
}
