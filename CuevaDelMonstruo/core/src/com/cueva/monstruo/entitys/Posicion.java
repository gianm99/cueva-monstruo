/*
  Posicion

  06/12/20
 */
package com.cueva.monstruo.entitys;

/**
 * Una posición en la cueva del monstruo
 *
 * @author gianm
 */
public class Posicion {

	private final int fila;        // Fila de la posición
	private final int columna;    // Columna de la posición
	private final int hash;        // Hash asociado a una posición

	public Posicion(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
		// Se calcula un hash único para la combinación de fila y columna
		int suma = fila + columna;
		hash = suma * (suma + 1) / 2 + fila;
	}

	/**
	 * Determina si una posición es correcta. Una posición es correcta si es una combinación de dos números enteros
	 * mayores a cero y menores al número máximo posible de filas y columnas
	 *
	 * @return boolean indicando si la posición es correcta
	 */
	public boolean esPosible() {
		return fila > 0 && fila <= Cueva.DIMENSION_MAX && columna > 0 && columna <= Cueva.DIMENSION_MAX;
	}

	/**
	 * Determina si la posición es el punto de partida del agente
	 *
	 * @return boolean indicando si es el punto de partida
	 */
	public boolean esInicial() {
		return fila == 1 && columna == 1;
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

	/**
	 * Determina si una posición es adyacente a la actual
	 * @param posicion posición que se comprueba
	 * @return si es adyacente
	 */
	public boolean esAdyacente(Posicion posicion){
		return posicion.equals(adyacente(Orientacion.ESTE))
				|| posicion.equals(adyacente(Orientacion.NORTE))
				|| posicion.equals(adyacente(Orientacion.OESTE))
				|| posicion.equals(adyacente(Orientacion.SUR));
	}

	/**
	 * Devuelve la dirección en la que se encuentra una posición respecto a esta
	 *
	 * @param posicion posición adyacente a esta posición
	 * @return dirección en la que está respecto a esta posición
	 */
	public Orientacion direccion(Posicion posicion) {
		if (posicion.getFila() < fila) {
			return Orientacion.SUR;
		} else if (posicion.getFila() > fila) {
			return Orientacion.NORTE;
		} else if (posicion.getColumna() < columna) {
			return Orientacion.OESTE;
		} else {
			return Orientacion.ESTE;
		}
	}

	@Override
	public String toString() {
		return "(" + fila + ", " + columna + ")";
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

	public int getFila() {
		return fila;
	}

	public int getColumna() {
		return columna;
	}
}
