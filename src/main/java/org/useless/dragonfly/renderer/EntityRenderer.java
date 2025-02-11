package org.useless.dragonfly.renderer;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.ImageParser;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.mixins.EntityRenderDispatcherAccessor;
import org.useless.dragonfly.models.entity.StaticEntityModel;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityRenderer<T extends Entity> extends net.minecraft.client.render.entity.EntityRenderer<T>
{
    public static boolean renderShadows = true;
    private final @NotNull Map<@NotNull String, @NotNull StaticEntityModel> modelMap = new HashMap<>();
    protected Class<? extends T> appliedClass = null;

    private float shadowSize;
    protected float shadowOpacity;

    public EntityRenderer(final float shadowSize)
    {
        this.shadowSize = shadowSize;
        this.shadowOpacity = 1.0F;
    }

    public EntityRenderer()
    {
        this(0.0f);
    }

    public float getShadowSize(final @NotNull T entity)
    {
        return this.shadowSize;
    }

    public abstract void render(@NotNull Tessellator tessellator, @NotNull T entity, double x, double y, double z,
                                float yaw, float partialTick);

    public void renderPreview(@NotNull final Tessellator tessellator, @NotNull final T entity, final double x, final double y, final double z, final float yaw, final float partialTick) {
        render(tessellator, entity, x, y, z, yaw, partialTick);
    }

    protected void bindTexture(final @NotNull String texturePath)
    {
        final TextureManager textureManager = this.renderDispatcher.textureManager;
        textureManager.bindTexture(textureManager.loadTexture(texturePath));
    }
    public boolean bindDownloadableTexture(final String urlTexture, final String backupTexture, final ImageParser imageParser) {
        return this.renderDispatcher.textureManager.bindDownloadableTexture(urlTexture, backupTexture, imageParser);
    }

    protected @NotNull StaticEntityModel getModel(@NotNull final String localId) {
        StaticEntityModel model = this.modelMap.get(localId);
        if (model != null) {
            return model;
        }
        final NamespaceID id = EntityDispatcher.idForClass(this.appliedClass);
        if (id != null) {
            model = EntityGeometryMojangData.Cache.getModelForEntity(id, localId);
        } else {
            model = EntityGeometryMojangData.Cache.getFallbackModel();
        }
        this.modelMap.put(localId, model);
        return model;
    }

    public final @NotNull EntityRenderer<T> setModel(@NotNull final String localId, @NotNull final String modelId, final double inflation) {
        this.modelMap.put(localId, EntityGeometryMojangData.Cache.getModel(modelId, inflation));
        return this;
    }

	@Override
	public void init(final EntityRenderDispatcher dispatcher) {
		super.init(dispatcher);
		for (Map.Entry<Class<?>, net.minecraft.client.render.entity.EntityRenderer<?>> entry : ((EntityRenderDispatcherAccessor)dispatcher).getRenderers().entrySet()) {
			if (entry.getValue() == this) {
				appliedClass = (Class<? extends T>) entry.getKey();
				break;
			}
		}

	}

	private void renderFire(final @NotNull Tessellator tessellator, final @NotNull T entity, final double x, final double y, final double z, final float partialTick)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        final IconCoordinate texture = TextureRegistry.getTexture("minecraft:block/fire");
        texture.parentAtlas.bind();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        final float f9 = entity.bbWidth * 1.4F;
        GL11.glScalef(f9, f9, f9);
        float f10 = 0.5F;
        final float f11 = 0.0F;
        float f12 = entity.bbHeight;
        float f13 = (float)(entity.y - entity.bb.minY);
        GL11.glRotatef(-this.renderDispatcher.viewLerpYaw, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, -0.3F + (float)(int)f12 * 0.02F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(LightmapHelper.isLightmapEnabled()) {
        	int lightmapCoord = entity.getLightmapCoord(partialTick);
        	lightmapCoord = LightmapHelper.setBlocklightValue(lightmapCoord, 15);
        	LightmapHelper.setLightmapCoord(lightmapCoord);
        }
        float f14 = 0.0F;
        int l = 0;
        tessellator.startDrawingQuads();
        while(f12 > 0.0F)
        {
            double f2 = texture.getIconUMin();
            double f4 = texture.getIconUMax();
            final double f6 = texture.getIconVMin();
            final double f8 = texture.getIconVMax();
            if((l / 2) % 2 == 0)
            {
                final double f15 = f4;
                f4 = f2;
                f2 = f15;
            }
            tessellator.addVertexWithUV(f10 - f11, 0.0F - f13, f14, f4, f8);
            tessellator.addVertexWithUV(-f10 - f11, 0.0F - f13, f14, f2, f8);
            tessellator.addVertexWithUV(-f10 - f11, 1.4F - f13, f14, f2, f6);
            tessellator.addVertexWithUV(f10 - f11, 1.4F - f13, f14, f4, f6);
            f12 -= 0.45F;
            f13 -= 0.45F;
            f10 *= 0.9F;
            f14 += 0.03F;
            l++;
        }
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void renderShadow(final @NotNull Tessellator tessellator, @NotNull final T entity, final double posX, final double posY, final double posZ,
                              final float opacity, final float partialTick)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        final TextureManager textureManager = this.renderDispatcher.textureManager;
        textureManager.bindTexture(textureManager.loadTexture("/assets/minecraft/textures/misc/shadow.png"));
        final World world = entity.world;
        GL11.glDepthMask(false);
        final double lerpX = entity.xo + (entity.x - entity.xo) * (double)partialTick;
        final double lerpY = entity.yo + (entity.y - entity.yo) * (double)partialTick + (double)entity.getShadowHeightOffs();
        final double lerpZ = entity.zo + (entity.z - entity.zo) * (double)partialTick;

        final float shadowSize = getShadowSize(entity);

        final int minX = MathHelper.floor(lerpX - (double) shadowSize);
        final int maxX = MathHelper.floor(lerpX + (double) shadowSize);
        final int minY = MathHelper.floor(lerpY - (double) shadowSize);
        final int maxY = MathHelper.floor(lerpY);
        final int minZ = MathHelper.floor(lerpZ - (double) shadowSize);
        final int maxZ = MathHelper.floor(lerpZ + (double) shadowSize);
        final double dxLerp = posX - lerpX;
        final double dyLerp = posY - lerpY;
        final double dzLerp = posZ - lerpZ;
        tessellator.startDrawingQuads();

        for(int blockX = minX; blockX <= maxX; blockX++)
        {
            for(int blockY = minY; blockY <= maxY; blockY++)
            {
                for(int blockZ = minZ; blockZ <= maxZ; blockZ++)
                {
                    final int blockId = world.getBlockId(blockX, blockY - 1, blockZ);
                    if(blockId > 0 && world.getBlockLightValue(blockX, blockY, blockZ) > 3)
                    {
                        renderShadowOnBlock(tessellator, entity, Blocks.blocksList[blockId], posX, posY + (double)entity.getShadowHeightOffs(), posZ, blockX, blockY, blockZ, opacity, shadowSize, dxLerp, dyLerp + (double)entity.getShadowHeightOffs(), dzLerp);
                    }
                }

            }
        }

        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }

    private void renderShadowOnBlock(final @NotNull Tessellator tessellator, final @NotNull T entity, final @NotNull Block<?> block, final double d, final double d1, final double d2,
                                     final int x, final int y, final int z, final float opacity, final float size, final double xo,
                                     final double yo, final double zo)
    {
        if(!block.isCubeShaped())
        {
            return;
        }
        double brightness = entity.world.getLightBrightness(x, y, z);
        if (Global.accessor.isFullbrightEnabled()) brightness = 1.0f;
        double alpha = ((double)opacity - (d1 - ((double)(y + 1) + yo)) / 2D) * 0.5D * brightness;
        if(alpha < 0.0D)
        {
            return;
        }
        if(alpha > 1.0D)
        {
            alpha = 1.0D;
        }
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)alpha);
        final AABB aabb = block.getCollisionBoundingBoxFromPool(entity.world, x, y, z);
        if(aabb == null) {
        	return;
        }
        final double xMin = aabb.minX + xo;
        final double xMax = aabb.maxX + xo;
        final double yMin = aabb.maxY + yo + 0.015625D - 1;
        final double zMin = aabb.minZ + zo;
        final double zMax = aabb.maxZ + zo;
        final float f2 = (float)((d - xMin) / 2D / (double)size + 0.5D);
        final float f3 = (float)((d - xMax) / 2D / (double)size + 0.5D);
        final float f4 = (float)((d2 - zMin) / 2D / (double)size + 0.5D);
        final float f5 = (float)((d2 - zMax) / 2D / (double)size + 0.5D);
        tessellator.addVertexWithUV(xMin, yMin, zMin, f2, f4);
        tessellator.addVertexWithUV(xMin, yMin, zMax, f2, f5);
        tessellator.addVertexWithUV(xMax, yMin, zMax, f3, f5);
        tessellator.addVertexWithUV(xMax, yMin, zMin, f3, f4);
    }

    public static void renderOffsetAABB(final @NotNull Tessellator tessellator, final @NotNull AABB aabb, final double x, final double y, final double z)
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setTranslation(x, y, z);
        tessellator.setNormal(0.0F, 0.0F, -1F);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator.setNormal(0.0F, -1F, 0.0F);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void renderFlatAABB(final @NotNull AABB aabb)
    {
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator.draw();
    }

    public void postRender(final @NotNull Tessellator tessellator, final @NotNull T entity, final double x, final double y, final double z, final float yaw, final float partialTick)
    {
        if(renderShadows && this.renderDispatcher.gameSettings.fancyGraphics.value == 1 && getShadowSize(entity) > 0.0F)
        {
            final double distance = this.renderDispatcher.distanceToLerpSquared(entity.x, entity.y, entity.z);
            final float opacity = (float)((1.0D - distance / 256D) * (double) this.shadowOpacity);
            if(opacity > 0.0F)
            {
                renderShadow(tessellator, entity, x, y, z, opacity, partialTick);
            }
        }
        if(entity.isOnFire())
        {
            renderFire(tessellator, entity, x, y, z, partialTick);
        }
    }

    public @NotNull Font getFont()
    {
        return this.renderDispatcher.getFont();
    }

    public @NotNull AABB entityViewBox(@NotNull final T entity) {
        return entity.bb;
    }
}
