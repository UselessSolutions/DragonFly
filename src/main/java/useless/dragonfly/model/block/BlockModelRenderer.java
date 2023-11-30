package useless.dragonfly.model.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.lwjgl.opengl.GL11;
import useless.dragonfly.DragonFly;
import useless.dragonfly.mixins.mixin.accessor.RenderBlocksAccessor;
import useless.dragonfly.model.block.data.PositionData;
import useless.dragonfly.model.block.processed.BlockCube;
import useless.dragonfly.model.block.processed.BlockFace;
import useless.dragonfly.model.block.processed.BlockModel;
import useless.dragonfly.utilities.vector.Vector3f;

import java.lang.reflect.Field;

public class BlockModelRenderer {
	public static Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
	private static boolean enableAO = false;
	private static boolean renderAllFaces = false;
	private static float colorRedTopRight;
	private static float colorRedBottomRight;
	private static float colorRedBottomLeft;
	private static float colorGreenTopRight;
	private static float colorRedTopLeft;
	private static float colorGreenBottomRight;
	private static float colorGreenBottomLeft;
	private static float colorGreenTopLeft;
	private static float colorBlueTopRight;
	private static float colorBlueBottomRight;
	private static float colorBlueBottomLeft;
	private static float colorBlueTopLeft;
	private static int overrideBlockTexture = -1;
	private static int rotationX = 0;
	private static int rotationY = 0;
	public static void renderModelInventory(BlockModelDragonFly modelDragonFly, Block block, int meta, float brightness){
		float xOffset = 0.5f;
		float yOffset = 0.5f;
		float zOffset = 0.5f;
		float xScale = 1f;
		float yScale = 1f;
		float zScale = 1f;
		PositionData displayData = modelDragonFly.baseModel.getDisplayPosition("firstperson_righthand");
		if (displayData != null){
			xOffset -= (float) displayData.translation[0]/16f;
			yOffset -= (float) displayData.translation[1]/16f;
			zOffset -= (float) displayData.translation[2]/16f;

			xScale = (float) displayData.scale[0];
			yScale = (float) displayData.scale[1];
			zScale = (float) displayData.scale[2];

			GL11.glRotatef((float) displayData.rotation[0], 1, 0 ,0);
			GL11.glRotatef((float) displayData.rotation[1], 0, 1 ,0);
			GL11.glRotatef((float) displayData.rotation[2], 0, 0 ,1);
		}
		Tessellator tessellator = Tessellator.instance;
		GL11.glRotatef(90f, 0, 1, 0);
		GL11.glTranslatef(-xOffset, -yOffset, -zOffset);
		GL11.glScalef(xScale, yScale, zScale);
		if (modelDragonFly.baseModel.blockCubes != null){
			for (BlockCube cube: modelDragonFly.baseModel.blockCubes) {
				for (BlockFace face: cube.faces.values()) {
					GL11.glColor4f(brightness, brightness, brightness, 1);
					tessellator.startDrawingQuads();
					tessellator.setNormal(face.getSide().getOffsetX(), face.getSide().getOffsetY(), face.getSide().getOffsetZ());
					if (face.useTint()){
						int color = BlockColorDispatcher.getInstance().getDispatch(block).getFallbackColor(meta);
						float r = (float)(color >> 16 & 0xFF) / 255.0f;
						float g = (float)(color >> 8 & 0xFF) / 255.0f;
						float b = (float)(color & 0xFF) / 255.0f;
						GL11.glColor4f(r * brightness, g * brightness, b * brightness, 1);
					}
					renderModelFace(face, 0, 0, 0);
					tessellator.draw();
				}
			}
		}
		GL11.glTranslatef(xOffset, yOffset, zOffset);
	}
	public static boolean renderModelNormal(BlockModel model, Block block, int x, int y, int z, int rotationX, int rotationY) {
		BlockModelRenderer.rotationX = rotationX;
		BlockModelRenderer.rotationY = rotationY;
		if (rotationX % 90 != 0 || rotationY % 90 != 0) throw new IllegalArgumentException("Rotation must be a multiple of 90!!");
		boolean didRender;
		if (mc.isAmbientOcclusionEnabled() && model.getAO()) {
			didRender = renderStandardModelWithAmbientOcclusion(model, block, x, y, z);
		} else {
			didRender = renderStandardModelWithColorMultiplier(model, block, x, y, z, 1, 1, 1);
		}
		BlockModelRenderer.rotationX = 0;
		BlockModelRenderer.rotationY = 0;
		return didRender;
	}

	public static boolean renderModelNoCulling(BlockModel model, Block block, int x, int y, int z, int rotationX, int rotationY) {
		renderAllFaces = true;
		boolean result = renderModelNormal(model, block, x, y, z, rotationX, rotationY);
		renderAllFaces = false;
		return result;
	}

