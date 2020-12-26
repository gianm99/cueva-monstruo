package com.cueva.monstruo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cueva.monstruo.entitys.Cueva;
import com.cueva.monstruo.screens.MainScreen;
import com.cueva.monstruo.screens.MenuScreen;

public class CuevaDelMonstruo extends Game {
	private com.cueva.monstruo.screens.MenuScreen menuScreen;
	private MainScreen mainScreen;
	private AppPreferences preferences;
	public Cueva cueva;
	public SpriteBatch batch;
	public BitmapFont font;
	public final static int MENU = 1;
	public final static int DEMO = 2;
	public final static int WIDTH = 480;
	public final static int HEIGHT = 480;

	@Override
	public void create() {
		menuScreen = new com.cueva.monstruo.screens.MenuScreen(this);
		preferences = new AppPreferences();
		cueva = new Cueva(getPreferences().getGameSize());
		batch = new SpriteBatch();
		font = new BitmapFont();
		setScreen(menuScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
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
			case DEMO:
				if (mainScreen == null) mainScreen = new MainScreen(this);
				this.setScreen(mainScreen);
				break;
		}
	}

	public AppPreferences getPreferences() {
		return preferences;
	}
}
