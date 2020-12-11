/**
 * Agente
 *
 * 06/12/2020
 */
package cuevadelmonstruo;

import cuevadelmonstruo.Informacion.Monstruo;
import cuevadelmonstruo.Informacion.Precipicio;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Un agente que razona para encontrar el tesoro en la cueva del monstruo
 *
 * @author gianm
 */
public class Agente {

	private boolean tesoro;				// Tesoro encontrado
	private boolean terminado;			// Ha regresado al punto de partida con el tesoro
	private boolean filasConocidas;		// Se conoce el número de filas de la cueva
	private boolean columnasConocidas;	// Se conoce el número de columnas de la cueva
	private int filas;					// Si se conoce, número de filas de la cueva
	private int columnas;				// Si se conoce, número de columnas de la cueva
	private Posicion posicion;			// Posición en la que está
	private Orientacion orientacion;	// Hacia donde apunta
	private Orientacion movimiento;		// Dirección a la que se va a mover
	private Deque<Posicion> historial;	// Historial de movimientos hasta el tesoro
	private BaseConocimiento BC;		// Base de conocimiento

	public Agente(Posicion posicion, Orientacion orientacion) {
		this.orientacion = orientacion;
		this.posicion = posicion;
		BC = new BaseConocimiento();
		historial = new ArrayDeque<>();
	}

	/**
	 * Actualizar la posición que ha visitado el agente y las adyacentes. Se infiere conocimiento
	 * basándose en lo que ya hay en la base de conocimiento y las reglas lógicas que definen la
	 * conducta del agente y el comportamiento del entorno.
	 *
	 * @param hedor
	 * @param brisa
	 * @param resplandor
	 * @param golpe
	 */
	public void actualizar(boolean hedor, boolean brisa, boolean resplandor, boolean golpe) {
		if (resplandor) {
			tesoro = true;
			return;
		}
		if (!tesoro) {
			historial.push(posicion);
		}
		if (golpe) {
			switch (orientacion) {
				case NORTE:
					filasConocidas = true;
					filas = posicion.getFila();
					break;
				case ESTE:
					columnasConocidas = true;
					columnas = posicion.getColumna();
					break;
				default:
			}
		}
		// Se actualiza la posición que se está visitando
		Informacion infoActual = new Informacion();
		infoActual.setMonstruo(Monstruo.NO);
		infoActual.setPrecipicio(Precipicio.NO);
		infoActual.setVisitado(true);
		infoActual.setHedor(hedor);
		infoActual.setBrisa(brisa);
		infoActual.setResplandor(resplandor);
		BC.registrar(posicion, infoActual);
		// Se actualizan las posiciones adyacentes
		actualizarAdyacentes(posicion, hedor, brisa);
	}

	/**
	 * Actualizar la información sobre las posiciones adyacentes a la que se ha visitado en base a
	 * las percepciones obtenidas. Se infiere conocimiento basándose en lo que ya hay en la base de
	 * conocimiento y las reglas lógicas que definen la conducta del agente y el comportamiento del
	 * entorno.
	 *
	 * @param central posición que se ha visitado
	 * @param hedor boolean que indica si se ha sentido un hedor
	 * @param brisa boolean que indica si se ha sentido una brisa
	 */
	private void actualizarAdyacentes(Posicion central, boolean hedor, boolean brisa) {
		Posicion posicionAd; // Posición adyacente
		Informacion informacionAd; // Información de la posición adyacente
		for (Orientacion o1 : Orientacion.values()) {
			posicionAd = central.adyacente(o1);
			// Si la posición no es posible, se ignora
			if (!posicionPosible(posicionAd)) {
				continue;
			}
			// Si la información es nueva, se guarda
			if (!BC.existeRegistro(posicionAd)) {
				informacionAd = new Informacion();
				informacionAd.setMonstruo(hedor ? Monstruo.POSIBLE : Monstruo.NO);
				informacionAd.setPrecipicio(brisa ? Precipicio.POSIBLE : Precipicio.NO);
				BC.registrar(posicionAd, informacionAd);
				continue;
			}
			informacionAd = BC.consultar(posicionAd);
			// Si antes no había monstruo ni precipicio, se deja igual
			if (informacionAd.getMonstruo() == Monstruo.NO
					&& informacionAd.getPrecipicio() == Precipicio.NO) {
				continue;
			}
			// Si no hay hedor ni brisa, no hay monstruo ni precipicio
			if (!hedor && !brisa) {
				informacionAd.setMonstruo(Monstruo.NO);
				informacionAd.setPrecipicio(Precipicio.NO);
				continue;
			}
			// Se comprueba si se puede confirmar que hay monstruo o precipicio
			boolean monstruoConfirmado = true;
			boolean precipicioConfirmado = true;
			for (Orientacion o2 : Orientacion.values()) {
				if (o1 != o2) {
					if (!posibleMonstruo(central.adyacente(o2))) {
						monstruoConfirmado = false;
					}
					if (!posiblePrecipicio(central.adyacente(o2))) {
						precipicioConfirmado = false;
					}
					if (!monstruoConfirmado && !precipicioConfirmado) {
						break;
					}
				}
			}
			if (informacionAd.getMonstruo() != Monstruo.NO) {
				if (!hedor) {
					informacionAd.setMonstruo(Monstruo.NO);
				} else if (monstruoConfirmado) {
					informacionAd.setMonstruo(Monstruo.SI);
				} else {
					informacionAd.setMonstruo(Monstruo.POSIBLE);
				}
			}
			if (informacionAd.getPrecipicio() != Precipicio.NO) {
				if (!brisa) {
					informacionAd.setPrecipicio(Precipicio.NO);
				} else if (precipicioConfirmado) {
					informacionAd.setPrecipicio(Precipicio.SI);
				} else {
					informacionAd.setPrecipicio(Precipicio.POSIBLE);
				}
			}
		}
	}

