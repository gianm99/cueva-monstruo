package com.cueva.monstruo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cueva.monstruo.CuevaDelMonstruo;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Cueva del monstruo";
		config.width = 580;
		config.height = 480;
		config.forceExit = false;
		new LwjglApplication(new CuevaDelMonstruo(), config);
	}
}
