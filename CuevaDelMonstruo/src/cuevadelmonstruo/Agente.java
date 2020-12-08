/**
 * Agente
 *
 * 06/12/2020
 */
package cuevadelmonstruo;

import cuevadelmonstruo.Informacion.Monstruo;
import cuevadelmonstruo.Informacion.Precipicio;

/**
 * Un agente que razona para encontrar el tesoro en la cueva del monstruo
 *
 * @author gianm
 */
public class Agente {

	private boolean hedor;				// Detecta hedor
	private boolean brisa;				// Detecta una brisa
	private boolean resplandor;			// Ve un resplandor
	private boolean golpe;              // Siente un golpe
	private boolean gemido;				// Escucha un (aterrorizante) gemido
	private boolean meta;				// Tesoro encontrado
	private Orientacion orientacion;	// Hacia donde apunta
	private int fila;					// Fila en t
	private int columna;				// Columna en t
	private int filaAnterior;			// Fila en t-1
	private int columnaAnterior;		// Columna en t-1
	private int t = 0;					// Reloj interno del agente
	private BaseConocimiento bc;

	public Agente(Orientacion orientacion, int fila, int columna) {
		this.orientacion = orientacion;
		this.fila = fila;
		this.columna = columna;
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
		this.meta = !this.meta ? resplandor : this.meta; // Coge el tesoro
		if (golpe) {
			switch (orientacion) {
				case NORTE:
					bc.registrarFilas(fila);
					break;
				case ESTE:
					bc.registrarColumnas(columna);
					break;
				default:
			}
		}
		// Se actualiza la posición que se está visitando
		Posicion posActual = new Posicion(fila, columna);
		Informacion infoActual = new Informacion(Monstruo.NO, Precipicio.NO);
		infoActual.setVisitado(true);
		infoActual.setHedor(hedor);
		infoActual.setBrisa(brisa);
		infoActual.setResplandor(resplandor);
		bc.registrar(posActual, infoActual);
		// Se actualizan las posiciones adyacentes
		actualizarAdyacentes(posActual, hedor, brisa);
		t++;	// Aumenta un ciclo en el reloj
	}

	/**
	 * Actualizar la información sobre las posiciones adyacentes a la que se ha visitado en base a
	 * las percepciones obtenidas. Se infiere conocimiento basándose en lo que ya hay en la base de
	 * conocimiento y las reglas lógicas que definen la conducta del agente y el comportamiento del
	 * entorno.
	 *
	 * @param posicion posición que se ha visitado
	 * @param hedor boolean que indica si se ha sentido un hedor
	 * @param brisa boolean que indica si se ha sentido una brisa
	 */
	private void actualizarAdyacentes(Posicion posicion, boolean hedor, boolean brisa) {
		Posicion posAdyacente;
		Informacion infoAdyacente;
		for (Orientacion o1 : Orientacion.values()) {
			posAdyacente = posicion.adyacente(o1);
			if (!posicionPosible(posAdyacente)) {
				continue; // Si está fuera de rango, se ignora
			}
			// Se verifica que, en el caso de que exista registro de la posición, se pueda afirmar 
			// la existencia de un monstruo o precipicio
			boolean hayMonstruo = true;
			boolean hayPrecipicio = true;
			if (bc.existeRegistro(posAdyacente) && (hedor || brisa)) {
				for (Orientacion o2 : Orientacion.values()) {
					if (o1 != o2) {
						if (!noHayMonstruo(posicion.adyacente(o2))) {
							hayMonstruo = false;
						}
						if (!noHayPrecipicio(posicion.adyacente(o2))) {
							hayPrecipicio = false;
						}
						if (!hayMonstruo && !hayPrecipicio) {
							break;
						}
					}
				}
			}
			Monstruo monstruo;
			if (!hedor) {
				monstruo = Monstruo.NO;
			} else if (hayMonstruo) {
				monstruo = Monstruo.SI;
			} else {
				monstruo = Monstruo.POSIBLE;
			}
			Precipicio precipicio;
			if (!brisa) {
				precipicio = Precipicio.NO;
			} else if (hayPrecipicio) {
				precipicio = Precipicio.SI;
			} else {
				precipicio = Precipicio.POSIBLE;
			}
			// Guardar la información
			if (bc.existeRegistro(posAdyacente)) {
				// Actualizar el registro
				infoAdyacente = bc.consultar(posAdyacente);
				infoAdyacente.setMonstruo(monstruo);
				infoAdyacente.setPrecipicio(precipicio);
			} else {
				// Crear el registro
				infoAdyacente = new Informacion(monstruo, precipicio);
				bc.registrar(posAdyacente, infoAdyacente);
			}
		}
	}

	/**
	 * Determina si se puede afirmar que en una posición no hay un monstruo
	 *
	 * @param p posición que se comprueba
	 * @return boolean indicando si se puede afirmar que no hay un monstruo
	 */
	public boolean noHayMonstruo(Posicion p) {
		return bc.existeRegistro(p) && bc.consultar(p).getMonstruo() == Monstruo.NO;
	}

	/**
	 * Determina si se puede afirmar que en una posición no hay un precipicio
	 *
	 * @param p posición que se comprueba
	 * @return boolean indicando si se puede afirmar que no hay un precipicio
	 */
	public boolean noHayPrecipicio(Posicion p) {
		return bc.existeRegistro(p) && bc.consultar(p).getPrecipicio() == Precipicio.NO;
	}

	/**
	 * Determina si una posición de la cueva (combinación de fila y columna) es posible dado el
	 * conocimiento obtenido
	 *
	 * @param p posición que se quiere comprobar
	 * @return boolean indicando si es posible
	 */
	public boolean posicionPosible(Posicion p) {
		return p.esCorrecta()
				&& (!bc.isFilasConocidas() || p.getFila() <= bc.getFilas())
				&& (!bc.isColumnasConocidas() || p.getColumna() <= bc.getColumnas());
	}

	/**
	 * Girar 90 grados en sentido horario
	 */
	public void girarDerecha() {
		orientacion = orientacion.derecha();
	}

	/**
	 * Girar -90 grados en sentido horario
	 */
	public void girarIzquierda() {
		orientacion = orientacion.izquierda();

	}

	//================================================================================
	// Enums
	//================================================================================
	/**
	 * Orientación que puede tener un agente
	 */
	public enum Orientacion {
		ESTE, NORTE, OESTE, SUR; // TODO Comprobar si este cambio de orden causa problemas
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
		 * @return la anterior posición posición en sentido horario
		 */
		public Orientacion izquierda() {
			return vals[Math.floorMod((this.ordinal() - 1), vals.length)];
		}
	}
}
