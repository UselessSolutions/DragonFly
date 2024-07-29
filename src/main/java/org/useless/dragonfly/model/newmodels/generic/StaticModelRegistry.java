package org.useless.dragonfly.model.newmodels.generic;

import net.minecraft.client.Minecraft;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.model.newmodels.generic.components.block.BlockComponentBuilder;
import org.useless.dragonfly.model.newmodels.generic.components.block.BlockFaceBuilder;
import org.useless.dragonfly.model.newmodels.generic.data.StaticModelDeserializer;

import java.util.HashMap;
import java.util.Map;

public final class StaticModelRegistry {
    private static final boolean hardFail = false;
    private static final StaticModelRegistry instance = new StaticModelRegistry();
    private final Minecraft mc;
    private final Map<NamespaceID, StaticModel> dataModelMap = new HashMap<>();
    private StaticModelRegistry(){
        this.mc = Minecraft.getMinecraft(this);
        reset();
    }
    public static StaticModelRegistry getInstance(){
        return instance;
    }
    public StaticModel registerModel(NamespaceID id, StaticModel model){
        dataModelMap.put(id, model);
        return model;
    }
    public StaticModel registerModel(String id, StaticModel model){
        if (!id.contains(":")){
            id = "minecraft:" + id;
        }
        return registerModel(new NamespaceID(id), model);
    }

    /**
     * @return Model from id if it can be located, otherwise returns the defaultModel
     */
    public StaticModel retrieveModel(NamespaceID id, @Nullable StaticModel defaultModel){
        if (dataModelMap.containsKey(id)) return dataModelMap.get(id);
        String resourceString = String.format("/assets/%s/models/%s.json", id.namespace, id.value);
        StaticModel model;
        try {
            model = StaticModelDeserializer.decodeStream(mc, resourceString);
        } catch (ModelLoadException e){
            if (hardFail){
                throw new RuntimeException(e);
            }
            e.printStackTrace();
            model = defaultModel;
        }
        if (model != null){
            registerModel(id, model);
        }
        return model;
    }
    /**
     *
     * @return Model from id if it can be located, otherwise returns the defaultModel
     */
    public StaticModel retrieveModel(String id, @Nullable StaticModel defaultModel){
        try {
            if (!id.contains(":")){
                id = "minecraft:" + id;
            }
            return retrieveModel(new NamespaceID(id), defaultModel);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return defaultModel;
        }
    }

	public StaticModel retrieveModel(String id) {
		return retrieveModel(id, retrieveModel("builtin:block/missing", null));
	}

	public StaticModel retrieveModel(NamespaceID id) {
		return retrieveModel(id, retrieveModel("builtin:block/missing", null));
	}

    public void reset(){
        dataModelMap.clear();
        registerModel("builtin:block/missing", new StaticModel(null, null,
            new BlockComponentBuilder(0, 0, 0, 1, 1, 1)
                .setFace(Side.TOP, new BlockFaceBuilder("").setCullSide(Side.TOP))
                .setFace(Side.BOTTOM, new BlockFaceBuilder("").setCullSide(Side.BOTTOM))
                .setFace(Side.NORTH, new BlockFaceBuilder("").setCullSide(Side.NORTH))
                .setFace(Side.SOUTH, new BlockFaceBuilder("").setCullSide(Side.SOUTH))
                .setFace(Side.WEST, new BlockFaceBuilder("").setCullSide(Side.WEST))
                .setFace(Side.EAST, new BlockFaceBuilder("").setCullSide(Side.EAST))
                .build()));
    }
}
