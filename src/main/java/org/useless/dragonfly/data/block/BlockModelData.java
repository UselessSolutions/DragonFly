package org.useless.dragonfly.data.block;

import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
//import org.useless.dragonfly.models.block.StaticBlockModel;

public interface BlockModelData {
    @NotNull NamespaceID modelId();
//    @NotNull StaticBlockModel asModel();
}
