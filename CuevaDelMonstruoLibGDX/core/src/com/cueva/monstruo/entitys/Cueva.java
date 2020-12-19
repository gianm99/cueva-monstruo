/*
  Cueva

  06/12/20
 */
package com.cueva.monstruo.entitys;

import com.cueva.monstruo.entitys.Agente.Orientacion;

/**
 * Una cueva con unas características determinadas
 *
 * @author gianm
 */
public class Cueva {

	private Agente agente;
	private int size;
	private int monstruos;
	private Cuadro[][] cuadros;
	public final int costado;
	public static final int COLUMNAS_MAX = 20;
	public static final int FILAS_MAX = 20;

	public Cueva(int size) {
		this.size = size;
		costado = 480 / size;
		cuadros = new Cuadro[size][size];
		int y = 0;
		for (int i = 0; i < size; i++) {
			int x = 0;
			for (int j = 0; j < size; j++) {
				cuadros[i][j] = new Cuadro();
			}
		}
	}

	public void obtenerPercepciones() {
		Posicion posicion = agente.getPosicion();
		int fila = posicion.getFila() - 1;
		int columna = posicion.getColumna() - 1;
		boolean hedor = cuadros[fila][columna].isHedor();
		boolean brisa = cuadros[fila][columna].isBrisa();
		boolean resplandor = cuadros[fila][columna].isTesoro();
		boolean golpe = calcularGolpe();
		agente.actualizar(hedor, brisa, resplandor, golpe);
	}

	/**
	 * Determina si el agente debe percibir un golpe por haber avanzado hacia un muro de la cueva
	 *
	 * @return boolean indicando si debe percibir un golpe
	 */
	private boolean calcularGolpe() {
		Posicion posicion = agente.getPosicion();
		Orientacion orientacion = agente.getOrientacion();
		return (posicion.getFila() == size && orientacion == Orientacion.NORTE)
				|| (posicion.getColumna() == size && orientacion == Orientacion.ESTE);
	}

	/**
	 * Hacer que el agente realice las acciones que considere correctas
	 */
	public void realizarAcciones() {
		agente.elegirAccion();
		if (agente.isEliminarMonstruo()) {
			Posicion posicionMonstruo = agente.getPosicionMonstruo();
			retirarMonstruo(posicionMonstruo);
		}
		Cuadro anterior = cuadroEnPosicion(agente.getPosicion());
		anterior.setAgente(false);
		agente.actuar();
		Cuadro nuevo = cuadroEnPosicion(agente.getPosicion());
		nuevo.setAgente(true);
	}

	/**
	 * Retira un monstruo y su hedor de la cueva
	 *
	 * @param p posición en la que estaba el monstruo
	 */
	private void retirarMonstruo(Posicion p) {
		// Quitar el monstruo
		cuadroEnPosicion(p).setMonstruo(false);
		// Quitar el hedor
		// Anterior posición del monstruo
		if (!tieneMonstruoCerca(p)) {
			cuadroEnPosicion(p).setHedor(false);
		}
		// Posiciones adyacentes
		for (Orientacion o : Orientacion.values()) {
			if (!tieneMonstruoCerca(p.adyacente(o))) {
				cuadroEnPosicion(p.adyacente(o)).setHedor(false);
			}
		}
	}

