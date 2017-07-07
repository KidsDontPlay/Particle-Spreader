package mrriegel.particlespreader.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static Configuration config;

	public static void refreshConfig(File file) {
		if ("".isEmpty())// no config yet
			return;
		config = new Configuration(file);
		config.load();

		if (config.hasChanged()) {
			config.save();
		}
	}

}
