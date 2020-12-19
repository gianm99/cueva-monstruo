package com.cueva.monstruo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cueva.monstruo.entitys.Cueva;
import com.cueva.monstruo.CuevaDelMonstruo;

public class SettingsFirstScreen implements Screen {
	final CuevaDelMonstruo game;
	private boolean chestSet;
	private Stage stage;
	private Label sizeGameLabel;
	private Label rowChestLabel;
	private Label columnChestLabel;
	private TextButton backButton;
	private TextButton nextButton;
	private TextButton addChestButton;
	private SelectBox<Integer> sizeGameSelect;
	private SelectBox<Integer> rowChestSelect;
	private SelectBox<Integer> columnChestSelect;

	public SettingsFirstScreen(CuevaDelMonstruo game) {
		this.game = game;
		stage = new Stage(new ScreenViewport());
	}

	@Override
	public void show() {
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		Skin skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
		Window window = new Window("Opciones/Cueva", skin);
		window.setFillParent(true);
		stage.addActor(window);
		Table table = new Table();
		table.setFillParent(true);
//		table.setDebug(true);
		window.addActor(table);
		//botón para regresar al menú inicial
		backButton = new TextButton("Volver", skin);
		//botón para ir a la segunda pantalla de opciones
		nextButton = new TextButton("Siguiente", skin);
		nextButton.setDisabled(true); //por defecto, está desactivado
		//tamaño de la cueva
		sizeGameSelect = new SelectBox<>(skin);
		sizeGameSelect.setItems(3, 4, 5, 6, 8, 10, 12, 15, 16, 20); //tamaños seguros
		sizeGameSelect.setSelected(game.getPreferences().getGameSize());
		//variable para los select de fila y columna
		Integer[] range = new Integer[sizeGameSelect.getSelected()];
		//noinspection ConstantConditions
		for (int i = 0; i < range.length; i++) range[i] = i + 1; //1..20
		//fila del tesoro
		rowChestSelect = new SelectBox<>(skin);
		rowChestSelect.setItems(range);
		rowChestSelect.setSelected(3);
		//columna del tesoro
		columnChestSelect = new SelectBox<>(skin);
		columnChestSelect.setItems(range);
		columnChestSelect.setSelected(3);
		//botón para añadir el tesoro
		addChestButton = new TextButton("Confirmar", skin);
		//añadir listeners
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.changeScreen(CuevaDelMonstruo.MENU);
			}
		});
		nextButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.changeScreen(CuevaDelMonstruo.SETTINGS_SECOND);
			}
		});
		sizeGameSelect.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.getPreferences().setGameSize(sizeGameSelect.getSelected());
				game.cueva = new Cueva(sizeGameSelect.getSelected()); //se sobreescribe cueva
				if (rowChestSelect.getSelected() > sizeGameSelect.getSelected()) {
					rowChestSelect.setSelected(sizeGameSelect.getSelected());
				}
				if (columnChestSelect.getSelected() > sizeGameSelect.getSelected()) {
					columnChestSelect.setSelected(sizeGameSelect.getSelected());
				}
				Integer[] range = new Integer[sizeGameSelect.getSelected()];
				for (int i = 0; i < range.length; i++) range[i] = i + 1; //1..20
				rowChestSelect.setItems(range);
				columnChestSelect.setItems(range);
//				chestSet = game.cueva.agregarTesoro(rowChestSelect.getSelected(), columnChestSelect.getSelected());
//				if (chestSet) nextButton.setDisabled(false);
				nextButton.setDisabled(true);
			}
		});
		addChestButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				chestSet = game.cueva.agregarTesoro(rowChestSelect.getSelected(), columnChestSelect.getSelected());
				if (chestSet) nextButton.setDisabled(false);
			}
		});
		sizeGameLabel = new Label("Dimension:", skin);
		rowChestLabel = new Label("Tesoro (fila):", skin);
		columnChestLabel = new Label("Tesoro (columna):", skin);
		table.row().pad(10, 0, 0, 10);
		table.add(sizeGameLabel).left();
		table.add(sizeGameSelect);
		table.row().pad(10, 0, 0, 10);
		table.add(rowChestLabel).left();
		table.add(rowChestSelect);
		table.row().pad(10, 0, 0, 10);
		table.add(columnChestLabel).left();
		table.add(columnChestSelect);
		table.row().pad(10, 0, 0, 10);
		table.add(addChestButton).colspan(2);
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
		// change the stage's viewport when the screen size is changed
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

	}
}
