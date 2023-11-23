package useless.dragonfly.mixins.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlockCache;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.dragonfly.model.block.BlockModelDragonFly;
import useless.dragonfly.DragonFly;
import useless.dragonfly.mixins.mixininterfaces.ExtraRendering;
import useless.dragonfly.model.block.processed.BlockCube;
import useless.dragonfly.model.block.processed.BlockFace;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.registries.TextureRegistry;

import static useless.dragonfly.DragonFly.terrainAtlasWidth;

@Mixin(value = RenderBlocks.class, remap = false)
public abstract class RenderBlocksMixin implements ExtraRendering {
	@Shadow
	private Minecraft mc;
	@Shadow
	private World world;
	@Shadow
	private boolean enableAO;
	@Shadow
	private WorldSource blockAccess;
	@Shadow
	private RenderBlockCache cache;
	@Shadow
	public abstract float getBlockBrightness(WorldSource blockAccess, int x, int y, int z);
	@Shadow
	private boolean renderAllFaces;
	@Shadow
	private int overrideBlockTexture;
	@Shadow
	public boolean overbright;
	@Shadow
	private float colorRedTopRight;
	@Shadow
	private float colorRedBottomRight;
	@Shadow
	private float colorRedBottomLeft;
	@Shadow
	private float colorGreenTopRight;
	@Shadow
	private float colorRedTopLeft;
	@Shadow
	private float colorGreenBottomRight;
	@Shadow
	private float colorGreenBottomLeft;
	@Shadow
	private float colorGreenTopLeft;
	@Shadow
	private float colorBlueTopRight;
	@Shadow
	private float colorBlueBottomRight;
	@Shadow
	private float colorBlueBottomLeft;
	@Shadow
	private float colorBlueTopLeft;
	@Shadow
	@Final
	private static float[] SIDE_LIGHT_MULTIPLIER;

