package com.cueva.monstruo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.cueva.monstruo.entitys.Cueva;
import com.cueva.monstruo.screens.DemoScreen;
import com.cueva.monstruo.screens.MainSettingsScreen;
import com.cueva.monstruo.screens.MenuScreen;
import com.cueva.monstruo.screens.OtherSettingsScreen;

public class CuevaDelMonstruo extends Game {
	private com.cueva.monstruo.screens.MenuScreen menuScreen;
	private com.cueva.monstruo.screens.MainSettingsScreen mainSettingsScreen;
	private com.cueva.monstruo.screens.OtherSettingsScreen otherSettingsScreen;
	private DemoScreen demoScreen;
	private AppPreferences preferences;
	public com.cueva.monstruo.entitys.Cueva cueva;
	public final static int MENU = 0;
	public final static int SETTINGS_FIRST = 1;
	public final static int SETTINGS_SECOND = 2;
	public final static int DEMO = 3;
	public final static int WIDTH=480;
	public final static int HEIGHT=480;

	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);
		menuScreen = new com.cueva.monstruo.screens.MenuScreen(this);
		preferences = new AppPreferences();
		cueva = new Cueva(getPreferences().getGameSize()); //tamaño por defecto
		cueva.agregarTesoro(3, 3); //posición por defecto
		setScreen(menuScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
	}

	/**
	 * Cambia la pantalla que se muestra al ejecutar el programa
	 *
	 * @param screen identificador de la pantalla que se quiere mostrar
	 */
	public void changeScreen(int screen) {
		switch (screen) {
			case MENU:
				if (menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case SETTINGS_FIRST:
				if (mainSettingsScreen == null) mainSettingsScreen = new MainSettingsScreen(this);
				this.setScreen(mainSettingsScreen);
				break;
			case SETTINGS_SECOND:
				if (otherSettingsScreen == null) otherSettingsScreen = new OtherSettingsScreen(this);
				this.setScreen(otherSettingsScreen);
				break;
			case DEMO:
				if (demoScreen == null) demoScreen = new DemoScreen(this);
				this.setScreen(demoScreen);
				break;
		}
	}

	public AppPreferences getPreferences() {
		return preferences;
	}
}
