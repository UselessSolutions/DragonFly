package useless.dragonfly.model.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlockCache;
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

import java.awt.*;
import java.lang.reflect.Field;

import static useless.dragonfly.utilities.vector.Vector3f.origin;

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
		int off = (int) ((System.currentTimeMillis()/20) % 360);
		float xOffset = 0.5f;
		float yOffset = 0.5f;
		float zOffset = 0.5f;
		float xScale;
		float yScale;
		float zScale;
		float xRot;
		float yRot;
		float zRot;
		PositionData displayData = modelDragonFly.baseModel.getDisplayPosition(DragonFly.renderState);
        switch (DragonFly.renderState) {
			case "ground":
				xOffset -= (float) displayData.translation[2] / 16f;
				yOffset -= (float) displayData.translation[1] / 16f;
				zOffset -= (float) displayData.translation[0] / 16f;

				xScale = (float) displayData.scale[2] * 4;
				yScale = (float) displayData.scale[1] * 4;
				zScale = (float) displayData.scale[0] * 4;

				xRot = (float) displayData.rotation[0];
				yRot = (float) displayData.rotation[1];
				zRot = (float) displayData.rotation[2];
				break;
            case "head":
                xOffset -= (float) displayData.translation[2] / 16f;
                yOffset -= (float) displayData.translation[1] / 16f;
                zOffset -= (float) displayData.translation[0] / 16f;

                xScale = (float) displayData.scale[2];
                yScale = (float) displayData.scale[1];
                zScale = (float) displayData.scale[0];

                xRot = (float) displayData.rotation[0];
				yRot = (float) displayData.rotation[1] + 180;
				zRot = (float) displayData.rotation[2];
                break;
            case "firstperson_righthand":
                xOffset -= (float) displayData.translation[2] / 8f;
                yOffset -= (float) displayData.translation[1] / 8f;
                zOffset -= (float) displayData.translation[0] / 8f;

                xScale = (float) displayData.scale[2] * 2.5f;
                yScale = (float) displayData.scale[1] * 2.5f;
                zScale = (float) displayData.scale[0] * 2.5f;

				xRot = (float) displayData.rotation[0];
				yRot = (float) displayData.rotation[1] + 45;
				zRot = (float) displayData.rotation[2];
                break;
            case "gui":
            default:
                xOffset -= (float) displayData.translation[2] / 16f;
                yOffset -= (float) displayData.translation[1] / 16f;
                zOffset -= (float) displayData.translation[0] / 16f;

                xScale = (float) displayData.scale[2] * 1.6f;
                yScale = (float) displayData.scale[1] * 1.6f;
                zScale = (float) displayData.scale[0] * 1.6f;

				xRot = (float) displayData.rotation[0] - 30;
				yRot = (float) displayData.rotation[1] + 45;
				zRot = (float) displayData.rotation[2];
        }
        Tessellator tessellator = Tessellator.instance;

		GL11.glRotatef(yRot, 0, 1, 0);
		GL11.glRotatef(xRot, 1, 0, 0);
		GL11.glRotatef(zRot, 0, 0, 1);
		GL11.glTranslatef(-xOffset, -yOffset, -zOffset);
		GL11.glScalef(xScale, yScale, zScale);
		if (modelDragonFly.baseModel.blockCubes != null){
			for (BlockCube cube: modelDragonFly.baseModel.blockCubes) {
				for (BlockFace face: cube.getFaces().values()) {
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
		for (BlockCube cube: model.blockCubes) {
			for (Side side: DragonFly.sides) {
				somethingRendered |= renderModelSide(model, cube, block, x, y, z, side);
			}
		}
		enableAO = false;
		return somethingRendered;
	}
	public static boolean renderModelSide(BlockModel model, BlockCube cube, Block block, int x, int y, int z, Side side) {
		BlockFace blockFace = cube.getFaceFromSide(side, rotationX, rotationY);
		if (blockFace == null) return false;
		if (!renderAllFaces){
			if (!renderSide(model, cube, side, x, y, z)) return false;
		}
		RenderBlockCache cache = rba().getCache();
		int sideOffX = side.getOffsetX();
		int sideOffY = side.getOffsetY();
		int sideOffZ = side.getOffsetZ();

		Vector3f vMin = cube.getMin().rotateAroundX(origin, rotationX).rotateAroundY(origin, rotationY);
		Vector3f vMax = cube.getMax().rotateAroundX(origin, rotationX).rotateAroundY(origin, rotationY);
		float minX = vMin.x;
		float minY = vMin.y;
		float minZ = vMin.z;
		float maxX = vMax.x;
		float maxY = vMax.y;
		float maxZ = vMax.z;

		float depth;
		int topX;
		int topY;
		int topZ;
		float topP;
		float botP;
		int lefX;
		int lefY;
		int lefZ;
		float lefP;
		float rigP;

		switch (side){
			case BOTTOM:
				depth = minY;
				topX = 0;
				topY = 0;
				topZ = 1;
				topP = maxZ;
				botP = minZ;
				lefX = -1;
				lefY = 0;
				lefZ = 0;
				lefP = 1.0F - minX;
				rigP = 1.0F - maxX;
				break;
			case TOP:
				depth = 1f - maxY;
				topX = 0;
				topY = 0;
				topZ = 1;
				topP = maxZ;
				botP = minZ;
				lefX = 1;
				lefY = 0;
				lefZ = 0;
				lefP = maxX;
				rigP = minX;
				break;
			case NORTH:
				depth = minZ;
				topX = -1;
				topY = 0;
				topZ = 0;
				topP = 1.0F - minX;
				botP = 1.0F - maxX;
				lefX = 0;
				lefY = 1;
				lefZ = 0;
				lefP = maxY;
				rigP = minY;
				break;
			case SOUTH:
				depth = 1f - maxZ;
				topX = 0;
				topY = 1;
				topZ = 0;
				topP = maxY;
				botP = minY;
				lefX = -1;
				lefY = 0;
				lefZ = 0;
				lefP = 1.0F - minX;
				rigP = 1.0F - maxX;
				break;
			case WEST:
				depth = minX;
				topX = 0;
				topY = 0;
				topZ = 1;
				topP = maxZ;
				botP = minZ;
				lefX = 0;
				lefY = 1;
				lefZ = 0;
				lefP = maxY;
				rigP = minY;
				break;
			case EAST:
				depth = 1f - maxX;
				topX = 0;
				topY = 0;
				topZ = 1;
				topP = maxZ;
				botP = minZ;
				lefX = 0;
				lefY = -1;
				lefZ = 0;
				lefP = 1.0F - minY;
				rigP = 1.0F - maxY;
				break;
			default:
				throw new IllegalArgumentException("Side " + side + " is not a valid side to render!");
		}

		float r;
		float g;
		float b;
		if (blockFace.useTint()){
			Color color = new Color(BlockColorDispatcher.getInstance().getDispatch(block).getWorldColor(mc.theWorld, x, y, z));
			r = color.getRed() / 255.0f;
			g = color.getGreen() / 255.0f;
			b = color.getBlue() / 255.0f;
		} else {
			r = 1f;
			g = 1f;
			b = 1f;
		}
		float lightTR;
		float lightBR;
		float lightBL;
		float lightTL;
		if (rba().getOverbright() || blockFace.getFullBright()){
			lightTR = 1.0f;
			lightBR = 1.0f;
			lightBL = 1.0f;
			lightTL = 1.0f;
		} else if (!cube.shade()) {
			float brightness = cache.getBrightness(0, 0, 0);;
			lightTR = brightness;
			lightBR = brightness;
			lightBL = brightness;
			lightTL = brightness;
		} else {
			float sideLightMultiplier = rba().getSIDE_LIGHT_MULTIPLIER()[side.getId()];
			r *= sideLightMultiplier;
			g *= sideLightMultiplier;
			b *= sideLightMultiplier;
			float directionBrightness = cache.getBrightness(sideOffX, sideOffY, sideOffZ);
			boolean lefT = cache.getOpacity(sideOffX + lefX, sideOffY + lefY, sideOffZ + lefZ);
			boolean botT = cache.getOpacity(sideOffX - topX, sideOffY - topY, sideOffZ - topZ);
			boolean topT = cache.getOpacity(sideOffX + topX, sideOffY + topY, sideOffZ + topZ);
			boolean rigT = cache.getOpacity(sideOffX - lefX, sideOffY - lefY, sideOffZ - lefZ);
			float leftBrightness = cache.getBrightness(sideOffX + lefX, sideOffY + lefY, sideOffZ + lefZ);
			float bottomBrightness = cache.getBrightness(sideOffX - topX, sideOffY - topY, sideOffZ - topZ);
			float topBrightness = cache.getBrightness(sideOffX + topX, sideOffY + topY, sideOffZ + topZ);
			float rightBrightness = cache.getBrightness(sideOffX - lefX, sideOffY - lefY, sideOffZ - lefZ);
			float bottomLeftBrightness = botT && lefT ? leftBrightness : cache.getBrightness(sideOffX + lefX - topX, sideOffY + lefY - topY, sideOffZ + lefZ - topZ);
			float topLeftBrightness = topT && lefT ? leftBrightness : cache.getBrightness(sideOffX + lefX + topX, sideOffY + lefY + topY, sideOffZ + lefZ + topZ);
			float bottomRightBrightness = botT && rigT ? rightBrightness : cache.getBrightness(sideOffX - lefX - topX, sideOffY - lefY - topY, sideOffZ - lefZ - topZ);
			float topRightBrightness = topT && rigT ? rightBrightness : cache.getBrightness(sideOffX - lefX + topX, sideOffY - lefY + topY, sideOffZ - lefZ + topZ);
			lightTL = (topLeftBrightness + leftBrightness + topBrightness + directionBrightness) / 4.0f;
			lightTR = (topBrightness + directionBrightness + topRightBrightness + rightBrightness) / 4.0f;
			lightBR = (directionBrightness + bottomBrightness + rightBrightness + bottomRightBrightness) / 4.0f;
			lightBL = (leftBrightness + bottomLeftBrightness + directionBrightness + bottomBrightness) / 4.0f;
			if (depth > 0.01) { // The non-external faces of the block use the central block's brightness
				directionBrightness = cache.getBrightness(0, 0, 0);
				lefT = cache.getOpacity(lefX, lefY, lefZ);
				botT = cache.getOpacity(-topX, -topY, -topZ);
				topT = cache.getOpacity(topX, topY, topZ);
				rigT = cache.getOpacity(-lefX, -lefY, -lefZ);
				leftBrightness = cache.getBrightness(lefX, lefY, lefZ);
				bottomBrightness = cache.getBrightness(-topX, -topY, -topZ);
				topBrightness = cache.getBrightness(topX, topY, topZ);
				rightBrightness = cache.getBrightness(-lefX, -lefY, -lefZ);
				bottomLeftBrightness = botT && lefT ? leftBrightness : cache.getBrightness(lefX - topX, lefY - topY, lefZ - topZ);
				topLeftBrightness = topT && lefT ? leftBrightness : cache.getBrightness(lefX + topX, lefY + topY, lefZ + topZ);
				bottomRightBrightness = botT && rigT ? rightBrightness : cache.getBrightness(-lefX - topX, -lefY - topY, -lefZ - topZ);
				topRightBrightness = topT && rigT ? rightBrightness : cache.getBrightness(-lefX + topX, -lefY + topY, -lefZ + topZ);
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
		float bl = botP * lightTL + (1.0f - botP) * lightBL;
		float br = botP * lightTR + (1.0f - botP) * lightBR;
		float brightnessTopRight = lefP * tl + (1.0f - lefP) * tr;
		float brightnessBottomLeft = lefP * bl + (1.0f - lefP) * br;
		float bottomRightBrightness = rigP * bl + (1.0f - rigP) * br;
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
			uvTL = face.getVertexUV(0);
			uvBL = face.getVertexUV(1);
			uvBR = face.getVertexUV(2);
			uvTR = face.getVertexUV(3);
		}


		Vector3f[] faceVertices = new Vector3f[4];
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


				float red = 1f;
				float green = 1f;
				float blue = 1f;

				if (cube.shade()){
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
				}
				tessellator.setColorOpaque_F(!face.getFullBright() ? red * sideBrightness : 1f, !face.getFullBright() ? green * sideBrightness : 1f, !face.getFullBright() ? blue * sideBrightness : 1f);
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
