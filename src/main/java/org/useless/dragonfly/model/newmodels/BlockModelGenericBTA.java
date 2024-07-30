package org.useless.dragonfly.model.newmodels;

import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.useless.dragonfly.model.newmodels.generic.ModelEntry;
import org.useless.dragonfly.model.newmodels.generic.StaticModel;
import org.useless.dragonfly.model.newmodels.generic.components.ModelComponent;
import org.useless.dragonfly.utilities.SideUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class BlockModelGenericBTA<T extends Block> extends BlockModel<T> {
	protected static ModelContainer modelContainer = new ModelContainer();
	protected boolean render3d = true;
	protected float renderScale = 0.25f;
	public BlockModelGenericBTA(Block block) {
		super(block);
		hasOverbright = true;
	}
	@Override
	public boolean shouldItemRender3d() {
		return render3d;
	}

	@Override
	public float getItemRenderScale() {
		return renderScale;
	}

	@Override
	public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
		return getBlockTextureFromSideAndMetadata(side, blockAccess.getBlockMetadata(x, y, z));
	}

	@Override
	public IconCoordinate getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
		return getBlockOverbrightTextureFromSideAndMeta(Side.getSideById(side), blockAccess.getBlockMetadata(x, y, z));
	}

	@Override
	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int meta) {
		return getOverlay(meta);
	}
