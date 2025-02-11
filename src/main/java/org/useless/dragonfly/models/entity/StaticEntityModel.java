package org.useless.dragonfly.models.entity;

import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.NotNull;

public interface StaticEntityModel {
    void resetBones();
    @NotNull BoneTransform getTransform(@NotNull String boneId);
    @NotNull AABB visibleBounds();
    void translateToBone(@NotNull String boneId);
    void render(@NotNull Tessellator tessellator);
}
