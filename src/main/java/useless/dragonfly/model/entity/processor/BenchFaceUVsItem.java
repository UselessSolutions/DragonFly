package useless.dragonfly.model.entity.processor;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.util.helper.Direction;

/*
 * Credit by 0999312! Thanks!
 * https://github.com/0999312/MMLib/blob/master/src/main/java/cn/mcmod_mmf/mmlib/client/model/pojo/FaceUVsItem.java
 */
public class BenchFaceUVsItem {
	@SerializedName("down")
	private BenchEntityFace down;
	@SerializedName("east")
	private BenchEntityFace east;
	@SerializedName("north")
	private BenchEntityFace north;
	@SerializedName("south")
	private BenchEntityFace south;
	@SerializedName("up")
	private BenchEntityFace up;
	@SerializedName("west")
	private BenchEntityFace west;

	public static BenchFaceUVsItem singleSouthFace() {
		BenchFaceUVsItem faces = new BenchFaceUVsItem();
		faces.north = BenchEntityFace.empty();
		faces.east = BenchEntityFace.empty();
		faces.west = BenchEntityFace.empty();
		faces.south = BenchEntityFace.single16X();
		faces.up = BenchEntityFace.empty();
		faces.down = BenchEntityFace.empty();
		return faces;
	}

	public BenchEntityFace getFace(Direction direction) {
		switch (direction) {
			case EAST:
				return west;
			case WEST:
				return east;
			case NORTH:
				return north;
			case SOUTH:
				return south;
			case UP:
				return down;
			case DOWN:
			default:
				return up;
		}
	}
}
