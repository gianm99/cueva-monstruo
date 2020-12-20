/*
  Cueva

  06/12/20
 */
package com.cueva.monstruo.entitys;

/**
 * Una cueva con unas características determinadas
 *
 * @author gianm
 */
public class Cueva {

	private boolean tesoroEncontrado;
	public Agente agente;
	private final int size;
	private int monstruos;
	private Posicion tesoro;
	public Cuadro[][] cuadros;
	public final int costado;
	public static final int DIMENSION_MAX = 20;
	public static final int DEFAULT_SIZE = 3;

	public Cueva(int size) {
		this.size = size;
		costado = 480 / size;
		cuadros = new Cuadro[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cuadros[i][j] = new Cuadro();
			}
		}
		agregarAgente();
	}

	public void enviarPercepciones() {
		Cuadro cuadroAgente = cuadroPos(agente.getPosActual());
		boolean hedor = cuadroAgente.isHedor();
		boolean brisa = cuadroAgente.isBrisa();
		boolean resplandor = cuadroAgente.isTesoro();
		boolean golpe = calcularGolpe();
		agente.actualizar(hedor, brisa, resplandor, golpe);
		tesoroEncontrado = agente.isTesoro();
	}

	/**
	 * Determina si el agente debe percibir un golpe por haber avanzado hacia un muro de la cueva
	 *
	 * @return boolean indicando si debe percibir un golpe
	 */
	private boolean calcularGolpe() {
		Posicion posicion = agente.getPosActual();
		Orientacion orientacion = agente.getOrientacion();
		return (posicion.getFila() == size && orientacion == Orientacion.NORTE)
				|| (posicion.getColumna() == size && orientacion == Orientacion.ESTE);
	}

	/**
	 * Hacer que el agente realice las acciones que considere correctas. En una iteración puede moverse o atacar, pero
	 * no las dos a la vez.
	 */
	public void registrarAcciones() {
		if (agente.puedoAtacar()) {
			Posicion posMonstruo = agente.atacar();
			retirarMonstruo(posMonstruo);
		} else {
			agente.elegirMovimiento();
			agente.mover();
			cuadroPos(agente.getPosAnterior()).setAgente(false);
			cuadroPos(agente.getPosActual()).setAgente(true);
		}
	}

	/**
	 * Retira un monstruo y su hedor de la cueva
	 *
	 * @param p posición en la que estaba el monstruo
	 */
	private void retirarMonstruo(Posicion p) {
		// Quitar el monstruo
		cuadroPos(p).setMonstruo(false);
		monstruos--;
		// Quitar el hedor
		if (noHayMonstruoCerca(p)) {
			// Anterior posición del monstruo
			cuadroPos(p).setHedor(false);
		}
		// Posiciones adyacentes
		for (Orientacion o : Orientacion.values()) {
			if (noHayMonstruoCerca(p.adyacente(o))) {
				cuadroPos(p.adyacente(o)).setHedor(false);
			}
		}
	}

	/**
	 * Determina si hay un monstruo en una posición adyacente
	 *
	 * @param p posición alrededor de la cual se comprueba
	 * @return boolean indicando si hay un monstruo en una posición adyacente
	 */
	private boolean noHayMonstruoCerca(Posicion p) {
		for (Orientacion o : Orientacion.values()) {
			Posicion adyacente = p.adyacente(o);
			if (posicionCorrecta(adyacente.getFila(), adyacente.getColumna())
					&& cuadroPos(adyacente).isMonstruo()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Devuelve el cuadro en la posición indicada
	 *
	 * @param p posición que se quiere comprobar
	 * @return cuadro de la posición indicada
	 */
	private Cuadro cuadroPos(Posicion p) {
		return cuadros[p.getFila() - 1][p.getColumna() - 1];
	}

	/**
	 * Agrega un agente a la cueva. Siempre lo sitúa en la posición inicial.
	 */
	public void agregarAgente() {
		agente = new Agente(new Posicion(1, 1), Orientacion.ESTE);
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
		//si había otro tesoro, se sustituye por el nuevo
		if (tesoro != null) {
			cuadroPos(tesoro).setTesoro(false);
		}
		cuadros[fila - 1][columna - 1].setTesoro(true);
		tesoro = new Posicion(fila, columna);
		return true;
	}

	/**
	 * Agrega un monstruo a una posición si es posible
	 *
	 * @param fila    fila de la posición
	 * @param columna columna de la posición
	 */
	public void agregarMonstruo(int fila, int columna) {
		if (!posicionCorrecta(fila, columna) || noRespetaPosicionInicial(fila, columna)
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
		if (!posicionCorrecta(fila, columna) || noRespetaPosicionInicial(fila, columna)
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
	 * Determina si una combinación de fila y columna es la posición inicial (1,1) de la cueva o adyacente a ella
	 *
	 * @param fila    fila que se comprueba
	 * @param columna columna que se comprueba
	 * @return no respeta la posición inicial
	 */
	private boolean noRespetaPosicionInicial(int fila, int columna) {
		return (!posicionCorrecta(fila, columna) || (fila == 1 && columna == 1)
				|| adyacenteInicial(fila, columna));
	}

	/**
	 * Determina si una posición es adyacente a la posición inicial (1,1)
	 *
	 * @param fila    fila de la posición
	 * @param columna columna de la posición
	 * @return boolean indicando si son adyacentes
	 */
	private static boolean adyacenteInicial(int fila, int columna) {
		return (fila - 1 == 1 && columna == 1) // NORTE
				|| (fila == 1 && columna - 1 == 1); // ESTE
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
	 * @return está dentro del rango de la cueva
	 */
	private boolean posicionCorrecta(int fila, int columna) {
		return fila > 0 && fila <= size && columna > 0 && columna <= size;
	}

	public int getSize() {
		return size;
	}

	public boolean isTesoroEncontrado() {
		return tesoroEncontrado;
	}

	public int getMonstruos() {
		return monstruos;
	}
}