	public static boolean renderModelBlockUsingTexture(BlockModel model, Block block, int x, int y, int z, int textureIndex, int rotationX, int rotationY) {
		overrideBlockTexture = textureIndex;
		boolean result = renderModelNormal(model, block, x, y, z, rotationX, rotationY);
		overrideBlockTexture = -1;
		return result;
	}
	public static boolean renderStandardModelWithAmbientOcclusion(BlockModel model, Block block, int x, int y, int z) {
		enableAO = true;
		rba().getCache().setupCache(block, rba().getBlockAccess(), x, y, z);
		boolean somethingRendered = false;
		Vector3f origin = new Vector3f(0.5f, 0.5f, 0.5f);
		for (BlockCube cube: model.blockCubes) {
			Vector3f vMin = cube.getMin().rotateAroundX(origin, rotationX).rotateAroundY(origin, rotationY);
			Vector3f vMax = cube.getMax().rotateAroundX(origin, rotationX).rotateAroundY(origin, rotationY);
			float minX = Math.min(vMin.x, vMax.x);
			float minY = Math.min(vMin.y, vMax.y);
			float minZ = Math.min(vMin.z, vMax.z);
			float maxX = Math.max(vMin.x, vMax.x);
			float maxY = Math.max(vMin.y, vMax.y);
			float maxZ = Math.max(vMin.z, vMax.z);
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.BOTTOM, minY, 0, 0, 1, maxZ, minZ, -1, 0, 0, 1.0F - minX, 1.0F - maxX);
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.TOP, 1.0F - maxY, 0, 0, 1, maxZ, minZ, 1, 0, 0, maxX, minX);
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.NORTH, minZ, -1, 0, 0, 1.0F - minX, 1.0F - maxX, 0, 1, 0, maxY, minY);
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.SOUTH, 1.0F - maxZ, 0, 1, 0, maxY, minY, -1, 0, 0, 1.0F - minX, 1.0F - maxX);
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.WEST, minX, 0, 0, 1, maxZ, minZ, 0, 1, 0, maxY, minY);
			somethingRendered |= renderModelSide(model, cube, block, x, y, z, Side.EAST, 1.0F - maxX, 0, 0, 1, maxZ, minZ, 0, -1, 0, 1.0F - minY, 1.0F - maxY);
		}
		enableAO = false;
		return somethingRendered;
	}
	public static boolean renderModelSide(BlockModel model, BlockCube cube, Block block, int x, int y, int z, Side side, float depth, int topX, int topY, int topZ, float topP, float botP, int lefX, int lefY, int lefZ, float lefP, float rigP) {
		BlockFace blockFace = cube.getFaceFromSide(side, rotationX, rotationY);
		if (blockFace == null) return false;
		int directionX = side.getOffsetX();
		int directionY = side.getOffsetY();
		int directionZ = side.getOffsetZ();

		float r = 1f;
		float g = 1f;
		float b = 1f;
		if (blockFace.useTint()){
			int color;
			color = BlockColorDispatcher.getInstance().getDispatch(block).getWorldColor(mc.theWorld, x, y, z);
			r = (float)(color >> 16 & 0xFF) / 255.0f;
			g = (float)(color >> 8 & 0xFF) / 255.0f;
			b = (float)(color & 0xFF) / 255.0f;
		}
		if (!renderAllFaces){
			if (!renderSide(model, cube, side, x, y, z)) return false;
		}
		float lightTR = 1.0f;
		float lightBR = 1.0f;
		float lightBL = 1.0f;
		float lightTL = 1.0f;
		if (!(rba().getOverbright() || !cube.shade())) {
			r *= rba().getSIDE_LIGHT_MULTIPLIER()[side.getId()];
			g *= rba().getSIDE_LIGHT_MULTIPLIER()[side.getId()];
			b *= rba().getSIDE_LIGHT_MULTIPLIER()[side.getId()];
			float directionBrightness = rba().getCache().getBrightness(directionX, directionY, directionZ);
			boolean lefT = rba().getCache().getOpacity(directionX + lefX, directionY + lefY, directionZ + lefZ);
			boolean botT = rba().getCache().getOpacity(directionX - topX, directionY - topY, directionZ - topZ);
			boolean topT = rba().getCache().getOpacity(directionX + topX, directionY + topY, directionZ + topZ);
			boolean rigT = rba().getCache().getOpacity(directionX - lefX, directionY - lefY, directionZ - lefZ);
			float leftBrightness = rba().getCache().getBrightness(directionX + lefX, directionY + lefY, directionZ + lefZ);
			float bottomBrightness = rba().getCache().getBrightness(directionX - topX, directionY - topY, directionZ - topZ);
			float topBrightness = rba().getCache().getBrightness(directionX + topX, directionY + topY, directionZ + topZ);
			float rightBrightness = rba().getCache().getBrightness(directionX - lefX, directionY - lefY, directionZ - lefZ);
			float bottomLeftBrightness = botT && lefT ? leftBrightness : rba().getCache().getBrightness(directionX + lefX - topX, directionY + lefY - topY, directionZ + lefZ - topZ);
			float topLeftBrightness = topT && lefT ? leftBrightness : rba().getCache().getBrightness(directionX + lefX + topX, directionY + lefY + topY, directionZ + lefZ + topZ);
			float bottomRightBrightness = botT && rigT ? rightBrightness : rba().getCache().getBrightness(directionX - lefX - topX, directionY - lefY - topY, directionZ - lefZ - topZ);
			float topRightBrightness = topT && rigT ? rightBrightness : rba().getCache().getBrightness(directionX - lefX + topX, directionY - lefY + topY, directionZ - lefZ + topZ);
			lightTL = (topLeftBrightness + leftBrightness + topBrightness + directionBrightness) / 4.0f;
			lightTR = (topBrightness + directionBrightness + topRightBrightness + rightBrightness) / 4.0f;
			lightBR = (directionBrightness + bottomBrightness + rightBrightness + bottomRightBrightness) / 4.0f;
			lightBL = (leftBrightness + bottomLeftBrightness + directionBrightness + bottomBrightness) / 4.0f;
			if (depth > 0.01) {
				directionBrightness = rba().getCache().getBrightness(0, 0, 0);
				lefT = rba().getCache().getOpacity(lefX, lefY, lefZ);
				botT = rba().getCache().getOpacity(-topX, -topY, -topZ);
				topT = rba().getCache().getOpacity(topX, topY, topZ);
				rigT = rba().getCache().getOpacity(-lefX, -lefY, -lefZ);
				leftBrightness = rba().getCache().getBrightness(lefX, lefY, lefZ);
				bottomBrightness = rba().getCache().getBrightness(-topX, -topY, -topZ);
				topBrightness = rba().getCache().getBrightness(topX, topY, topZ);
				rightBrightness = rba().getCache().getBrightness(-lefX, -lefY, -lefZ);
				bottomLeftBrightness = botT && lefT ? leftBrightness : rba().getCache().getBrightness(lefX - topX, lefY - topY, lefZ - topZ);
				topLeftBrightness = topT && lefT ? leftBrightness : rba().getCache().getBrightness(lefX + topX, lefY + topY, lefZ + topZ);
				bottomRightBrightness = botT && rigT ? rightBrightness : rba().getCache().getBrightness(-lefX - topX, -lefY - topY, -lefZ - topZ);
				topRightBrightness = topT && rigT ? rightBrightness : rba().getCache().getBrightness(-lefX + topX, -lefY + topY, -lefZ + topZ);
				lightTL = (topLeftBrightness + leftBrightness + topBrightness + directionBrightness) / 4.0f * depth + lightTL * (1.0f - depth);
				lightTR = (topBrightness + directionBrightness + topRightBrightness + rightBrightness) / 4.0f * depth + lightTR * (1.0f - depth);
				lightBR = (directionBrightness + bottomBrightness + rightBrightness + bottomRightBrightness) / 4.0f * depth + lightBR * (1.0f - depth);
				lightBL = (leftBrightness + bottomLeftBrightness + directionBrightness + bottomBrightness) / 4.0f * depth + lightBL * (1.0f - depth);
			}
		}

		colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = r;
		colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = g;
		colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = b;

		float tl = topP * lightTL + (1.0f - topP) * lightBL;
		float tr = topP * lightTR + (1.0f - topP) * lightBR;
		float bl2 = botP * lightTL + (1.0f - botP) * lightBL;
		float br = botP * lightTR + (1.0f - botP) * lightBR;
		float brightnessTopRight = lefP * tl + (1.0f - lefP) * tr;
		float brightnessBottomLeft = lefP * bl2 + (1.0f - lefP) * br;
		float bottomRightBrightness = rigP * bl2 + (1.0f - rigP) * br;
		float topRightBrightness = rigP * tl + (1.0f - rigP) * tr;
		colorRedTopLeft *= brightnessTopRight;
		colorGreenTopLeft *= brightnessTopRight;
		colorBlueTopLeft *= brightnessTopRight;
		colorRedBottomLeft *= brightnessBottomLeft;
		colorGreenBottomLeft *= brightnessBottomLeft;
		colorBlueBottomLeft *= brightnessBottomLeft;
		colorRedBottomRight *= bottomRightBrightness;
		colorGreenBottomRight *= bottomRightBrightness;
		colorBlueBottomRight *= bottomRightBrightness;
		colorRedTopRight *= topRightBrightness;
		colorGreenTopRight *= topRightBrightness;
		colorBlueTopRight *= topRightBrightness;
		renderModelFace(blockFace, x, y, z);
		return true;
	}
	public static void renderModelFace(BlockFace face, double x, double y, double z) {
		Tessellator tessellator = Tessellator.instance;
		double[] uvTL;
		double[] uvBL;
		double[] uvBR;
		double[] uvTR;
		if (overrideBlockTexture >= 0) {
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


		Vector3f[] faceVertices = new Vector3f[4];
		Vector3f origin = new Vector3f(0.5f, 0.5f, 0.5f);
		for (int i = 0; i < faceVertices.length; i++) {
			faceVertices[i] = face.vertices[i].rotateAroundX(origin, rotationX).rotateAroundY(origin, rotationY);
		}
		Vector3f vtl = faceVertices[0];
		Vector3f vbl = faceVertices[1];
		Vector3f vbr = faceVertices[2];
		Vector3f vtr = faceVertices[3];

		if (enableAO) {
			// Top Left
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.addVertexWithUV(x + vtl.x, y + vtl.y, z + vtl.z, uvTL[0], uvTL[1]);

			// Bottom Left
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.addVertexWithUV(x + vbl.x, y + vbl.y, z + vbl.z, uvBL[0], uvBL[1]);

			// Bottom Right
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.addVertexWithUV(x + vbr.x, y + vbr.y, z + vbr.z, uvBR[0], uvBR[1]);

			// Top Right
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.addVertexWithUV(x + vtr.x, y + vtr.y, z + vtr.z, uvTR[0], uvTR[1]);
		} else {
			tessellator.addVertexWithUV(x + vtl.x, y + vtl.y, z + vtl.z, uvTL[0], uvTL[1]); // Top Left
			tessellator.addVertexWithUV(x + vbl.x, y + vbl.y, z + vbl.z, uvBL[0], uvBL[1]); // Bottom Left
			tessellator.addVertexWithUV(x + vbr.x, y + vbr.y, z + vbr.z, uvBR[0], uvBR[1]); // Bottom Right
			tessellator.addVertexWithUV(x + vtr.x, y + vtr.y, z + vtr.z, uvTR[0], uvTR[1]); // Top Right
		}
	}
	public static boolean renderStandardModelWithColorMultiplier(BlockModel model, Block block, int x, int y, int z, float r, float g, float b) {
		enableAO = false;
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
		float blockBrightness = rba().invokeGetBlockBrightness(rba().getBlockAccess(), x, y, z);
		for (BlockCube cube: model.blockCubes) {
			for (Side side: DragonFly.sides) {
				BlockFace face = cube.getFaceFromSide(side, rotationX, rotationY);
				if (face == null) continue;
				int _x = x + side.getOffsetX();
				int _y = y + side.getOffsetY();
				int _z = z + side.getOffsetZ();

				if (!renderAllFaces){
					if (!renderSide(model, cube, side, x, y, z)) continue;
				}

				float sideBrightness;
				if (!cube.isOuterFace(side, rotationX, rotationY) && !block.blockMaterial.isLiquid()){
					sideBrightness = blockBrightness;
				} else {
					sideBrightness = rba().invokeGetBlockBrightness(rba().getBlockAccess(), _x, _y, _z);
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
				tessellator.setColorOpaque_F(red * (cube.shade() ? sideBrightness : 1f), green * (cube.shade() ? sideBrightness : 1f), blue * (cube.shade() ? sideBrightness : 1f));
				renderModelFace(face, x, y, z);
				renderedSomething = true;
			}
		}
		return renderedSomething;
	}
	public static boolean renderSide(BlockModel model, BlockCube cube, Side side, int x, int y, int z){
		WorldSource blockAccess = rba().getBlockAccess();
		boolean renderOuterSide = blockAccess.getBlock(x, y, z).shouldSideBeRendered(blockAccess, x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ(), side.getId(), blockAccess.getBlockMetadata(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ()));
		return !cube.getFaceFromSide(side, rotationX, rotationY).cullFace(x, y, z, renderOuterSide);
	}
	public static RenderBlocks getRenderBlocks(){
		try {
			Field f = BlockModelRenderBlocks.class.getDeclaredField("renderBlocks");
			f.setAccessible(true);
			RenderBlocks rb = (RenderBlocks) f.get(null);
			f.setAccessible(false);
			return rb;
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	public static RenderBlocksAccessor rba(){
		return (RenderBlocksAccessor) getRenderBlocks();
	}
}
