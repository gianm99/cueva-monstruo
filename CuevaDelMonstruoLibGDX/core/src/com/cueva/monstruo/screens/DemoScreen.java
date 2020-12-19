package com.cueva.monstruo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.cueva.monstruo.entitys.Cueva;
import com.cueva.monstruo.CuevaDelMonstruo;

public class DemoScreen implements Screen {
	private CuevaDelMonstruo parent;
	private Cueva cueva;
	private Texture agente;
	private Texture monstruo;
	private Texture precipicio;
	private Texture suelo;
	private Texture tesoroCerrado;
	private Texture tesoroAbierto;

	public DemoScreen(CuevaDelMonstruo cuevaDelMonstruo) {
		this.parent = cuevaDelMonstruo;
		this.cueva = cuevaDelMonstruo.cueva;
		cargarTexturas();

	}

	/**
	 * Carga las texturas que se usan para dibujar la demo
	 */
	private void cargarTexturas() {
		agente=escalarTextura(new Pixmap(Gdx.files.internal("agent.png")));
		monstruo=escalarTextura(new Pixmap(Gdx.files.internal("monster.png")));
		precipicio=escalarTextura(new Pixmap(Gdx.files.internal("trap.png")));
		suelo=escalarTextura(new Pixmap(Gdx.files.internal("floor.png")));
		tesoroCerrado=escalarTextura(new Pixmap(Gdx.files.internal("chest_closed.png")));
		tesoroAbierto=escalarTextura(new Pixmap(Gdx.files.internal("chest_open.png")));
		//TODO Importar las imágenes de las flechas
	}

	/**
	 * Escala una imagen al tamaño de celda que se necesita para mostrar la demo correctamente
	 * @param original Pixmap que se quiere escalar y convertir en Texture
	 * @return Texture que contiene la imagen escalada
	 */
	private Texture escalarTextura(Pixmap original) {
		int w = CuevaDelMonstruo.WIDTH;
		int h = CuevaDelMonstruo.HEIGHT;
		int size = cueva.getSize();
		Pixmap escalada = new Pixmap(w / size, h / size, original.getFormat());
		Texture textura = new Texture(escalada);
		escalada.dispose();
		original.dispose();
		return textura;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

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
