package org.useless.dragonfly.model.newmodels.generic.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.model.newmodels.generic.DisplayPosition;
import org.useless.dragonfly.model.newmodels.generic.StaticModel;
import org.useless.dragonfly.model.newmodels.generic.StaticModelRegistry;
import org.useless.dragonfly.model.newmodels.generic.components.ModelComponent;
import org.useless.dragonfly.model.newmodels.generic.components.block.BlockComponentBuilder;
import org.useless.dragonfly.model.newmodels.generic.components.block.BlockFaceBuilder;
import org.useless.dragonfly.utilities.AxisUtils;
import org.useless.dragonfly.utilities.SideUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MojangModelDeserializer implements JsonDeserializer<StaticModel> {
    @Override
    public StaticModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String parent = null;
        Map<String, String> textureSymbols = new HashMap<>();
        String particle = null;
        List<Element> elements = new ArrayList<>();
        if (object.has("parent")) {
            parent = object.get("parent").getAsString();
        }
        if (object.has("textures")){
            Map<String, JsonElement> map = object.getAsJsonObject("textures").asMap();
            for (Map.Entry<String, JsonElement> e : map.entrySet()){
                if (e.getKey().equals("particle")){
                    particle = e.getValue().getAsString();
                } else {
                    textureSymbols.put(e.getKey(), e.getValue().getAsString());
                }
            }
        }

        if (object.has("elements")){
            JsonArray eArr = object.getAsJsonArray("elements");
            for (JsonElement e : eArr){
                elements.add(new Element(e.getAsJsonObject()));
            }
        }

        StaticModel parentModel = null;
        if (parent != null){
            parentModel = StaticModelRegistry.getInstance().retrieveModel(parent, null);
            if (parentModel == null) throw new JsonParseException("Could not retrieve parent model from string '" + parent + "'.");
        }

        ModelComponent[] components = new ModelComponent[elements.size()];
        for (int i = 0; i < components.length; i++) {
            Element e = elements.get(i);
            BlockComponentBuilder builder = new BlockComponentBuilder(e.from[0]/16d, e.from[1]/16d, e.from[2]/16d, e.to[0]/16d, e.to[1]/16d, e.to[2]/16d);
            builder.setUseShade(e.shade);

            if (e.rotation != null){
                builder.setRotationOrigin(e.rotation.origin[0]/16d, e.rotation.origin[1]/16d, e.rotation.origin[2]/16d);
                switch (e.rotation.axis){
                    case X:
                        builder.setRotationX(e.rotation.angle);
                        break;
                    case Y:
                        builder.setRotationY(e.rotation.angle);
                        break;
                    case Z:
                        builder.setRotationZ(e.rotation.angle);
                        break;
                }
            }

            if (particle != null){
                textureSymbols.remove("particle");
                for (Side side : Side.sides){
                    textureSymbols.put("particle_" + side.toString().toLowerCase(), particle);
                }
                textureSymbols.put("overlay", parent);
            }

            for (Map.Entry<Side, Face> faceEntry : e.faceMap.entrySet()){
                Face eFace = faceEntry.getValue();
                BlockFaceBuilder faceBuilder = new BlockFaceBuilder(eFace.texture);

                faceBuilder.setCullSide(eFace.cullface);
                faceBuilder.setTexRotation(eFace.textureRotation);
                faceBuilder.setUseColor(eFace.tintIndex != -1);



                if (eFace.uv != null){
                    boolean mirrorU = false;
                    boolean mirrorV = false;

                    if (eFace.uv[0] > eFace.uv[2]){
                        mirrorU = true;
                    }

                    if (eFace.uv[1] > eFace.uv[3]){
                        mirrorV = true;
                    }

                    double minU = Math.min(eFace.uv[0], eFace.uv[2]);
                    double minV = Math.min(eFace.uv[1], eFace.uv[3]);
                    double widthU = Math.abs(eFace.uv[0] - eFace.uv[2]);
                    double widthV = Math.abs(eFace.uv[1] - eFace.uv[3]);

                    faceBuilder.setTexMirrorX(mirrorU);
                    faceBuilder.setTexMirrorY(mirrorV);
                    faceBuilder.setManualUVs(minU, minV, widthU, widthV);
                }

                builder.setFace(faceEntry.getKey(), faceBuilder);
            }
            components[i] = builder.build();
        }

        Boolean ambientOcclusion = null;
        if (object.has("ambientocclusion")){
            ambientOcclusion = object.get("ambientocclusion").getAsBoolean();
        }

        Map<String, DisplayPosition> displayPositionMap = new HashMap<>();
        if (object.has("display")){
            Map<String, JsonElement> elementMap = object.getAsJsonObject("display").asMap();
            for (Map.Entry<String, JsonElement> e : elementMap.entrySet()){
                JsonObject o = e.getValue().getAsJsonObject();
                double[] rotation;
                if (o.has("rotation")){
                    rotation = ModelDeserializer.decodeDoubleArr(o.getAsJsonArray("rotation"));
                } else {
                    rotation = new double[3];
                }


                double[] translation;
                if (o.has("translation")){
                    translation = ModelDeserializer.decodeDoubleArr(o.getAsJsonArray("translation"));
                } else {
                    translation = new double[3];
                }

                double[] scale;
                if (o.has("scale")){
                    scale = ModelDeserializer.decodeDoubleArr(o.getAsJsonArray("scale"));
                } else {
                    scale = new double[]{ 1, 1, 1 };
                }

                displayPositionMap.put(e.getKey(), new DisplayPosition(rotation, translation, scale));
            }
        }

        StaticModel outputModel = new StaticModel(parentModel, ambientOcclusion, components);

        for (Map.Entry<String, String> entry : textureSymbols.entrySet()){
            assert !entry.getValue().isEmpty();
            outputModel.setTexture(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, DisplayPosition> e : displayPositionMap.entrySet()){
            outputModel.setDisplayPosition(e.getKey(), e.getValue());
        }

        return outputModel;
    }
    static class Element {
        final double[] from;
        final double[] to;
        @Nullable
        final Rotation rotation;
        final boolean shade;
        @NotNull
        Map<Side, Face> faceMap = new HashMap<>();
        public Element(JsonObject sourceObject){
            JsonArray fromArr = sourceObject.getAsJsonArray("from");
            from = new double[fromArr.size()];
            for (int i = 0; i < from.length; i++) {
                from[i] = fromArr.get(i).getAsDouble();
            }

            JsonArray toArr = sourceObject.getAsJsonArray("to");
            to = new double[toArr.size()];
            for (int i = 0; i < to.length; i++) {
                to[i] = toArr.get(i).getAsDouble();
            }

            if (sourceObject.has("rotation")){
                rotation = new Rotation(sourceObject.getAsJsonObject("rotation"));
            } else {
                rotation = null;
            }

            if (sourceObject.has("shade")){
                shade = sourceObject.get("shade").getAsBoolean();
            } else {
                shade = true;
            }

            Map<String, JsonElement> jsonFaceMap = sourceObject.getAsJsonObject("faces").asMap();
            for (Map.Entry<String, JsonElement> e : jsonFaceMap.entrySet()){
                Side side = SideUtils.getSideFromName(e.getKey());
                faceMap.put(side, new Face(side, e.getValue().getAsJsonObject()));
            }
        }

        @Override
        public String toString() {
            return "Element{" +
                "from=" + Arrays.toString(from) +
                ", to=" + Arrays.toString(to) +
                ", rotation=" + rotation +
                ", shade=" + shade +
                ", faceMap=" + faceMap +
                '}';
        }
    }
    static class Rotation {
        final double @NotNull [] origin;
        @NotNull
        final Axis axis;
        final float angle;
        final boolean rescale;
        public Rotation(JsonObject sourceObject){
            JsonArray originArr = sourceObject.getAsJsonArray("origin");
            origin = new double[originArr.size()];
            for (int i = 0; i < origin.length; i++) {
                origin[i] = originArr.get(i).getAsDouble();
            }

            axis = AxisUtils.getAxisFromName(sourceObject.get("axis").getAsString());

            angle = sourceObject.get("angle").getAsFloat();

            if (sourceObject.has("rescale")){
                rescale = sourceObject.get("rescale").getAsBoolean();
            } else {
                rescale = false;
            }
        }

        @Override
        public String toString() {
            return "Rotation{" +
                "origin=" + Arrays.toString(origin) +
                ", axis='" + axis + '\'' +
                ", angle=" + angle +
                ", rescale=" + rescale +
                '}';
        }
    }

    static class Face{
        double @Nullable [] uv = null;
        String texture;
        Side cullface;
        int textureRotation = 0;
        int tintIndex = -1;

        public Face(Side side, JsonObject sourceObject){
            if (sourceObject.has("uv")){
                JsonArray uvArr = sourceObject.getAsJsonArray("uv");
                uv = new double[uvArr.size()];
                for (int i = 0; i < uv.length; i++) {
                    uv[i] = uvArr.get(i).getAsDouble();
                }
            }
            texture = sourceObject.get("texture").getAsString();

            if (sourceObject.has("cullface")){
                cullface = SideUtils.getSideFromName(sourceObject.get("cullface").getAsString());
            } else {
                cullface = side;
            }

            if (sourceObject.has("rotation")){
                textureRotation = sourceObject.get("rotation").getAsInt();
            }

            if (sourceObject.has("tintindex")){
                tintIndex = sourceObject.get("tintindex").getAsInt();
            }
        }

        @Override
        public String toString() {
            return "Face{" +
                "uv=" + Arrays.toString(uv) +
                ", texture='" + texture + '\'' +
                ", cullface=" + cullface +
                ", textureRotation=" + textureRotation +
                ", tintIndex=" + tintIndex +
                '}';
        }
    }
}