	/**
	 * Determina si hay un monstruo en una posición adyacente
	 *
	 * @param p posición alrededor de la cual se comprueba
	 * @return boolean indicando si hay un monstruo en una posición adyacente
	 */
	private boolean tieneMonstruoCerca(Posicion p) {
		for (Orientacion o : Orientacion.values()) {
			Posicion adyacente = p.adyacente(o);
			if (posicionCorrecta(adyacente.getFila(), adyacente.getColumna())
					&& cuadroEnPosicion(adyacente).isMonstruo()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve el cuadro en la posición indicada
	 *
	 * @param p posición que se quiere comprobar
	 * @return cuadro de la posición indicada
	 */
	private Cuadro cuadroEnPosicion(Posicion p) {
		return cuadros[p.getFila() - 1][p.getColumna() - 1];
	}

	/**
	 * Agrega un agente a la cueva. Siempre lo sitúa en la posición inicial.
	 */
	public void agregarAgente() {
		agente = new Agente(new Posicion(1, 1), Orientacion.ESTE, monstruos);
		cuadros[0][0].setAgente(true);
	}

	/**
	 * Agrega un tesoro a una posición si es posible
	 *
	 * @param fila    fila de la posición
	 * @param columna fila de la posición
	 * @return boolean indicando si se ha agregado el tesoro
	 */
	public boolean agregarTesoro(int fila, int columna) {
		if (!posicionCorrecta(fila, columna) || (fila == 1 && columna == 1)
				|| cuadros[fila - 1][columna - 1].isMonstruo()
				|| cuadros[fila - 1][columna - 1].isPrecipicio()) {
			return false;
		}
		cuadros[fila - 1][columna - 1].setTesoro(true);

		return true;
	}

	/**
	 * Agrega un monstruo a una posición si es posible
	 *
	 * @param fila    fila de la posición
	 * @param columna columna de la posición
	 */
	public void agregarMonstruo(int fila, int columna) {
		if (!posicionCorrecta(fila, columna) || !respetaPosicionInicial(fila, columna)
				|| cuadros[fila - 1][columna - 1].isTesoro()) {
			return;
		}
		monstruos++;
		cuadros[fila - 1][columna - 1].setMonstruo(true);
		expandirHedor(fila, columna);
	}

	/**
	 * Agrega el hedor de un monstruo a su posición y las adyacentes
	 *
	 * @param fila    fila de la posición del monstruo
	 * @param columna columna de la posición del monstruo
	 */
	private void expandirHedor(int fila, int columna) {
		agregarHedor(fila, columna);
		// ESTE
		if (posicionCorrecta(fila, columna + 1)) {
			agregarHedor(fila, columna + 1);
		}
		// NORTE
		if (posicionCorrecta(fila + 1, columna)) {
			agregarHedor(fila + 1, columna);
		}
		// OESTE
		if (posicionCorrecta(fila, columna - 1)) {
			agregarHedor(fila, columna - 1);
		}
		// SUR
		if (posicionCorrecta(fila - 1, columna)) {
			agregarHedor(fila - 1, columna);
		}
	}

	/**
	 * Agrega hedor a una posición de la cueva
	 *
	 * @param fila    fila de la posición
	 * @param columna columna de la posición
	 */
	private void agregarHedor(int fila, int columna) {
		fila--;
		columna--;
		cuadros[fila][columna].setHedor(true);
	}

	/**
	 * Agrega un precipicio a una posición si es posible
	 *
	 * @param fila    fila de la posición
	 * @param columna columna de la posición
	 */
	public void agregarPrecipicio(int fila, int columna) {
		if (!posicionCorrecta(fila, columna) || !respetaPosicionInicial(fila, columna)
				|| cuadros[fila - 1][columna - 1].isTesoro()) {
			return;
		}
		cuadros[fila - 1][columna - 1].setPrecipicio(true);
		expandirBrisa(fila, columna);
	}

	/**
	 * Agrega la brisa de un precipicio a su posición y las adyacentes
	 *
	 * @param fila    fila de la posición del precipicio
	 * @param columna columna de la posición del precipicio
	 */
	private void expandirBrisa(int fila, int columna) {
		agregarBrisa(fila, columna);
		// ESTE
		if (posicionCorrecta(fila, columna + 1)) {
			agregarBrisa(fila, columna + 1);
		}
		// NORTE
		if (posicionCorrecta(fila + 1, columna)) {
			agregarBrisa(fila + 1, columna);
		}
		// OESTE
		if (posicionCorrecta(fila, columna - 1)) {
			agregarBrisa(fila, columna - 1);
		}
		// SUR
		if (posicionCorrecta(fila - 1, columna)) {
			agregarBrisa(fila - 1, columna);
		}
	}

	/**
	 * Agrega brisa a una posición de la cueva
	 *
	 * @param fila    fila de la posición
	 * @param columna columna de la posición
	 */
	private void agregarBrisa(int fila, int columna) {
		fila--;
		columna--;
		cuadros[fila][columna].setBrisa(true);
	}

	/**
	 * Determina si una combinación de fila y columna está alrededor de la posición inicial (1,1) de
	 * la cueva
	 *
	 * @param fila
	 * @param columna
	 * @return
	 */
	private boolean respetaPosicionInicial(int fila, int columna) {
		return (posicionCorrecta(fila, columna) && !(fila == 1 && columna == 1)
				&& !adyacentes(1, 1, fila, columna));
	}

	/**
	 * Determina si dos posiciones son adyacentes
	 *
	 * @param fila1    fila de la posición 1
	 * @param columna1 columna de la posición 1
	 * @param fila2    fila de la posición 2
	 * @param columna2 columna de la posición 2
	 * @return boolean indicando si son adyacentes
	 */
	private static boolean adyacentes(int fila1, int columna1, int fila2, int columna2) {
		return (fila1 == fila2 && columna1 == columna2 + 1) // ESTE
				|| (fila1 == fila2 + 1 && columna1 == columna2) // NORTE
				|| (fila1 == fila2 && columna1 == columna2 - 1) // OESTE
				|| (fila1 == fila2 - 1 && columna1 == columna2); // SUR
	}

	/**
	 * Determina el agente de la cueva ha terminado la búsqueda del tesoro
	 *
	 * @return boolean indicando si todos los agentes han terminado
	 */
	public boolean haTerminado() {
		return agente.isTerminado();
	}

	/**
	 * Determina si una posición está dentro del rango de la cueva
	 *
	 * @param fila    fila de la posición que se comprueba
	 * @param columna columna de la posición que se comprueba
	 * @return
	 */
	private boolean posicionCorrecta(int fila, int columna) {
		return fila > 0 && fila <= size && columna > 0 && columna <= size;
	}

	public int getSize(){
		return size;
	}
}
