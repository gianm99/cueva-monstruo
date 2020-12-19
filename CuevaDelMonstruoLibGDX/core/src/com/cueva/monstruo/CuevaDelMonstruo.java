package com.cueva.monstruo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cueva.monstruo.entitys.Cueva;
import com.cueva.monstruo.screens.DemoScreen;
import com.cueva.monstruo.screens.SettingsFirstScreen;
import com.cueva.monstruo.screens.MenuScreen;
import com.cueva.monstruo.screens.SettingsSecondScreen;

public class CuevaDelMonstruo extends Game {
	private com.cueva.monstruo.screens.MenuScreen menuScreen;
	private SettingsFirstScreen settingsFirstScreen;
	private SettingsSecondScreen settingsSecondScreen;
	private DemoScreen demoScreen;
	private AppPreferences preferences;
	public Cueva cueva;
	public SpriteBatch batch;
	public final static int MENU = 0;
	public final static int SETTINGS_FIRST = 1;
	public final static int SETTINGS_SECOND = 2;
	public final static int DEMO = 3;
	public final static int WIDTH = 480;
	public final static int HEIGHT = 480;

	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);
		menuScreen = new com.cueva.monstruo.screens.MenuScreen(this);
		preferences = new AppPreferences();
		cueva = new Cueva(getPreferences().getGameSize()); //tamaño por defecto
		cueva.agregarTesoro(Cueva.DEFAULT_SIZE, Cueva.DEFAULT_SIZE); //posición por defecto
		batch = new SpriteBatch();
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
				if (settingsFirstScreen == null) settingsFirstScreen = new SettingsFirstScreen(this);
				this.setScreen(settingsFirstScreen);
				break;
			case SETTINGS_SECOND:
				if (settingsSecondScreen == null) settingsSecondScreen = new SettingsSecondScreen(this);
				this.setScreen(settingsSecondScreen);
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
