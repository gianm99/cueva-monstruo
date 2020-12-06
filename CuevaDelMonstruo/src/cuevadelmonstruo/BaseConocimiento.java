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

	private final HashMap<Posicion, Informacion> bc = new HashMap();

	/**
	 * Comprueba si existe un registro con información asociada a una posición en concreto
	 *
	 * @param fila fila de la posición que se quiere comprobar
	 * @param columna columna de la posición que se quiere comprobar
	 * @return boolean que indica si hay o no un registro
	 */
	public boolean existeRegistro(int fila, int columna) {
		return bc.containsKey(new Posicion(fila, columna));
	}

	/**
	 * Devolver la información asociada a una posición
	 *
	 * @param fila fila de la posición sobre la que se consulta
	 * @param columna columna de la posición sobre la que se consulta
	 * @return percepción asociada a la posición que se consulta
	 */
	public Informacion consultar(int fila, int columna) {
		return bc.get(new Posicion(fila, columna));
	}

	/**
	 * Registra la información que se ha percibido o inferido sobre una posición
	 *
	 * @param fila fila de la posición asociada a la percepción
	 * @param columna columna de la posición asociada a la percepción
	 * @param p percepción que se quiere registrar
	 */
	public void registrar(int fila, int columna, Informacion p) {
		// TODO Pensar si es necesario comprobar si ya existía registro
		bc.put(new Posicion(fila, columna), p);
	}
}
