package org.useless.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class GsonHelper {
    private GsonHelper() {}

    public static float[] getAsFloatArray(@NotNull JsonElement element, int expectedSize) throws JsonParseException {
        if (!element.isJsonArray()) throw new JsonParseException("Provided element is not a jsonArray! " + element);
        JsonArray arr = element.getAsJsonArray();
        if (arr.size() != expectedSize) throw new JsonParseException("Provided array is of size '" + arr.size() + "' not expected size of '" + expectedSize + "'!");
        float[] returnArr = new float[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            JsonElement e = arr.get(i);
            if (!e.isJsonPrimitive()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            JsonPrimitive p = e.getAsJsonPrimitive();
            if (!p.isNumber()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            returnArr[i] = p.getAsFloat();
        }
        return returnArr;
    }

    public static double[] getAsDoubleArray(@NotNull JsonElement element, int expectedSize) throws JsonParseException {
        if (!element.isJsonArray()) throw new JsonParseException("Provided element is not a jsonArray! " + element);
        JsonArray arr = element.getAsJsonArray();
        if (arr.size() != expectedSize) throw new JsonParseException("Provided array is of size '" + arr.size() + "' not expected size of '" + expectedSize + "'!");
        double[] returnArr = new double[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            JsonElement e = arr.get(i);
            if (!e.isJsonPrimitive()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            JsonPrimitive p = e.getAsJsonPrimitive();
            if (!p.isNumber()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            returnArr[i] = p.getAsDouble();
        }
        return returnArr;
    }

    public static long[] getAsLongArray(@NotNull JsonElement element, int expectedSize) throws JsonParseException {
        if (!element.isJsonArray()) throw new JsonParseException("Provided element is not a jsonArray! " + element);
        JsonArray arr = element.getAsJsonArray();
        if (arr.size() != expectedSize) throw new JsonParseException("Provided array is of size '" + arr.size() + "' not expected size of '" + expectedSize + "'!");
        long[] returnArr = new long[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            JsonElement e = arr.get(i);
            if (!e.isJsonPrimitive()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            JsonPrimitive p = e.getAsJsonPrimitive();
            if (!p.isNumber()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            returnArr[i] = p.getAsLong();
        }
        return returnArr;
    }

    public static int[] getAsIntArray(@NotNull JsonElement element, int expectedSize) throws JsonParseException {
        if (!element.isJsonArray()) throw new JsonParseException("Provided element is not a jsonArray! " + element);
        JsonArray arr = element.getAsJsonArray();
        if (arr.size() != expectedSize) throw new JsonParseException("Provided array is of size '" + arr.size() + "' not expected size of '" + expectedSize + "'!");
        int[] returnArr = new int[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            JsonElement e = arr.get(i);
            if (!e.isJsonPrimitive()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            JsonPrimitive p = e.getAsJsonPrimitive();
            if (!p.isNumber()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            returnArr[i] = p.getAsInt();
        }
        return returnArr;
    }

    public static short[] getAsShortArray(@NotNull JsonElement element, int expectedSize) throws JsonParseException {
        if (!element.isJsonArray()) throw new JsonParseException("Provided element is not a jsonArray! " + element);
        JsonArray arr = element.getAsJsonArray();
        if (arr.size() != expectedSize) throw new JsonParseException("Provided array is of size '" + arr.size() + "' not expected size of '" + expectedSize + "'!");
        short[] returnArr = new short[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            JsonElement e = arr.get(i);
            if (!e.isJsonPrimitive()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            JsonPrimitive p = e.getAsJsonPrimitive();
            if (!p.isNumber()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            returnArr[i] = p.getAsShort();
        }
        return returnArr;
    }

    public static byte[] getAsByteArray(@NotNull JsonElement element, int expectedSize) throws JsonParseException {
        if (!element.isJsonArray()) throw new JsonParseException("Provided element is not a jsonArray! " + element);
        JsonArray arr = element.getAsJsonArray();
        if (arr.size() != expectedSize) throw new JsonParseException("Provided array is of size '" + arr.size() + "' not expected size of '" + expectedSize + "'!");
        byte[] returnArr = new byte[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            JsonElement e = arr.get(i);
            if (!e.isJsonPrimitive()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            JsonPrimitive p = e.getAsJsonPrimitive();
            if (!p.isNumber()) throw new JsonParseException("Element '" + i + "' in array is not a number!");
            returnArr[i] = p.getAsByte();
        }
        return returnArr;
    }

    public static boolean[] getAsBooleanArray(@NotNull JsonElement element, int expectedSize) throws JsonParseException {
        if (!element.isJsonArray()) throw new JsonParseException("Provided element is not a jsonArray! " + element);
        JsonArray arr = element.getAsJsonArray();
        if (arr.size() != expectedSize) throw new JsonParseException("Provided array is of size '" + arr.size() + "' not expected size of '" + expectedSize + "'!");
        boolean[] returnArr = new boolean[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            JsonElement e = arr.get(i);
            if (!e.isJsonPrimitive()) throw new JsonParseException("Element '" + i + "' in array is not a boolean!");
            JsonPrimitive p = e.getAsJsonPrimitive();
            if (!p.isBoolean()) throw new JsonParseException("Element '" + i + "' in array is not a boolean!");
            returnArr[i] = p.getAsBoolean();
        }
        return returnArr;
    }

    public static String[] getAsStringArray(@NotNull JsonElement element, int expectedSize) throws JsonParseException {
        if (!element.isJsonArray()) throw new JsonParseException("Provided element is not a jsonArray! " + element);
        JsonArray arr = element.getAsJsonArray();
        if (arr.size() != expectedSize) throw new JsonParseException("Provided array is of size '" + arr.size() + "' not expected size of '" + expectedSize + "'!");
        String[] returnArr = new String[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            JsonElement e = arr.get(i);
            if (!e.isJsonPrimitive()) throw new JsonParseException("Element '" + i + "' in array is not a string!");
            JsonPrimitive p = e.getAsJsonPrimitive();
            if (!p.isString()) throw new JsonParseException("Element '" + i + "' in array is not a string!");
            returnArr[i] = p.getAsString();
        }
        return returnArr;
    }
}
