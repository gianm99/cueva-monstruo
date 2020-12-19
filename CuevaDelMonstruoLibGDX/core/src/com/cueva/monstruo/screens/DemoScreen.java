package com.cueva.monstruo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.cueva.monstruo.CuevaDelMonstruo;
import com.cueva.monstruo.entitys.Cuadro;
import com.cueva.monstruo.entitys.Cueva;

public class DemoScreen implements Screen {
	final CuevaDelMonstruo game;
	private Cueva cueva;
	private Texture agente;
	private Texture monstruo;
	private Texture precipicio;
	private Texture suelo;
	private Texture tesoroCerrado;
	private Texture tesoroAbierto;
	private OrthographicCamera camara;
	private boolean autoPlay;
	private long lastMoveTime;

	public DemoScreen(CuevaDelMonstruo game) {
		this.game = game;
		this.cueva = game.cueva;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(251f / 255f, 140f / 255f, 100f / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camara.update();
		game.batch.setProjectionMatrix(camara.combined);
		//dibujar el mapa y todos los elementos
		game.batch.begin();
		Cuadro cuadro;
		for (int i = 0; i < cueva.getSize(); i++) {
			for (int j = 0; j < cueva.getSize(); j++) {
				int x = j * cueva.costado;
				int y = i * cueva.costado;
				//suelo
				game.batch.draw(suelo, x, y);
				cuadro = cueva.cuadros[i][j];
				//precipicio
				if (cuadro.isPrecipicio()) game.batch.draw(precipicio, x, y);
				//monstruo
				if (cuadro.isMonstruo()) game.batch.draw(monstruo, x, y);
				//tesoro
				if (cuadro.isTesoro()) {
					game.batch.draw(cuadro.isTesoroEncontrado() ? tesoroAbierto : tesoroCerrado, x, y);
				}
				//agente
				if (cuadro.isAgente()) game.batch.draw(agente, x, y);
			}
		}
		game.batch.end();
		//procesar input de usuario
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.changeScreen(CuevaDelMonstruo.MENU);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			autoPlay = !autoPlay;
		}
		if (!cueva.haTerminado() && TimeUtils.nanoTime() - lastMoveTime > 500000000
				&& (autoPlay || Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
			cueva.obtenerPercepciones();
			cueva.realizarAcciones();
			lastMoveTime = TimeUtils.nanoTime();
		}
	}

	@Override
	public void show() {
		this.cueva = game.cueva; //recargar la cueva
		autoPlay = false;
		//cargar las texturas
		agente = escalarTextura(new Pixmap(Gdx.files.internal("agent.png")));
		monstruo = escalarTextura(new Pixmap(Gdx.files.internal("monster.png")));
		precipicio = escalarTextura(new Pixmap(Gdx.files.internal("trap.png")));
		suelo = escalarTextura(new Pixmap(Gdx.files.internal("floor.png")));
		tesoroCerrado = escalarTextura(new Pixmap(Gdx.files.internal("chest_closed.png")));
		tesoroAbierto = escalarTextura(new Pixmap(Gdx.files.internal("chest_open.png")));
		//crear la cámara
		camara = new OrthographicCamera();
		camara.setToOrtho(false, 480, 480);
	}

	/**
	 * Escala una imagen al tamaño de celda que se necesita para mostrar la demo correctamente
	 *
	 * @param original Pixmap que se quiere escalar y convertir en Texture
	 * @return Texture que contiene la imagen escalada
	 */
	@org.jetbrains.annotations.NotNull
	private Texture escalarTextura(Pixmap original) {
		int w = CuevaDelMonstruo.WIDTH;
		int h = CuevaDelMonstruo.HEIGHT;
		int size = cueva.getSize();
		Pixmap escalada = new Pixmap(w / size, h / size, original.getFormat());
		escalada.drawPixmap(original,
				0, 0, original.getWidth(), original.getHeight(),
				0, 0, escalada.getWidth(), escalada.getHeight());
		Texture textura = new Texture(escalada);
		escalada.dispose();
		original.dispose();
		return textura;
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
		agente.dispose();
		monstruo.dispose();
		precipicio.dispose();
		suelo.dispose();
		tesoroCerrado.dispose();
	}
}
