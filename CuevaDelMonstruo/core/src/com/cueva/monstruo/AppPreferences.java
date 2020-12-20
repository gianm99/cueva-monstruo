package com.cueva.monstruo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AppPreferences {
	private static final String GAME_SIZE = "size";
	private static final String PREFS_NAME = "CuevaDelMonstruoLibGDX";

	protected Preferences getPrefs(){
		return Gdx.app.getPreferences(PREFS_NAME);
	}

	public int getGameSize(){
		return getPrefs().getInteger(GAME_SIZE,6);
	}

	public void setGameSize(int size){
		getPrefs().putInteger(GAME_SIZE,size);
		getPrefs().flush();
	}
}
