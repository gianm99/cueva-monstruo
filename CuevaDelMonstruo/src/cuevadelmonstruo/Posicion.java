/**
 * Posicion
 *
 * 06/12/20
 */
package cuevadelmonstruo;

import cuevadelmonstruo.Agente.Orientacion;

/**
 * Una posición en la cueva del monstruo
 *
 * @author gianm
 */
public class Posicion {

	private final int fila;
	private final int columna;
	private final int hash; // hash asociado a una posición

	public Posicion(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
		// Se calcula un hash único para la combinación de fila y columna
		int suma = fila + columna;
		hash = suma * (suma + 1) / 2 + fila;
	}

	/**
	 * Determina si una posición es correcta. Una posición es correcta si es una combinación de dos
	 * números enteros mayores a cero y menores al número máximo de filas y columnas
	 *
	 * @return boolean indicando si la posición es correcta
	 */
	public boolean esCorrecta() {
		return fila > 0 && fila <= Cueva.FILAS_MAX && columna > 0 && columna <= Cueva.COLUMNAS_MAX;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Posicion) {
			return ((Posicion) obj).fila == fila && ((Posicion) obj).columna == columna;
		}
		return false;
	}

	/**
	 * Devuelve la posición que se encuentra en la dirección que se indique
	 *
	 * @param orientacion dirección en la que se encuentra la posición adyacente
	 * @return posición adyacente en el sentido indicado
	 */
	public Posicion adyacente(Orientacion orientacion) {
		switch (orientacion) {
			case NORTE:
				return new Posicion(fila + 1, columna);
			case ESTE:
				return new Posicion(fila, columna + 1);
			case SUR:
				return new Posicion(fila - 1, columna);
			default:
				return new Posicion(fila, columna - 1);
		}
	}

	//================================================================================
	// Getters y setters
	//================================================================================
	public int getFila() {
		return fila;
	}

	public int getColumna() {
		return columna;
	}
}
