/**
 * BaseConocimiento
 *
 * 06/12/20
 */
package cuevadelmonstruo;

import java.util.HashMap;

/**
 * Una base de conocimiento para solucionar el problema de la cueva del monstruo
 *
 * @author gianm
 */
public class BaseConocimiento {

	private int filas;
	private int columnas;
	private boolean filasConocidas;
	private boolean columnasConocidas;
	private final HashMap<Posicion, Informacion> bc = new HashMap();

	/**
	 * Comprueba si existe un registro con información asociada a una posición en concreto
	 *
	 * @param p posición que se quiere comprobar
	 * @return boolean que indica si hay o no un registro
	 */
	public boolean existeRegistro(Posicion p) {
		return bc.containsKey(p);
	}

	/**
	 * Devolver la información asociada a una posición
	 *
	 * @param p posición que se quiere consultar
	 * @return percepción asociada a la posición que se consulta
	 */
	public Informacion consultar(Posicion p) {
		return bc.get(p);
	}

	/**
	 * Registra la información que se ha percibido o inferido sobre una posición
	 *
	 * @param p posición que se quiere registrar
	 * @param i percepción que se quiere registrar
	 */
	public void registrar(Posicion p, Informacion i) {
		if (p.getFila() == 1) {
			i.setBordeSur(true);
		} else if (filasConocidas && p.getFila() == filas) {
			i.setBordeNorte(true);
		}
		if (p.getColumna() == 1) {
			i.setBordeOeste(true);
		} else if (columnasConocidas && p.getColumna() == columnas) {
			i.setBordeEste(true);
		}
		bc.put(p, i);
	}

	/**
	 * Guarda el número de filas que tiene la cueva
	 *
	 * @param filas número de filas que se ha determinado que tiene la cueva
	 */
	public void registrarFilas(int filas) {
		this.filas = filas;
		filasConocidas = true;
	}

	/**
	 * Guarda el número de columnas que tiene la cueva
	 *
	 * @param columnas número de columnas que se ha determinado que tiene la cueva
	 */
	public void registrarColumnas(int columnas) {
		this.columnas = columnas;
		columnasConocidas = true;
	}

	//================================================================================
	// Getters y setters
	//================================================================================
	public int getFilas() {
		return filas;
	}

	public int getColumnas() {
		return columnas;
	}

	public boolean isFilasConocidas() {
		return filasConocidas;
	}

	public boolean isColumnasConocidas() {
		return columnasConocidas;
	}
}
