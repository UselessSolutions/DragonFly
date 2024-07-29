package org.useless.dragonfly.model.newmodels.generic.data.components.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.util.helper.Side;
import org.useless.dragonfly.model.newmodels.generic.components.block.BlockComponent;
import org.useless.dragonfly.model.newmodels.generic.components.block.BlockComponentBuilder;
import org.useless.dragonfly.model.newmodels.generic.components.block.BlockFaceBuilder;
import org.useless.dragonfly.model.newmodels.generic.data.ModelDeserializer;
import org.useless.dragonfly.utilities.SideUtils;

import java.util.HashMap;
import java.util.Map;

public class BlockComponentDeserializer implements ModelDeserializer<BlockComponent> {
    @Override
    public BlockComponent deserialize(JsonElement element, int version) {
        JsonObject object = element.getAsJsonObject();
        double[] from = ModelDeserializer.decodeDoubleArr(object.getAsJsonArray("from"));
        double[] to = ModelDeserializer.decodeDoubleArr(object.getAsJsonArray("to"));

        BlockComponentBuilder componentBuilder = new BlockComponentBuilder(from[0]/16d, from[1]/16d, from[2]/16d, to[0]/16d, to[1]/16d, to[2]/16d);

        Map<Side, BlockFaceBuilder> faces = decodeFaceMap(object.getAsJsonObject("faces"));
        for (Map.Entry<Side, BlockFaceBuilder> e : faces.entrySet()){
            componentBuilder.setFace(e.getKey(), e.getValue());
        }

        if (object.has("overbright_faces")){
            Map<Side, BlockFaceBuilder> overbrightfaces = decodeFaceMap(object.getAsJsonObject("overbright_faces"));
            for (Map.Entry<Side, BlockFaceBuilder> e : overbrightfaces.entrySet()){
                componentBuilder.setOverbrightFace(e.getKey(), e.getValue());
            }
        }

        if (object.has("rotation")){
            Rotation rotation = new Rotation(object.getAsJsonObject("rotation"));
            componentBuilder.setRotationOrigin(rotation.origin[0]/16d, rotation.origin[1]/16d, rotation.origin[2]/16d);
            componentBuilder.setRotationX(rotation.angles[0]);
            componentBuilder.setRotationY(rotation.angles[1]);
            componentBuilder.setRotationZ(rotation.angles[2]);
            componentBuilder.setRescaleX(rotation.rescale_x);
            componentBuilder.setRescaleY(rotation.rescale_y);
            componentBuilder.setRescaleZ(rotation.rescale_z);
        }

        if (object.has("shade")){
            componentBuilder.setUseShade(object.get("shade").getAsBoolean());
        }

        if (object.has("inner_lighting")){
            componentBuilder.setForceInnerLighting(object.get("inner_lighting").getAsBoolean());
        }

        return componentBuilder.build();
    }
    public static Map<Side, BlockFaceBuilder> decodeFaceMap(JsonObject mapObject){
        Map<Side, BlockFaceBuilder> faceMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> e : mapObject.asMap().entrySet()) {
            Side side = SideUtils.getSideFromName(e.getKey());
            Face face = new Face(e.getValue().getAsJsonObject());
            BlockFaceBuilder faceBuilder = new BlockFaceBuilder(face.texture);
            faceBuilder.setCullSide(face.cullface);
            faceBuilder.setUseColor(face.useColor);
            faceBuilder.setFlippedNormals(face.flipNormals);
            faceBuilder.setDoubleSided(face.doubledSided);
            faceBuilder.setTexRotation(face.textureRotation);

            faceBuilder.setUVScale(face.uv_scale[0], face.uv_scale[1]);

            if (face.uv != null) {
                boolean mirrorU = false;
                boolean mirrorV = false;

                if (face.uv[0] > face.uv[2]) {
                    mirrorU = true;
                }

                if (face.uv[1] > face.uv[3]) {
                    mirrorV = true;
                }

                double minU = Math.min(face.uv[0], face.uv[2]);
                double minV = Math.min(face.uv[1], face.uv[3]);
                double widthU = Math.abs(face.uv[0] - face.uv[2]);
                double widthV = Math.abs(face.uv[1] - face.uv[3]);

                faceBuilder.setTexMirrorX(mirrorU);
                faceBuilder.setTexMirrorY(mirrorV);
                faceBuilder.setManualUVs(minU, minV, widthU, widthV);
            }

            faceMap.put(side, faceBuilder);
        }
        return faceMap;
    }
}
