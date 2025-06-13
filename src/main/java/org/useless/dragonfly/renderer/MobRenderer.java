package org.useless.dragonfly.renderer;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.Global;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.Color;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.slf4j.Logger;
import org.useless.dragonfly.animation.Animation;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import org.useless.dragonfly.models.entity.mojang.StaticEntityModelMojang;
import org.useless.util.AnimationHelper;
import org.useless.util.AnimationState;

import java.util.ArrayList;
import java.util.List;

public abstract class MobRenderer<T extends Mob> extends EntityRenderer<T> {
	public Vector3f VEC_ANIMATION = new Vector3f();
    public static final float ENTITY_RENDER_SCALE = 16f;
    private static final Logger LOGGER = LogUtils.getLogger();
    private final @NotNull List<@Nullable StaticEntityModel> setupModels = new ArrayList<>();
    public MobRenderer(final float shadowSize) {
        super(shadowSize);
    }
    @Override
    public void render(final @NotNull Tessellator tessellator, final @NotNull T entity, final double x, final double y, final double z, final float yaw, final float partialTick) {
        loadEntityTexture(entity);
//        bindTexture("/assets/minecraft/textures/entity/creeper/debug.png");
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glPushMatrix();
        preRenderTransform(entity, x, y, z, yaw, partialTick);

        float brightness = entity.getBrightness(partialTick);
        if (Global.accessor.isFullbrightEnabled() || LightmapHelper.isLightmapEnabled()) brightness = 1.0f;

        this.setupModels.clear();
        final int maxRenderLayer = maxRenderLayer(entity);
        for(int layer = 0; layer <= maxRenderLayer; layer++) {
            try {
                final StaticEntityModel model = getAndSetupModelForLayer(entity, brightness, partialTick, layer);
                this.setupModels.add(model);
                if (model != null) {
                    model.render(tessellator);
                }
            } catch (final Exception e) {
                LOGGER.error("Error setting up model on layer '{}' in renderer '{}'", layer, getClass().getSimpleName(), e);
            }
        }
        renderAdditional(tessellator, entity, partialTick);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        final int argb = getOverlayColor(entity, brightness, partialTick);
        final boolean hasOverlayAlpha = Color.alphaFromInt(argb) > 0;
        if(hasOverlayAlpha || entity.hurtTime > 0 || entity.deathTime > 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthFunc(GL11.GL_EQUAL);
            if(entity.hurtTime > 0 || entity.deathTime > 0) {
                for(int layer = 0; layer <= maxRenderLayer; layer++) {
                    final StaticEntityModel model = this.setupModels.get(layer);
                    if (model != null) {
                        GL11.glColor4f(brightness, 0.0F, 0.0F, 0.4F);
                        model.render(tessellator);
                    }
                }

            }
            if(hasOverlayAlpha) {
                final float r = Color.redFromInt(argb)/255f;
                final float g = Color.greenFromInt(argb)/255f;
                final float b = Color.blueFromInt(argb)/255f;
                final float a = Color.alphaFromInt(argb)/255f;
                for(int layer = 0; layer < maxRenderLayer; layer++) {
                    final StaticEntityModel model = this.setupModels.get(layer);
                    if (model != null) {
                        GL11.glColor4f(r, g, b, a);
                        model.render(tessellator);
                    }
                }
            }
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        renderSpecials(tessellator, entity, x, y, z);
    }

    protected void preRenderTransform(final @NotNull T entity, final double x, final double y, final double z, final float yaw, final float partialTick) {
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-getBodyYaw(entity, partialTick) * MathHelper.RAD_TO_DEG, 0, 1, 0);
        GL11.glScalef(1/ENTITY_RENDER_SCALE, 1/ENTITY_RENDER_SCALE, -1/ENTITY_RENDER_SCALE);

        if(entity.deathTime > 0) {
            float rotationProgress = (((entity.deathTime + partialTick) - 1.0F) / 20F) * 1.6F;
            rotationProgress = MathHelper.sqrt_float(rotationProgress);
            if(rotationProgress > 1.0F) {
                rotationProgress = 1.0F;
            }
            GL11.glRotatef(rotationProgress * getMaxDeathRotation(entity), 0.0F, 0.0F, 1.0F);
        }
    }

    protected float getLimbSwing(final @NotNull T entity, final float partialTick) {
        return entity.walkAnimPos - entity.walkAnimSpeed * (1.0F - partialTick);
    }

    protected float getLimbYaw(final @NotNull T entity, final float partialTick) {
        float limbYaw = MathHelper.lerp(entity.walkAnimSpeedO, entity.walkAnimSpeed, partialTick);
        if(limbYaw > 1.0F) {
            limbYaw = 1.0F;
        }
        return limbYaw;
    }

