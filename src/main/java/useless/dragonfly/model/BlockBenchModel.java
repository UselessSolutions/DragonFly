package useless.dragonfly.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.DragonFly;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class BlockBenchModel {
	public static HashMap<String, Side> keyToSide = new HashMap<>();
	public static HashMap<Side, String> sideToKey = new HashMap<>();
	public static void registerSide(Side side, String key){
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

	public static BlockBenchModel decodeModel(String modID, String modelSource){
		InputStream inputStream = BlockBenchModel.class.getResourceAsStream(DragonFly.getModelLocation(modID, modelSource));
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
		BlockBenchModel model = DragonFly.GSON.fromJson(reader, BlockBenchModel.class);
		model.hasFaceToRenderOnSide = new boolean[6];
		for (BenchCube cube: model.elements) {
			cube.process();
			for (int i = 0; i < model.hasFaceToRenderOnSide.length; i++) {
				model.hasFaceToRenderOnSide[i] |= cube.isOuterFace(Side.getSideById(i));
			}
		}
		for (BenchCube cube: model.elements) {
			cube.processVisibleFaces(model);
		}
		return model;
	}
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
