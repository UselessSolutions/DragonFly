package useless.dragonfly.utilities;

import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.util.vector.Vector3f;
import useless.dragonfly.debug.DebugBlocks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Utilities {
	public static final float COMPARE_CONST = 0.001f;
	public static String writeFields(Class clazz){
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
				return rotateAroundX(point, origin, angle);
			case "y":
				return rotateAroundY(point, origin, angle);
			case "z":
				return rotateAroundZ(point, origin, angle);
		}
		throw new RuntimeException("Axis " + axis + " Is not 'X', 'Y', or 'Z'!");
	}
	public static Vector3f rotateAroundX(Vector3f point, Vector3f origin, float angle){
		angle = (float) ((angle) * (Math.PI/180)); // Convert to radians
		float y = point.getY();
		float z = point.getZ();
		z -= origin.getZ();
		y -= origin.getY();

		float newZ = (float) (z * Math.cos(angle) - y * Math.sin(angle));
		float newY = (float) (z * Math.sin(angle) + y * Math.cos(angle));

		z = newZ + origin.getZ();
		y = newY + origin.getY();
		return new Vector3f(point.x, y, z);
	}

	public static Vector3f rotateAroundY(Vector3f point, Vector3f origin, float angle){
		angle = (float) ((angle) * (Math.PI/180)); // Convert to radians
		float x = point.getX();
		float z = point.getZ();
		z -= origin.getZ();
		x -= origin.getX();

		float newZ = (float) (z * Math.cos(angle) - x * Math.sin(angle));
		float newX = (float) (z * Math.sin(angle) + x * Math.cos(angle));

		z = newZ + origin.getZ();
		x = newX + origin.getX();
		return new Vector3f(x, point.y, z);
	}
	public static Vector3f rotateAroundZ(Vector3f point, Vector3f origin, float angle){
		angle = (float) ((angle) * (Math.PI/180)); // Convert to radians
		float x = point.getX();
		float y = point.getY();
		y -= origin.getY();
		x -= origin.getX();

		float newZ = (float) (y * Math.cos(angle) - x * Math.sin(angle));
		float newY = (float) (y * Math.sin(angle) + x * Math.cos(angle));

		y = newZ + origin.getY();
		x = newY + origin.getX();
		return new Vector3f(x, y, point.z);
	}
	public static List<String> getResourceFiles(String path) {
		List<String> filenames = new ArrayList<>();

		try (InputStream in = getResourceAsStream(path)) {
			if (in == null) {
				System.out.println(path + " failed to load, `in` was null");
				return new ArrayList<>();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String resource;

			while ((resource = br.readLine()) != null) {
				filenames.add(resource);
			}
		} catch (IOException e) {
			System.out.println(path + " failed to load\n" + e);
			return new ArrayList<>();
		}

		return filenames;
	}

	public static InputStream getResourceAsStream(String resource) {
		final InputStream in
			= getContextClassLoader().getResourceAsStream(resource);

		return in == null ? DebugBlocks.class.getResourceAsStream(resource) : in;
	}

	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
}
