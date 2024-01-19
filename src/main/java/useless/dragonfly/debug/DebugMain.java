package useless.dragonfly.debug;

import turniplabs.halplibe.helper.ItemHelper;
import useless.dragonfly.DragonFly;
import useless.dragonfly.debug.item.ItemDebugStick;

public class DebugMain {
	public static void init(){
		ItemHelper.createItem(DragonFly.MOD_ID, new ItemDebugStick("debug", 21000), "debug").setIconCoord(4, 10);
		DebugEntities.init();
		DebugBlocks.init();
//		StringBuilder builder = new StringBuilder();
//		for (String string: ModelHelper.modelDataFiles.keySet()) {
//			builder.append(string);
//			builder.append(Utilities.tabBlock(ModelHelper.modelDataFiles.get(string).toString(), 1));
//		}
//		DragonFly.LOGGER.info(builder.toString());
	}
}
