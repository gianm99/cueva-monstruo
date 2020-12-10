/**
 * BaseConocimiento
 *
 * 06/12/20
 */
package cuevadelmonstruo;

import java.util.HashMap;

/**
 * Una base de conocimiento para solucionar el problema de la cueva del monstruo. Sirve para guardar
 * la información sobre cada posición de la cueva con la que el agente se encuentre.
 *
 * @author gianm
 */
public class BaseConocimiento {

	private final HashMap<Posicion, Informacion> bc = new HashMap();

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
		bc.put(p, i);
	}

	/**
	 * Comprueba si existe un registro con información asociada a una posición en concreto
	 *
	 * @param p posición que se quiere comprobar
	 * @return boolean que indica si hay o no un registro
	 */
	public boolean existeRegistro(Posicion p) {
		return bc.containsKey(p);
	}
}
