package org.useless.dragonfly.data.entity;

import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.models.entity.StaticEntityModel;

public interface EntityModelData {
    @NotNull String modelId();
    @NotNull StaticEntityModel asModel(double inflation);
}
