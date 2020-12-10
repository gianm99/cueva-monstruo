/**
 * Cueva
 *
 * 06/12/20
 */
package cuevadelmonstruo;

import cuevadelmonstruo.Agente.Orientacion;

/**
 * Una cueva con unas características determinadas
 *
 * @author gianm
 */
public class Cueva {

	private final Agente agente;
	private final int columnas;
	private final int filas;
	private final Cuadro[][] cuadros;
	public static final int COLUMNAS_MIN = 3;
	public static final int FILAS_MIN = 3;
	public static final int COLUMNAS_MAX = 100;
	public static final int FILAS_MAX = 100;

	public Cueva(int filas, int columnas) {
		this.filas = filas;
		this.columnas = columnas;
		cuadros = new Cuadro[filas][columnas];
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				cuadros[i][j] = new Cuadro();
			}
		}
		agente = new Agente(new Posicion(1, 1), Orientacion.ESTE);
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
		return (posicion.getFila() == filas && orientacion == Orientacion.NORTE)
				|| (posicion.getColumna() == columnas && orientacion == Orientacion.ESTE);
	}

	/**
	 * Hacer que el agente realice las acciones que considere correctas
	 */
	public void realizarAcciones() {
		agente.elegirAccion();
		agente.mover();
	}

	/**
	 * Agrega un tesoro a una posición si es posible
	 *
	 * @param fila fila de la posición
	 * @param columna fila de la posición
	 */
	public void agregarTesoro(int fila, int columna) {
		if (!posicionCorrecta(fila, columna)
				|| cuadros[fila - 1][columna - 1].isMonstruo()
				|| cuadros[fila - 1][columna - 1].isPrecipicio()) {
			return;
		}
		cuadros[fila - 1][columna - 1].setTesoro(true);
	}

	/**
	 * Agrega un monstruo a una posición si es posible
	 *
	 * @param fila fila de la posición
	 * @param columna columna de la posición
	 */
	public void agregarMonstruo(int fila, int columna) {
		if (!posicionCorrecta(fila, columna)
				|| cuadros[fila - 1][columna - 1].isTesoro()) {
			return;
		}
		cuadros[fila - 1][columna - 1].setMonstruo(true);
		expandirHedor(fila, columna);
	}

	/**
	 * Agrega el hedor de un monstruo a su posición y las adyacentes
	 *
	 * @param fila fila de la posición del monstruo
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
	 * @param fila fila de la posición
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
	 * @param fila fila de la posición
	 * @param columna columna de la posición
	 */
	public void agregarPrecipicio(int fila, int columna) {
		if (!posicionCorrecta(fila, columna)
				|| cuadros[fila - 1][columna - 1].isTesoro()) {
			return;
		}
		cuadros[fila - 1][columna - 1].setMonstruo(true);
		expandirBrisa(fila, columna);
	}

	/**
	 * Agrega la brisa de un precipicio a su posición y las adyacentes
	 *
	 * @param fila fila de la posición del precipicio
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
	 * @param fila fila de la posición
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
		return (posicionCorrecta(fila, columna) && !adyacentes(1, 1, fila, columna));
	}

	/**
	 * Determina si dos posiciones son adyacentes
	 *
	 * @param fila1 fila de la posición 1
	 * @param columna1 columna de la posición 1
	 * @param fila2 fila de la posición 2
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
	 * @param fila fila de la posición que se comprueba
	 * @param columna columna de la posición que se comprueba
	 * @return
	 */
	private boolean posicionCorrecta(int fila, int columna) {
		return fila > 0 && fila <= filas && columna > 0 && columna <= columnas;
	}
}