	/**
	 * Determina la acción a realizar por el agente en base al conocimiento que tiene sobre el
	 * entorno. Si ya ha encontrado el tesoro, lo que hace es recorrer el camino de vuelta al punto
	 * de partida. Se elige una posición aleatoria de las adyacentes, dando prioridad a las no
	 * visitadas.
	 */
	public void elegirAccion() {
		if (tesoro) {
			movimiento = posicion.direccion(historial.pop());
			return;
		}
		List<Orientacion> visitadas = new ArrayList<>();
		List<Orientacion> nuevas = new ArrayList<>();
		for (Orientacion o : Orientacion.values()) {
			if (!posicionSegura(posicion.adyacente(o))) {
				continue;
			}
			if (BC.consultar(posicion.adyacente(o)).isVisitado()) {
				visitadas.add(o);
			} else {
				nuevas.add(o);
			}
		}
		Random rand = new Random();
		if (!nuevas.isEmpty()) {
			movimiento = nuevas.get(rand.nextInt(nuevas.size()));
		} else {
			movimiento = visitadas.get(rand.nextInt(visitadas.size()));
		}
	}

	/**
	 * Se mueve hacia una nueva posición y actualiza los datos sobre la posición actual y la
	 * orientación
	 */
	public void mover() {
		orientacion = movimiento; // Se gira
		System.out.println("Me muevo hacia el " + movimiento);
		posicion = posicion.adyacente(movimiento); // Se mueve
		System.out.println("Ahora estoy en " + posicion);
		if (tesoro && posicion.esPuntoPartida()) {
			// Si tiene el tesoro y está en el punto de partida, ha terminado
			terminado = true;
		}
	}

	/**
	 * Determina si es posible que en una posición haya un monstruo dado el conocimiento obtenido
	 *
	 * @param p posición que se comprueba
	 * @return boolean indicando si es posible
	 */
	public boolean posibleMonstruo(Posicion p) {
		return posicionPosible(p) && BC.existeRegistro(p)
				&& BC.consultar(p).getMonstruo() != Monstruo.NO;
	}

	/**
	 * Determina si es posible que en una posición haya un precipicio dado el conocimiento obtenido
	 *
	 * @param p posición que se comprueba
	 * @return boolean indicando si es posible
	 */
	public boolean posiblePrecipicio(Posicion p) {
		return posicionPosible(p) && BC.existeRegistro(p)
				&& BC.consultar(p).getPrecipicio() != Precipicio.NO;
	}

	/**
	 * Determina si una posición de la cueva (combinación de fila y columna) es segura para visitar
	 * dado el conocimiento obtenido
	 *
	 * @param p posición que se quiere comprobar
	 * @return boolean indicando si es segura
	 */
	public boolean posicionSegura(Posicion p) {
		return BC.existeRegistro(p) && BC.consultar(p).segura();
	}

	/**
	 * Determina si una posición de la cueva (combinación de fila y columna) es posible dado el
	 * conocimiento obtenido
	 *
	 * @param p posición que se quiere comprobar
	 * @return boolean indicando si es posible
	 */
	public boolean posicionPosible(Posicion p) {
		return p.esPosible()
				&& (!filasConocidas || p.getFila() <= filas)
				&& (!columnasConocidas || p.getColumna() <= columnas);
	}

	//================================================================================
	// Getters y setters
	//================================================================================
	public Posicion getPosicion() {
		return posicion;
	}

	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}

	public Orientacion getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(Orientacion orientacion) {
		this.orientacion = orientacion;
	}

	public boolean isTesoro() {
		return tesoro;
	}

	public void setTesoro(boolean tesoro) {
		this.tesoro = tesoro;
	}

	public boolean isTerminado() {
		return terminado;
	}

	public void setTerminado(boolean terminado) {
		this.terminado = terminado;
	}

	//================================================================================
	// Enums y clases
	//================================================================================
	/**
	 * Orientación que puede tener un agente
	 */
	public enum Orientacion {
		ESTE, NORTE, OESTE, SUR;
		private static final Orientacion[] vals = values();

		/**
		 * Girar una posición hacia adelante en el sentido horario
		 *
		 * @return la siguiente posición en sentido horario
		 */
		public Orientacion derecha() {
			return vals[Math.floorMod((this.ordinal() + 1), vals.length)];
		}

		/**
		 * Girar una posición hacia atrás en el sentido horario
		 *
		 * @return la anterior posición en sentido horario
		 */
		public Orientacion izquierda() {
			return vals[Math.floorMod((this.ordinal() - 1), vals.length)];
		}
	}
}