    protected float getLimbPitch(final @NotNull T entity, final float partialTick) {
        return entity.tickCount + partialTick;
    }

    public float getBodyYaw(final @NotNull T entity, final float partialTick) {
        return MathHelper.lerp(entity.yBodyRotO, entity.yBodyRot, partialTick) * MathHelper.DEG_TO_RAD;
    }

    protected float getHeadYaw(final @NotNull T entity, final float partialTick) {
        return MathHelper.lerp(entity.yRotO, entity.yRot, partialTick) * MathHelper.DEG_TO_RAD;
    }

    protected float getHeadPitch(final @NotNull T entity, final float partialTick) {
        return MathHelper.lerp(entity.xRotO, entity.xRot, partialTick) * MathHelper.DEG_TO_RAD;
    }

    protected void renderSpecials(final @NotNull Tessellator tessellator, final @NotNull T entity, final double x, final double y, final double z)
    {
        if(Minecraft.getMinecraft().canRenderEntityLabel()) {
            renderLivingLabel(tessellator, entity, Integer.toString(entity.id), x, y, z, 64, false);
        }
        else if (!entity.nickname.isEmpty()) {
            renderLivingLabel(tessellator, entity, entity.getDisplayName(), x, y, z, 64, true);
        }
    }

    protected void renderLivingLabel(final @NotNull Tessellator t, final @NotNull T entity, final @NotNull String text, final double x, final double y, final double z, final int maxDistance, final boolean depthTest)
    {
        final float cameraDistance = (float) this.renderDispatcher.camera.distanceTo(entity);
        if (cameraDistance > maxDistance)
        {
            return;
        }
        final Font sr = getFont();
        final float scale = 1.6f/60f;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y + entity.getHeadHeight() + 0.8f, (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderDispatcher.viewLerpYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.renderDispatcher.viewLerpPitch, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        if (!depthTest) GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        t.startDrawingQuads();
        final int halfTextWidth = sr.getStringWidth(text) / 2;
        t.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        t.addVertex(-halfTextWidth - 1, -1, 0.0D);
        t.addVertex(-halfTextWidth - 1,  8, 0.0D);
        t.addVertex( halfTextWidth,      8, 0.0D);
        t.addVertex( halfTextWidth,     -1, 0.0D);
        t.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        sr.drawString(text, -halfTextWidth, 0,0x20ffffff, false);
        if (!depthTest) GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
		sr.drawString(text, -halfTextWidth, 0,0xffffff, false);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    protected int getOverlayColor(final @NotNull T entity, final float brightness, final float partialTick) {
        return 0;
    }

    protected float getMaxDeathRotation(final @NotNull T entity)
    {
        return 90F;
    }

    protected int maxRenderLayer(final @NotNull T entity) {
        return 0;
    }

    protected abstract @Nullable StaticEntityModel getAndSetupModelForLayer(final @NotNull T entity, float brightness, final float partialTick, final int layer);

    protected void renderAdditional(@NotNull final Tessellator tessellator, final @NotNull T entity, final float partialTick)
    {
    }

    public void loadEntityTexture(final @NotNull T entity)
    {
        if (!Minecraft.getMinecraft().gameSettings.mobVariants.value) {
            bindTexture(entity.getDefaultEntityTexture());
        }
        else {
            bindTexture(entity.getEntityTexture());
        }
    }

    @Override
    public @NotNull AABB entityViewBox(@NotNull final T entity) {
//        return this.entityModel.visibleBounds().cloneMove(entity.x, entity.y, entity.z);
        return super.entityViewBox(entity);
    }

	/*
	 * Animation Util
	 */
	protected void animateWalk(StaticEntityModelMojang staticEntityModelMojang, Animation animationData, float p_268057_, float p_268347_, float p_268138_, float p_268165_) {
		long time = (long) (p_268057_ * 50.0F * p_268138_);
		float scale = Math.min(p_268347_ * p_268165_, 1.0F);
		AnimationHelper.animate(staticEntityModelMojang, animationData, time, scale, VEC_ANIMATION);
	}

	protected void applyStatic(StaticEntityModelMojang staticEntityModelMojang, Animation animationData) {
		AnimationHelper.animate(staticEntityModelMojang, animationData, 0L, 1.0F, VEC_ANIMATION);
	}

	protected void animate(StaticEntityModelMojang staticEntityModelMojang, AnimationState animationState, Animation animationData, float p_233388_, float p_233389_) {
		animationState.updateTime(p_233388_, p_233389_);
		animationState.ifStarted(p_233392_ -> AnimationHelper.animate(staticEntityModelMojang, animationData, p_233392_.getAccumulatedTime(), 1.0F, VEC_ANIMATION));
	}
}
