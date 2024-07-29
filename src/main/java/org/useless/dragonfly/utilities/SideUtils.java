package org.useless.dragonfly.utilities;

import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;

public class SideUtils {
	/////////////////////

	public static final Side[][] sideXRotMap = new Side[][]{
		{Side.BOTTOM,   Side.SOUTH,     Side.TOP,       Side.NORTH},
		{Side.TOP,      Side.NORTH,     Side.BOTTOM,    Side.SOUTH},
		{Side.NORTH,    Side.BOTTOM,    Side.SOUTH,     Side.TOP},
		{Side.SOUTH,    Side.TOP,       Side.NORTH,     Side.BOTTOM},
		{Side.WEST,     Side.WEST,      Side.WEST,      Side.WEST},
		{Side.EAST,     Side.EAST,      Side.EAST,      Side.EAST},
	};

	public static final Side[][] sideYRotMap = new Side[][]{
		{Side.BOTTOM,   Side.BOTTOM,    Side.BOTTOM,    Side.BOTTOM},
		{Side.TOP,      Side.TOP,       Side.TOP,       Side.TOP},
		{Side.NORTH,    Side.EAST,      Side.SOUTH,     Side.WEST},
		{Side.SOUTH,    Side.WEST,      Side.NORTH,     Side.EAST},
		{Side.WEST,     Side.NORTH,     Side.EAST,      Side.SOUTH},
		{Side.EAST,     Side.SOUTH,     Side.WEST,      Side.NORTH},
	};

	public static final Side[][] sideZRotMap = new Side[][]{
		{Side.BOTTOM,   Side.EAST,      Side.TOP,       Side.WEST},
		{Side.TOP,      Side.WEST,      Side.BOTTOM,    Side.EAST},
		{Side.NORTH,    Side.NORTH,     Side.NORTH,     Side.NORTH},
		{Side.SOUTH,    Side.SOUTH,     Side.SOUTH,     Side.SOUTH},
		{Side.WEST,     Side.BOTTOM,    Side.EAST,      Side.TOP},
		{Side.EAST,     Side.TOP,       Side.WEST,      Side.BOTTOM},
	};
	@NotNull
	public static Side rotateSide(int rotX, int rotY, int rotZ, Side inputSide){
		return sideZRotMap[sideYRotMap[sideXRotMap[inputSide.getId()][rotX & 0b11].getId()][rotY & 0b11].getId()][rotZ & 0b11];
	}
	public static Side getSideFromName(String s){
		switch (s.toLowerCase()){
			case "bottom":
			case "down": // Mojang compatibility name
				return Side.BOTTOM;
			case "top":
			case "up": // Mojang compatibility name
				return Side.TOP;
			case "north":
				return Side.NORTH;
			case "south":
				return Side.SOUTH;
			case "west":
				return Side.WEST;
			case "east":
				return Side.EAST;
			default:
				new IllegalArgumentException("Could not parse sideId '" + s + "'!").printStackTrace();
				return Side.NONE;
		}
	}
}
