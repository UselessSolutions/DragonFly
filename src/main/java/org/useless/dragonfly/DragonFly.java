package org.useless.dragonfly;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;

public class DragonFly implements GameStartEntrypoint {
    public static final String MOD_ID = "dragonfly";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static String version;
	public static String renderState = "gui";
	static {
		version = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
	}
	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {
		LOGGER.info("DragonFly initialized.");
	}
}
