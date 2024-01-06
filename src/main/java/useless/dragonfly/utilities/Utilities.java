package useless.dragonfly.utilities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;
import net.minecraft.core.data.DataLoader;
import useless.dragonfly.DragonFly;
import useless.dragonfly.utilities.vector.Vector3f;

import java.io.InputStream;
import java.lang.reflect.Field;
public class Utilities {
	public static final float COMPARE_CONST = 0.001f;
	public static String writeFields(Class<?> clazz){
		Field[] fields = clazz.getFields();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < fields.length; i++)  {
			Field field = fields[i];
			builder.append(field.getName()).append(": ").append(field.getClass().cast(field));
			if (i < fields.length-1){
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	public static String tabBlock(String string, int tabAmount){
		StringBuilder builder = new StringBuilder();
		for (String line: string.split("\n")) {
			builder.append(tabString(tabAmount)).append(line).append("\n");
		}
		return builder.toString();
	}
	public static String tabString(int tabAmount){
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < tabAmount; i++) {
			builder.append("\t");
		}
		return builder.toString();
	}
	public static boolean equalFloats(float a, float b){
		return Math.abs(Float.compare(a, b)) < COMPARE_CONST;
	}
	public static Vector3f rotatePoint(Vector3f point, Vector3f origin, String axis, float angle){
		switch (axis){
			case "x":
				return point.rotateAroundX(origin, -angle);
			case "y":
				return point.rotateAroundY(origin, angle);
			case "z":
				return point.rotateAroundZ(origin, -angle);
		}
		throw new RuntimeException("Axis " + axis + " Is not 'X', 'Y', or 'Z'!");
	}

	/**
	 * Tries to load resource from multiple classes, if they all fail it throws an exception
	 */
	public static InputStream getResourceAsStream(String path){
		InputStream in;
		if (!Global.isServer && Minecraft.getMinecraft(Minecraft.class).texturePackList != null){
			in = Minecraft.getMinecraft(Minecraft.class).texturePackList.selectedTexturePack.getResourceAsStream(path);
			if (in != null){
				return in;
			}
		}
		in = DataLoader.class.getResourceAsStream(path);
		if (in != null){
			return in;
		}
		in = Utilities.class.getResourceAsStream(path);
		if (in != null){
			return in;
		}
		in = DragonFly.class.getResourceAsStream(path);
		if (in != null){
			return in;
		}
		in = FabricLoader.getInstance().getClass().getResourceAsStream(path);
		if (in != null){
			return in;
		}
		in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		if (in != null){
			return in;
		}
		throw new RuntimeException("Resource at '" + path + "' returned null! Does this file exist?");
	}
}