	@Inject(method = "renderBlockOnInventory(Lnet/minecraft/core/block/Block;IF)V", at = @At("HEAD"), cancellable = true)
	public void redirectRenderer(Block block, int metadata, float brightness, CallbackInfo ci){
		if (BlockModelDispatcher.getInstance().getDispatch(block) instanceof BlockModelDragonFly){
			GL11.glColor4f(1,1,1,1);
			BlockModelDragonFly blockModelDragonFly = (BlockModelDragonFly) BlockModelDispatcher.getInstance().getDispatch(block);
			renderModelInventory(blockModelDragonFly, block, metadata, brightness);
			ci.cancel();
		}
	}
	@Unique
	public void renderModelInventory(BlockModelDragonFly modelDragonFly, Block block, int meta, float brightness){
		float yOffset = 0.5f;
		Tessellator tessellator = Tessellator.instance;
		GL11.glTranslatef(-0.5f, 0.0f - yOffset, -0.5f);
		if (modelDragonFly.baseModel.blockCubes != null){
			for (BlockCube cube: modelDragonFly.baseModel.blockCubes) {
				for (BlockFace face: cube.faces.values()) {
					tessellator.startDrawingQuads();
					tessellator.setNormal(face.getSide().getOffsetX(), face.getSide().getOffsetY(), face.getSide().getOffsetZ());
					if (face.useTint()){
						int color = BlockColorDispatcher.getInstance().getDispatch(block).getFallbackColor(meta);
						float r = (float)(color >> 16 & 0xFF) / 255.0f;
						float g = (float)(color >> 8 & 0xFF) / 255.0f;
						float b = (float)(color & 0xFF) / 255.0f;
						GL11.glColor4f(r, g, b, 1);
					}
					renderModelFace(cube, face.getSide(), 0, 0, 0);
					tessellator.draw();
					GL11.glColor4f(1, 1, 1, 1);
				}
			}
		}
		GL11.glTranslatef(0.5f, 0.5f, 0.5f);
	}
	@Unique
	public boolean renderModelNormal(BlockModel model, Block block, int x, int y, int z) {
//		int color = BlockColorDispatcher.getInstance().getDispatch(block).getWorldColor(this.world, x, y, z);
//		float red = (float)(color >> 16 & 0xFF) / 255.0f;
//		float green = (float)(color >> 8 & 0xFF) / 255.0f;
//		float blue = (float)(color & 0xFF) / 255.0f;
		if (mc.isAmbientOcclusionEnabled() && model.getAO()) {
			return this.renderStandardModelWithAmbientOcclusion(model, block, x, y, z);
		}
		return this.renderStandardModelWithColorMultiplier(model, block, x, y, z, 1, 1, 1);
	}
	@Unique
	public boolean renderModelNoCulling(BlockModel model, Block block, int x, int y, int z){
		this.renderAllFaces = true;
		boolean result = this.renderModelNormal(model, block, x, y, z);
		this.renderAllFaces = false;
		return result;
	}
	@Unique
	public boolean renderModelBlockUsingTexture(BlockModel model, Block block, int x, int y, int z, int textureIndex){
		this.overrideBlockTexture = textureIndex;
		boolean result = this.renderModelNormal(model, block, x, y, z);
		this.overrideBlockTexture = -1;
		return result;
	}
	@Unique
	public boolean renderStandardModelWithAmbientOcclusion(BlockModel model, Block block, int x, int y, int z) {
		this.enableAO = true;
		this.cache.setupCache(block, this.blockAccess, x, y, z);
		boolean somethingRendered = false;
		for (BlockCube cube: model.blockCubes) {
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.BOTTOM, cube.yMin(), 0, 0, 1, cube.zMax(), cube.zMin(), -1, 0, 0, 1.0F - cube.xMin(), 1.0F - cube.xMax());
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.TOP, 1.0F - cube.yMax(), 0, 0, 1, cube.zMax(), cube.zMin(), 1, 0, 0, cube.xMax(), cube.xMin());
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.NORTH, cube.zMin(), -1, 0, 0, 1.0F - cube.xMin(), 1.0F - cube.xMax(), 0, 1, 0, cube.yMax(), cube.yMin());
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.SOUTH, 1.0F - cube.zMax(), 0, 1, 0, cube.yMax(), cube.yMin(), -1, 0, 0, 1.0F - cube.xMin(), 1.0F - cube.xMax());
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.WEST, cube.xMin(), 0, 0, 1, cube.zMax(), cube.zMin(), 0, 1, 0, cube.yMax(), cube.yMin());
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.EAST, 1.0F - cube.xMax(), 0, 0, 1, cube.zMax(), cube.zMin(), 0, -1, 0, 1.0F - cube.yMin(), 1.0F - cube.yMax());
		}
		this.enableAO = false;
		return somethingRendered;
	}
	@Unique
	public boolean renderSide(BlockModel model, BlockCube cube, Side side, int x, int y, int z){
		if (cube.isOuterFace(side)){
			return !cube.getFaceFromSide(side).cullFace(x, y, z, blockAccess);
		}
		return true;
	}
	@Unique
	public boolean renderModelSide(BlockModel model, BlockCube cube, Block block, int x, int y, int z, Side side, float depth, int topX, int topY, int topZ, float topP, float botP, int lefX, int lefY, int lefZ, float lefP, float rigP) {
		if (cube.getFaceFromSide(side) == null) return false;
		int dirX = side.getOffsetX();
		int dirY = side.getOffsetY();
		int dirZ = side.getOffsetZ();

		int color;
		float r;
		float g;
		float b;
		if (cube.getFaceFromSide(side).useTint()){
			color = BlockColorDispatcher.getInstance().getDispatch(block).getWorldColor(this.world, x, y, z);
			r = (float)(color >> 16 & 0xFF) / 255.0f;
			g = (float)(color >> 8 & 0xFF) / 255.0f;
			b = (float)(color & 0xFF) / 255.0f;
		} else {
			r = g = b = 1f;
		}

		if (!this.renderAllFaces){
			if (!renderSide(model, cube, side, x, y, z)) return false;
		}
		float lightTL;
		float lightBL;
		float lightBR;
		float lightTR;
		if (this.overbright) {
			lightTR = 1.0f;
			lightBR = 1.0f;
			lightBL = 1.0f;
			lightTL = 1.0f;
		} else {
			float dirB = this.cache.getBrightness(dirX, dirY, dirZ);
			boolean lefT = this.cache.getOpacity(dirX + lefX, dirY + lefY, dirZ + lefZ);
			boolean botT = this.cache.getOpacity(dirX - topX, dirY - topY, dirZ - topZ);
			boolean topT = this.cache.getOpacity(dirX + topX, dirY + topY, dirZ + topZ);
			boolean rigT = this.cache.getOpacity(dirX - lefX, dirY - lefY, dirZ - lefZ);
			float lB = this.cache.getBrightness(dirX + lefX, dirY + lefY, dirZ + lefZ);
			float bB = this.cache.getBrightness(dirX - topX, dirY - topY, dirZ - topZ);
			float tB = this.cache.getBrightness(dirX + topX, dirY + topY, dirZ + topZ);
			float rB = this.cache.getBrightness(dirX - lefX, dirY - lefY, dirZ - lefZ);
			float blB = botT && lefT ? lB : this.cache.getBrightness(dirX + lefX - topX, dirY + lefY - topY, dirZ + lefZ - topZ);
			float tlB = topT && lefT ? lB : this.cache.getBrightness(dirX + lefX + topX, dirY + lefY + topY, dirZ + lefZ + topZ);
			float brB = botT && rigT ? rB : this.cache.getBrightness(dirX - lefX - topX, dirY - lefY - topY, dirZ - lefZ - topZ);
			float trB = topT && rigT ? rB : this.cache.getBrightness(dirX - lefX + topX, dirY - lefY + topY, dirZ - lefZ + topZ);
			lightTL = (tlB + lB + tB + dirB) / 4.0f;
			lightTR = (tB + dirB + trB + rB) / 4.0f;
			lightBR = (dirB + bB + rB + brB) / 4.0f;
			lightBL = (lB + blB + dirB + bB) / 4.0f;
			if ((double)depth > 0.01) {
				dirB = this.cache.getBrightness(0, 0, 0);
				lefT = this.cache.getOpacity(lefX, lefY, lefZ);
				botT = this.cache.getOpacity(-topX, -topY, -topZ);
				topT = this.cache.getOpacity(topX, topY, topZ);
				rigT = this.cache.getOpacity(-lefX, -lefY, -lefZ);
				lB = this.cache.getBrightness(lefX, lefY, lefZ);
				bB = this.cache.getBrightness(-topX, -topY, -topZ);
				tB = this.cache.getBrightness(topX, topY, topZ);
				rB = this.cache.getBrightness(-lefX, -lefY, -lefZ);
				blB = botT && lefT ? lB : this.cache.getBrightness(lefX - topX, lefY - topY, lefZ - topZ);
				tlB = topT && lefT ? lB : this.cache.getBrightness(lefX + topX, lefY + topY, lefZ + topZ);
				brB = botT && rigT ? rB : this.cache.getBrightness(-lefX - topX, -lefY - topY, -lefZ - topZ);
				trB = topT && rigT ? rB : this.cache.getBrightness(-lefX + topX, -lefY + topY, -lefZ + topZ);
				lightTL = (tlB + lB + tB + dirB) / 4.0f * depth + lightTL * (1.0f - depth);
				lightTR = (tB + dirB + trB + rB) / 4.0f * depth + lightTR * (1.0f - depth);
				lightBR = (dirB + bB + rB + brB) / 4.0f * depth + lightBR * (1.0f - depth);
				lightBL = (lB + blB + dirB + bB) / 4.0f * depth + lightBL * (1.0f - depth);
			}
		}
		if (this.overbright) {
			this.colorRedTopRight = r;
			this.colorRedBottomRight = r;
			this.colorRedBottomLeft = r;
			this.colorRedTopLeft = r;
			this.colorGreenTopRight = g;
			this.colorGreenBottomRight = g;
			this.colorGreenBottomLeft = g;
			this.colorGreenTopLeft = g;
			this.colorBlueTopRight = b;
			this.colorBlueBottomRight = b;
			this.colorBlueBottomLeft = b;
			this.colorBlueTopLeft = b;
		} else {
			this.colorRedBottomRight = this.colorRedTopRight = r * SIDE_LIGHT_MULTIPLIER[side.getId()];
			this.colorRedBottomLeft = this.colorRedTopRight;
			this.colorRedTopLeft = this.colorRedTopRight;
			this.colorGreenBottomRight = this.colorGreenTopRight = g * SIDE_LIGHT_MULTIPLIER[side.getId()];
			this.colorGreenBottomLeft = this.colorGreenTopRight;
			this.colorGreenTopLeft = this.colorGreenTopRight;
			this.colorBlueBottomRight = this.colorBlueTopRight = b * SIDE_LIGHT_MULTIPLIER[side.getId()];
			this.colorBlueBottomLeft = this.colorBlueTopRight;
			this.colorBlueTopLeft = this.colorBlueTopRight;
		}
		float tl = topP * lightTL + (1.0f - topP) * lightBL;
		float tr = topP * lightTR + (1.0f - topP) * lightBR;
		float bl2 = botP * lightTL + (1.0f - botP) * lightBL;
		float br = botP * lightTR + (1.0f - botP) * lightBR;
		float ltl = lefP * tl + (1.0f - lefP) * tr;
		float lbl = lefP * bl2 + (1.0f - lefP) * br;
		float lbr = rigP * bl2 + (1.0f - rigP) * br;
		float ltr = rigP * tl + (1.0f - rigP) * tr;
		this.colorRedTopLeft *= ltl;
		this.colorGreenTopLeft *= ltl;
		this.colorBlueTopLeft *= ltl;
		this.colorRedBottomLeft *= lbl;
		this.colorGreenBottomLeft *= lbl;
		this.colorBlueBottomLeft *= lbl;
		this.colorRedBottomRight *= lbr;
		this.colorGreenBottomRight *= lbr;
		this.colorBlueBottomRight *= lbr;
		this.colorRedTopRight *= ltr;
		this.colorGreenTopRight *= ltr;
		this.colorBlueTopRight *= ltr;
		renderModelFace( cube, side, x, y, z);
		return true;
	}
	@Unique
	public void renderModelFace(BlockCube cube, Side side, double x, double y, double z) {
		BlockFace face = cube.getFaceFromSide(side);
		Tessellator tessellator = Tessellator.instance;
		double[] uvTL;
		double[] uvBL;
		double[] uvBR;
		double[] uvTR;
		if (this.overrideBlockTexture >= 0) {
			uvTL = face.generateVertexUV(overrideBlockTexture, 0);
			uvBL = face.generateVertexUV(overrideBlockTexture, 1);
			uvBR = face.generateVertexUV(overrideBlockTexture, 2);
			uvTR = face.generateVertexUV(overrideBlockTexture, 3);
		} else {
			uvTL = face.vertexUVs[0];
			uvBL = face.vertexUVs[1];
			uvBR = face.vertexUVs[2];
			uvTR = face.vertexUVs[3];
		}

		if (this.enableAO) {
			// Top Left
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.addVertexWithUV(x + face.vertices[0].x, y + face.vertices[0].y, z + face.vertices[0].z, uvTL[0], uvTL[1]);

			// Bottom Left
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.addVertexWithUV(x + face.vertices[1].x, y + face.vertices[1].y, z + face.vertices[1].z, uvBL[0], uvBL[1]);

			// Bottom Right
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.addVertexWithUV(x + face.vertices[2].x, y + face.vertices[2].y, z + face.vertices[2].z, uvBR[0], uvBR[1]);

			// Top Right
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.addVertexWithUV(x + face.vertices[3].x, y + face.vertices[3].y, z + face.vertices[3].z, uvTR[0], uvTR[1]);
		} else {
			tessellator.addVertexWithUV(x + face.vertices[0].x, y + face.vertices[0].y, z + face.vertices[0].z, uvTL[0], uvTL[1]); // Top Left
			tessellator.addVertexWithUV(x + face.vertices[1].x, y + face.vertices[1].y, z + face.vertices[1].z, uvBL[0], uvBL[1]); // Bottom Left
			tessellator.addVertexWithUV(x + face.vertices[2].x, y + face.vertices[2].y, z + face.vertices[2].z, uvBR[0], uvBR[1]); // Bottom Right
			tessellator.addVertexWithUV(x + face.vertices[3].x, y + face.vertices[3].y, z + face.vertices[3].z, uvTR[0], uvTR[1]); // Top Right
		}
	}
	@Unique
	public boolean renderStandardModelWithColorMultiplier(BlockModel model, Block block, int x, int y, int z, float r, float g, float b) {
		this.enableAO = false;
		Tessellator tessellator = Tessellator.instance;
		boolean renderedSomething = false;
		float cBottom = 0.5f;
		float cTop = 1.0f;
		float cNorthSouth = 0.8f;
		float cEastWest = 0.6f;
		float rTop = cTop * r;
		float gTop = cTop * g;
		float bTop = cTop * b;
		float rBottom = cBottom;
		float rNorthSouth = cNorthSouth;
		float rEastWest = cEastWest;
		float gBottom = cBottom;
		float gNorthSouth = cNorthSouth;
		float gEastWest = cEastWest;
		float bBottom = cBottom;
		float bNorthSouth = cNorthSouth;
		float bEastWest = cEastWest;
		rBottom *= r;
		rNorthSouth *= r;
		rEastWest *= r;
		gBottom *= g;
		gNorthSouth *= g;
		gEastWest *= g;
		bBottom *= b;
		bNorthSouth *= b;
		bEastWest *= b;
		float blockBrightness = this.getBlockBrightness(this.blockAccess, x, y, z);
		for (BlockCube cube: model.blockCubes) {
			for (Side side: DragonFly.sides) {
				if (cube.getFaceFromSide(side) == null) continue;
				int _x = x + side.getOffsetX();
				int _y = y + side.getOffsetY();
				int _z = z + side.getOffsetZ();

				if (!this.renderAllFaces){
					if (!renderSide(model, cube, side, x, y, z)) continue;
				}

				float sideBrightness;
				if (!cube.isOuterFace(side) && !block.blockMaterial.isLiquid()){
					sideBrightness = blockBrightness;
				} else {
					sideBrightness = getBlockBrightness(this.blockAccess, _x, _y, _z);
				}

				float red;
				float green;
				float blue;

				switch (side){
					case TOP:
						red = rTop;
						green = gTop;
						blue = bTop;
						break;
					case BOTTOM:
						red = rBottom;
						green = gBottom;
						blue = bBottom;
						break;
					case NORTH:
					case SOUTH:
						red = rNorthSouth;
						green = gNorthSouth;
						blue = bNorthSouth;
						break;
					case WEST:
					case EAST:
						red = rEastWest;
						green = gEastWest;
						blue = bEastWest;
						break;
					default:
						throw new RuntimeException("Specified side does not exist on a cube!!!");
				}
				tessellator.setColorOpaque_F(red * sideBrightness, green * sideBrightness, blue * sideBrightness);
				renderModelFace(cube, side, x, y, z);
				renderedSomething = true;
			}
		}
		return renderedSomething;
	}
}
