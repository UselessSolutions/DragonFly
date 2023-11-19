package useless.dragonfly;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.core.util.helper.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import useless.dragonfly.debug.DebugMain;
import useless.dragonfly.registries.TextureRegistry;
public class DragonFly implements ModInitializer, PreLaunchEntrypoint {
    public static final String MOD_ID = "dragonfly";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Gson GSON = new Gson();
	public static final Side[] sides = new Side[]{Side.BOTTOM, Side.TOP, Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST};
	public static boolean isDev = FabricLoader.getInstance().isDevelopmentEnvironment();
    @Override
    public void onInitialize() {
		if (isDev){
			DebugMain.init();
		}
        LOGGER.info("DragonFly initialized.");
    }

	@Override
	public void onPreLaunch() {
		TextureRegistry.init();
	}
}
