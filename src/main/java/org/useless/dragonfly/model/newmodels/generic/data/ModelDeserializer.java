package org.useless.dragonfly.model.newmodels.generic.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public interface ModelDeserializer<T> {
    static double[] decodeDoubleArr(JsonArray array){
        double[] arr = new double[array.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = array.get(i).getAsDouble();
        }
        return arr;
    }
    static float[] decodeFloatArr(JsonArray array){
        float[] arr = new float[array.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = array.get(i).getAsFloat();
        }
        return arr;
    }

    static int[] decodeIntArr(JsonArray array){
        int[] arr = new int[array.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = array.get(i).getAsInt();
        }
        return arr;
    }
    T deserialize(JsonElement element, int version)
        throws JsonParseException;
}
