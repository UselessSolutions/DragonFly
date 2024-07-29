package org.useless.dragonfly.utilities;

import net.minecraft.core.util.helper.Axis;

public class AxisUtils {
	public static Axis getAxisFromName(String name){
		switch (name.toLowerCase()){
			case "x":
				return Axis.X;
			case "y":
				return Axis.Y;
			case "z":
				return Axis.Z;
			default:
				return Axis.NONE;
		}
	}
}
