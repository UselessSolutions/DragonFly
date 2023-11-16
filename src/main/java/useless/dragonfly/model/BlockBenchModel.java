package useless.dragonfly.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import useless.dragonfly.DragonFly;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class BlockBenchModel {
	public static BlockBenchModel decodeModel(String modID, String modelSource){
		InputStream inputStream = BlockBenchModel.class.getResourceAsStream(DragonFly.getModelLocation(modID, modelSource));
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
		BlockBenchModel model = DragonFly.GSON.fromJson(reader, BlockBenchModel.class);
//		System.out.println(model.credit);
//		for (BlockBenchModel.BenchCube cube: model.elements) {
//			System.out.println(Arrays.toString(cube.from));
//			System.out.println(Arrays.toString(cube.to));
//			System.out.println(cube.color);
//			for (String key: cube.faces.keySet()) {
//				System.out.println(key);
//				BlockBenchModel.BenchFace face = cube.faces.get(key);
//				System.out.println(face.texture);
//				System.out.println(Arrays.toString(face.uv));
//			}
//
//		}
		return model;
	}
	@SerializedName("credit")
	public String credit = "";
	@SerializedName("elements")
	public BenchCube[] elements;
	public static class BenchCube{
		@SerializedName("from")
		public int[] from;
		@SerializedName("to")
		public int[] to;
		@SerializedName("color")
		public int color;
		@SerializedName("faces")
		public HashMap<String, BenchFace> faces;

	}
	public static class BenchFace{
		@SerializedName("uv")
		public int[] uv;
		@SerializedName("texture")
		public String texture;
	}
}
