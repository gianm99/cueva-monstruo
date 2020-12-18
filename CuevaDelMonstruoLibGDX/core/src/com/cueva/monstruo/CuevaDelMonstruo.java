package com.cueva.monstruo;

import com.badlogic.gdx.Game;

public class CuevaDelMonstruo extends Game {
	private MenuScreen menuScreen;
	private MainSettingsScreen mainSettingsScreen;
	private OtherSettingsScreen otherSettingsScreen;
	private GameScreen gameScreen;
	private AppPreferences preferences;
	public Cueva cueva;
	public final static int MENU = 0;
	public final static int SETTINGS_FIRST = 1;
	public final static int SETTINGS_SECOND = 2;
	public final static int DEMO = 3;

	@Override
	public void create() {
		menuScreen = new MenuScreen(this);
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
				if (gameScreen == null) gameScreen = new GameScreen(this);
				this.setScreen(gameScreen);
				break;
		}
	}

	public AppPreferences getPreferences() {
		return preferences;
	}
}
