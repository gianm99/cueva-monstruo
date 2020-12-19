package com.cueva.monstruo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cueva.monstruo.CuevaDelMonstruo;

public class MenuScreen implements Screen {
	final CuevaDelMonstruo game;
	private Stage stage;

	public MenuScreen(CuevaDelMonstruo game) {
		this.game = game;
		stage = new Stage(new ScreenViewport());
	}

	@Override
	public void show() {
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		Skin skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
		Table table = new Table();
		table.setFillParent(true);
//		table.setDebug(true);
		stage.addActor(table);
		//crear botones
		TextButton newGame = new TextButton("Nueva partida",skin);
		TextButton preferences = new TextButton("Opciones", skin);
		TextButton exit = new TextButton("Salir", skin);
		//añadir botones a la tabla
		table.add(newGame).fillX().uniformX();
		table.row().pad(10, 0, 10, 0);
		table.add(preferences).fillX().uniformX();
		table.row();
		table.add(exit).fillX().uniformX();
		//crear button listeners
		exit.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				Gdx.app.exit();
			}
		});
		newGame.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				game.changeScreen(CuevaDelMonstruo.DEMO);
			}
		});
		preferences.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				game.changeScreen(CuevaDelMonstruo.SETTINGS_FIRST);
			}
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
