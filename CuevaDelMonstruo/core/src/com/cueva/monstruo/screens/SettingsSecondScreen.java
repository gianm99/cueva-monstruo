package com.cueva.monstruo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cueva.monstruo.CuevaDelMonstruo;
import com.cueva.monstruo.entitys.Cueva;

public class SettingsSecondScreen implements Screen {
	final CuevaDelMonstruo game;
	private Stage stage;
	private Label rowMonsterLabel;
	private Label columnMonsterLabel;
	private Label rowCliffLabel;
	private Label columnCliffLabel;

	public SettingsSecondScreen(CuevaDelMonstruo game) {
		this.game = game;
		stage = new Stage(new ScreenViewport());
	}

	@Override
	public void show() {
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		Skin skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
		Window window = new Window("Otras opciones/Obstaculos", skin);
		window.setFillParent(true);
		stage.addActor(window);
		Table table = new Table();
		table.setFillParent(true);
//		table.setDebug(true);
		window.addActor(table);
		//botón para regresar a la anterior pantalla
		final TextButton backButton = new TextButton("Volver", skin);
		//botón para comenzar la demostración
		final TextButton nextButton = new TextButton("Comenzar", skin);
		//variable para los select de fila y columna
		Integer[] range = new Integer[game.cueva.getSize()];
		for (int i = 0; i < range.length; i++) range[i] = i + 1; //1..size
		//fila para un monstruo
		final SelectBox<Integer> rowMonsterSelect = new SelectBox<>(skin);
		rowMonsterSelect.setItems(range);
		rowMonsterSelect.setSelected(1);
		//columna para un monstruo
		final SelectBox<Integer> columnMonsterSelect = new SelectBox<>(skin);
		columnMonsterSelect.setItems(range);
		columnMonsterSelect.setSelected(Cueva.DEFAULT_SIZE);
		//botón para añadir un monstruo
		final TextButton addMonsterButton = new TextButton("Confirmar", skin);
		//fila para un precipicio
		final SelectBox<Integer> rowCliffSelect = new SelectBox<>(skin);
		rowCliffSelect.setItems(range);
		rowCliffSelect.setSelected(Cueva.DEFAULT_SIZE);
		//columna para un precipicio
		final SelectBox<Integer> columnCliffSelect = new SelectBox<>(skin);
		columnCliffSelect.setItems(range);
		columnCliffSelect.setSelected(1);
		//botón para añadir un precipicio
		final TextButton addCliffButton = new TextButton("Confirmar", skin);
		//añadir listeners
//		backButton.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				game.changeScreen(CuevaDelMonstruo.SETTINGS_FIRST);
//			}
//		});
		nextButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.changeScreen(CuevaDelMonstruo.DEMO);
			}
		});
		addMonsterButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.cueva.agregarMonstruo(rowMonsterSelect.getSelected(), columnMonsterSelect.getSelected());
			}
		});
		addCliffButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.cueva.agregarPrecipicio(rowCliffSelect.getSelected(), columnCliffSelect.getSelected());
			}
		});
		rowMonsterLabel=new Label("Monstruo (fila):",skin);
		columnMonsterLabel=new Label("Monstruo (columna):",skin);
		rowCliffLabel=new Label("Precipicio (fila):",skin);
		columnCliffLabel=new Label("Precipicio (columna):",skin);
		table.row().pad(10, 0, 0, 10);
		table.add(rowMonsterLabel).left();
		table.add(rowMonsterSelect);
		table.row().pad(10, 0, 0, 10);
		table.add(columnMonsterLabel).left();
		table.add(columnMonsterSelect);
		table.row().pad(10, 0, 0, 10);
		table.add(addMonsterButton).colspan(2);
		table.row().pad(10, 0, 0, 10);
		table.add(rowCliffLabel).left();
		table.add(rowCliffSelect);
		table.row().pad(10, 0, 0, 10);
		table.add(columnCliffLabel).left();
		table.add(columnCliffSelect);
		table.row().pad(10, 0, 0, 10);
		table.add(addCliffButton).colspan(2);
		table.row().pad(10, 0, 0, 10);
		table.add(backButton).left();
		table.add(nextButton);
	}

	@Override
	public void render(float delta) {
		// clear the screen ready for next set of images to be drawn
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell our stage to do actions and draw itself
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

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

	}
}