//	@Override // Not a base method in 7.2
	public IconCoordinate getOverlay(int meta){
		modelContainer.reset();
		setInventoryModel(modelContainer,meta);
		return modelContainer.getOverlay();
	}

	@Override
	public boolean render(Tessellator tessellator, int x, int y, int z) {
		hasOverbright = false;
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);

		modelContainer.reset();
		setWorldModel(modelContainer,renderBlocks.blockAccess, x, y, z, meta);

		Collection<ModelEntry> entries = modelContainer.getModels();
		if (!hasOverbright){
			for (ModelEntry e : entries){
				if (e.model.hasOverbright()) {
					hasOverbright = true;
					break;
				}
			}
		}

		int color = BlockColorDispatcher.getInstance().getDispatch(block).getWorldColor(renderBlocks.blockAccess, x, y, z);
		float r = (float)(color >> 16 & 0xff) / 255F;
		float g = (float)(color >> 8 & 0xff) / 255F;
		float b = (float)(color & 0xff) / 255F;

		boolean flag = false;

		renderBlocks.cache.setupCache(block, renderBlocks.blockAccess, x, y, z);
		for (ModelEntry modelEntry : entries){
			ModelComponent[] components = modelEntry.model.getComponents();
			if (components == null) continue;
			for (ModelComponent component : components){
				flag |= component.drawComponentInWorld(this, modelEntry, tessellator, renderBlocks.blockAccess, block, meta,
					x, y, z, r, g, b);
			}
		}

		return flag;
	}

	@Override
	public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightMapCoord) {
		modelContainer.reset();
		setInventoryModel(modelContainer, metadata);

		int color = BlockColorDispatcher.getInstance().getDispatch(block).getFallbackColor(metadata);
		float r = (float)(color >> 16 & 0xff) / 255F;
		float g = (float)(color >> 8 & 0xff) / 255F;
		float b = (float)(color & 0xff) / 255F;

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		for (ModelEntry entry : modelContainer.getModels()){
			ModelComponent[] components = entry.model.getComponents();
			if (components == null) continue;
			for (ModelComponent component : components){
				component.drawComponent(entry, tessellator, metadata, 0, 0, 0, r, g, b, brightness, alpha);
			}
		}

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	@Override
	public IconCoordinate getParticleTexture(Side side, int meta) {
		modelContainer.reset();
		setInventoryModel(modelContainer, meta);
		return modelContainer.getParticleSide(side);
	}

//	@Override this replaces the above method in the future // TODO fix vanilla particle to use worldspace info
	public IconCoordinate getParticleTexture(WorldSource worldSource, int x, int y, int z, Side side, int meta){
		modelContainer.reset();
		setWorldModel(modelContainer, worldSource, x, y, z, meta);
		return modelContainer.getParticleSide(side);
	}
	@Override
	public boolean shouldSideBeRendered(WorldSource worldSource, int x, int y, int z, int sideInt) {
		return shouldSideBeRendered(worldSource, x, y, z, sideInt, 0);
	}

	@Override
	public boolean shouldSideBeRendered(WorldSource blockAccess, int x, int y, int z, int sideInt, int meta)
	{
		Side side = Side.getSideById(sideInt);
		Block checkBlock = blockAccess.getBlock(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ());
		if (checkBlock == null) return true;
		switch (side){
			case BOTTOM:
				if (checkBlock.minY > 0.0D) return true;
				break;
			case TOP:
				if (checkBlock.maxY < 1.0D) return true;
				break;
			case NORTH:
				if (checkBlock.minZ > 0.0D) return true;
				break;
			case SOUTH:
				if (checkBlock.maxZ < 1.0D) return true;
				break;
			case WEST:
				if (checkBlock.minX > 0.0D) return true;
				break;
			case EAST:
				if (checkBlock.maxX < 1.0D) return true;
				break;
		}
		return !blockAccess.isBlockOpaqueCube(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ());
	}
	@Override
	public boolean shouldSideBeColored(WorldSource blockAccess, int x, int y, int z, int sideInt, int meta){
		Side side = Side.getSideById(sideInt);
		modelContainer.reset();
		setWorldModel(modelContainer,blockAccess, x, y, z, meta);
		return modelContainer.shouldParticleBeColored(side);
	}
	public BlockModelGenericBTA<?> setRender3d(boolean render3d){
		this.render3d = render3d;
		return this;
	}

	public BlockModelGenericBTA<?> setRenderScale(float renderScale){
		this.renderScale = renderScale;
		return this;
	}
	public abstract void setWorldModel(ModelContainer modelContainer, WorldSource worldSource, int x, int y, int z, int meta);
	public abstract void setInventoryModel(ModelContainer modelContainer, int meta);

	public static class ModelContainer {
		private final Map<String, ModelEntry> modelEntries = new HashMap<>();

		public void reset(){
			modelEntries.clear();
		}
		public void addModel(StaticModel model){
			addModel(String.valueOf(modelEntries.size() + 1), model);
		}
		public void addModel(String identifier, StaticModel model){
			modelEntries.put(identifier, new ModelEntry(model));
		}
		public void addModel(String identifier, StaticModel model, float rotationX, float rotationY, float rotationZ){
			ModelEntry e = new ModelEntry(model);
			e.setRotationX(rotationX);
			e.setRotationY(rotationY);
			e.setRotationZ(rotationZ);
			modelEntries.put(identifier, e);
		}
		public void rotateModelX(String identifier, float rotationX){
			modelEntries.get(identifier).setRotationX(rotationX);
		}
		public void rotateModelY(String identifier, float rotationY){
			modelEntries.get(identifier).setRotationY(rotationY);
		}
		public void rotateModelZ(String identifier, float rotationZ){
			modelEntries.get(identifier).setRotationZ(rotationZ);
		}
		public void offsetModelX(String identifier, float offsetX){
			modelEntries.get(identifier).setOffsetX(offsetX);
		}
		public void offsetModelY(String identifier, float offsetY){
			modelEntries.get(identifier).setOffsetY(offsetY);
		}
		public void offsetModelZ(String identifier, float offsetZ){
			modelEntries.get(identifier).setOffsetZ(offsetZ);
		}
		@Nullable
		public IconCoordinate getOverlay(){
			Collection<ModelEntry> entries = getModels();
			for (ModelEntry m : entries){
				IconCoordinate c = m.model.getOverlay();
				if (c != null) return c;
			}
			return null;
		}
		@Nullable
		public IconCoordinate getParticleSide(Side side){
			Collection<ModelEntry> entries = getModels();
			for (ModelEntry m : entries){
				IconCoordinate c = m.model.getParticle(SideUtils.rotateSide(m.get90DegRotationX(), -m.get90DegRotationY(), m.get90DegRotationZ(), side));
				if (c != null) return c;
			}
			return null;
		}
		public boolean shouldParticleBeColored(Side side){
			Collection<ModelEntry> entries = getModels();
			for (ModelEntry m : entries){
				Boolean c = m.model.useParticleColor(SideUtils.rotateSide(m.get90DegRotationX(), -m.get90DegRotationY(), m.get90DegRotationZ(), side));
				if (c != null) return c;
			}
			return false;
		}
		public Collection<ModelEntry> getModels(){
			return modelEntries.values();
		}
	}
}
