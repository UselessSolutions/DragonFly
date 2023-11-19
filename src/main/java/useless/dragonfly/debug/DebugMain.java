package useless.dragonfly.debug;

import useless.dragonfly.DragonFly;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.utilities.Utilities;

public class DebugMain {
	public static void init(){
		DebugBlocks.init();
		DebugEntities.init();
		StringBuilder builder = new StringBuilder();
		for (String string: ModelHelper.modelDataFiles.keySet()) {
			builder.append(string);
			builder.append(Utilities.tabBlock(ModelHelper.modelDataFiles.get(string).toString(), 1));
		}
		DragonFly.LOGGER.info(builder.toString());
	}
}
