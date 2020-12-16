/**
 * BaseConocimiento
 *
 * 06/12/20
 */
package cuevadelmonstruosimple;

import cuevadelmonstruosimple.Agente.Orientacion;
import cuevadelmonstruosimple.Informacion.Monstruo;
import cuevadelmonstruosimple.Informacion.Precipicio;
import java.util.HashMap;

/**
 * Una base de conocimiento para solucionar el problema de la cueva del monstruo. Sirve para guardar
 * la información sobre cada posición de la cueva con la que el agente se encuentre.
 *
 * @author gianm
 */
public class BaseConocimiento {

	private boolean filasConocidas;		// Se conoce el número de filas de la cueva
	private boolean columnasConocidas;	// Se conoce el número de columnas de la cueva
	private int filas;					// Si se conoce, número de filas de la cueva
	private int columnas;				// Si se conoce, número de columnas de la cueva
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

	/**
	 * Actualiza toda la información que ya existe en la BC para intentar inferir nuevo conocimiento
	 */
	public void actualizarTodo() {
		for (HashMap.Entry<Posicion, Informacion> entry : bc.entrySet()) {
			Posicion posicion = entry.getKey();
			Informacion informacion = entry.getValue();
			// Si no se ha visitado, se ignora
			if (!informacion.isVisitado()) {
				continue;
			}
			// Si no hay hedor ni brisa, se ignora
			if (!informacion.isHedor() && !informacion.isBrisa()) {
				continue;
			}
			Posicion posicionAd; // Posición adyacente
			Informacion informacionAd; // Información de la posición adyacente
			for (Orientacion o1 : Orientacion.values()) {
				posicionAd = posicion.adyacente(o1);
				// Si no existe registro, se ignora
				if (!existeRegistro(posicionAd)) {
					continue;
				}
				informacionAd = consultar(posicionAd);
				// Si antes no había monstruo ni había un posible precipicio, se deja igual
				if (informacionAd.getMonstruo() == Monstruo.NO
						&& informacionAd.getPrecipicio() != Precipicio.POSIBLE) {
					continue;
				}
				// Se comprueba si se puede confirmar que hay monstruo o precipicio
				boolean monstruoConfirmado = true;
				boolean precipicioConfirmado = true;
				for (Orientacion o2 : Orientacion.values()) {
					if (o1 != o2) {
						if (posibleMonstruo(posicion.adyacente(o2))) {
							monstruoConfirmado = false;
						}
						if (posiblePrecipicio(posicion.adyacente(o2))) {
							precipicioConfirmado = false;
						}
						if (!monstruoConfirmado && !precipicioConfirmado) {
							break;
						}
					}
				}
				// Se elimina el hedor si no hay origen posible
				if (monstruoConfirmado && informacionAd.getMonstruo() == Monstruo.NO) {
					informacion.setHedor(false);
				}
				// Solo se comprueba si había un monstruo o es posible
				if (informacionAd.getMonstruo() != Monstruo.NO) {
					if (!informacion.isHedor()) {
						informacionAd.setMonstruo(Monstruo.NO);
					} else if (monstruoConfirmado) {
						informacionAd.setMonstruo(Monstruo.SI);
					} else {
						informacionAd.setMonstruo(Monstruo.POSIBLE);
					}
				}
				// Solo se comprueba si hay un posible precipicio
				if (informacionAd.getPrecipicio() == Precipicio.POSIBLE) {
					if (!informacion.isBrisa()) {
						informacionAd.setPrecipicio(Precipicio.NO);
					} else if (precipicioConfirmado) {
						informacionAd.setPrecipicio(Precipicio.SI);
					} else {
						informacionAd.setPrecipicio(Precipicio.POSIBLE);
					}
				}
			}
		}
	}

	public Posicion monstruoVisible(Posicion agente) {
		for (HashMap.Entry<Posicion, Informacion> entry : bc.entrySet()) {
			Posicion posicion = entry.getKey();
			Informacion informacion = entry.getValue();
			if (informacion.getMonstruo() == Monstruo.SI
					&& (posicion.getFila() == agente.getFila()
					|| posicion.getColumna() == agente.getColumna())) {
				informacion.setMonstruo(Monstruo.NO); // mata al monstruo
				return posicion;
			}
		}
		return null;
	}

	/**
	 * Determina si una posición de la cueva es posible dado el conocimiento obtenido
	 *
	 * @param p posición que se quiere comprobar
	 * @return boolean indicando si es posible
	 */
	public boolean posicionPosible(Posicion p) {
		return p.esPosible()
				&& (!filasConocidas || p.getFila() <= filas)
				&& (!columnasConocidas || p.getColumna() <= columnas);
	}

	/**
	 * Determina si es posible que en una posición haya un monstruo dado el conocimiento obtenido
	 *
	 * @param p posición que se comprueba
	 * @return boolean indicando si es posible
	 */
	public boolean posibleMonstruo(Posicion p) {
		return posicionPosible(p) && existeRegistro(p)
				&& consultar(p).getMonstruo() != Monstruo.NO;
	}

	/**
	 * Determina si es posible que en una posición haya un precipicio dado el conocimiento obtenido
	 *
	 * @param p posición que se comprueba
	 * @return boolean indicando si es posible
	 */
	public boolean posiblePrecipicio(Posicion p) {
		return posicionPosible(p) && existeRegistro(p)
				&& consultar(p).getPrecipicio() != Precipicio.NO;
	}

	public void registrarFilas(int filas) {
		filasConocidas = true;
		this.filas = filas;
	}

	public void registrarColumnas(int columnas) {
		columnasConocidas = true;
		this.columnas = columnas;
	}
}
