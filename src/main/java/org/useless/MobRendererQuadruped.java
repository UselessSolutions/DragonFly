package org.useless;

import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.models.entity.BoneTransform;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import org.useless.dragonfly.renderer.MobRenderer;

public class MobRendererQuadruped<T extends Mob> extends MobRenderer<T> {

    public MobRendererQuadruped(final float shadowSize) {
        super(shadowSize);
    }

    @Override
    protected @Nullable StaticEntityModel getAndSetupModelForLayer(@NotNull final T entity, final float brightness, final float partialTick, final int layer) {
        final StaticEntityModel model = getModel("main");
        model.resetBones();

        final BoneTransform head = model.getTransform("head");
        final BoneTransform leg0 = model.getTransform("leg0");
        final BoneTransform leg1 = model.getTransform("leg1");
        final BoneTransform leg2 = model.getTransform("leg2");
        final BoneTransform leg3 = model.getTransform("leg3");

        final float bodyYaw = getBodyYaw(entity, partialTick);
        final float headYaw = getHeadYaw(entity, partialTick) - bodyYaw;
        final float headPitch = getHeadPitch(entity, partialTick);
        final float limbSwing = getLimbSwing(entity, partialTick);
        final float limbYaw = getLimbYaw(entity, partialTick);

        head.rotX = headPitch;
        head.rotY = headYaw;
        leg0.rotX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbYaw;
        leg1.rotX = MathHelper.cos(limbSwing * 0.6662F + MathHelper.PI) * 1.4F * limbYaw;
        leg2.rotX = MathHelper.cos(limbSwing * 0.6662F + MathHelper.PI) * 1.4F * limbYaw;
        leg3.rotX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbYaw;

        return model;
    }
}
