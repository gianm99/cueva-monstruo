/**
 * Posicion
 *
 * 06/12/20
 */
package cuevadelmonstruo;

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
}
