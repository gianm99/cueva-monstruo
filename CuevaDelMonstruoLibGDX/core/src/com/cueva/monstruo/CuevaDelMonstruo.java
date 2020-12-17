package com.cueva.monstruo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.scenario.Settings;

import java.awt.*;

public class CuevaDelMonstruo extends Game {
    private MenuScreen menuScreen;
    private SettingsScreen settingsScreen;
    private GameScreen gameScreen;

    public final static int MENU = 0;
    public final static int SETTINGS = 1;
    public final static int GAME = 2;

    @Override
    public void create() {
        menuScreen = new MenuScreen(this);
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
            case SETTINGS:
                if (settingsScreen == null) settingsScreen = new SettingsScreen(this);
                this.setScreen(settingsScreen);
            case GAME:
                if (gameScreen == null) gameScreen = new GameScreen(this);
                this.setScreen(gameScreen);
        }
    }
}
