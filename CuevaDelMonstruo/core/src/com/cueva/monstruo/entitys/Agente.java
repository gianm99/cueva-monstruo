/*
  Agente

  06/12/2020
 */
package com.cueva.monstruo.entitys;

import com.cueva.monstruo.entitys.Informacion.Monstruo;
import com.cueva.monstruo.entitys.Informacion.Precipicio;

import java.util.*;

/**
 * Un agente que razona para encontrar el tesoro en la cueva del monstruo
 *
 * @author gianm
 */
public class Agente {

	private boolean tesoro; //tesoro encontrado
	private boolean terminado; //tesoro encontrado y en posición inicial
	private boolean dimensionConocida; //conoce la dimensión de la cueva
	private int dimension; //dimensión de la cueva (si es conocida)
	private int flechas; //flechas restantes
	private Posicion posActual; //posición actual
	private Posicion posAnterior; //posición anterior
	private Orientacion orientacion; //dirección en la que mira
	private Orientacion movimiento; //movimiento a realizar
	private final Deque<Posicion> historial; //historial de movimientos
	private final HashMap<Posicion, Informacion> bc; //base de conocimiento

	public Agente(Posicion posicion, Orientacion orientacion) {
		this.posActual = posicion;
		this.posAnterior = posicion;
		this.orientacion = orientacion;
		this.historial = new ArrayDeque<>();
		historial.push(posActual);
		this.bc = new HashMap<>();
	}

	/**
	 * Actualizar la posición que ha visitado el agente y las adyacentes. Se infiere conocimiento basándose en lo que ya
	 * hay en la base de conocimiento y las reglas lógicas que definen la conducta del agente y el comportamiento del
	 * entorno.
	 *
	 * @param hedor      ha detectado hedor
	 * @param brisa      ha notado una brisa
	 * @param resplandor ha visto un resplandor
	 * @param golpe      ha notado un golpe
	 */
	public void actualizar(boolean hedor, boolean brisa, boolean resplandor, boolean golpe) {
		if (resplandor) {
			tesoro = true;
		}
		if (golpe) {
			switch (orientacion) {
				case NORTE:
					dimensionConocida = true;
					dimension = posActual.getFila();
					break;
				case ESTE:
					dimensionConocida = true;
					dimension = posActual.getColumna();
					break;
				default:
			}
		}
		// Se actualiza la posición que se está visitando
		Informacion infoActual = new Informacion();
		infoActual.setHedor(hedor);
		infoActual.setBrisa(brisa);
		infoActual.setMonstruo(Monstruo.NO);
		infoActual.setPrecipicio(Precipicio.NO);
		infoActual.setVisitado(true);
		registrar(posActual, infoActual);
		// Se actualizan las posiciones adyacentes
		actualizarAdyacentes(posActual, hedor, brisa);
		// Se actualizan el resto de posiciones descubiertas
//		actualizarResto();
	}

	/**
	 * Actualizar la información sobre las posiciones adyacentes a la que se ha visitado en base a las percepciones
	 * obtenidas. Se infiere conocimiento basándose en lo que ya hay en la base de conocimiento y las reglas lógicas que
	 * definen la conducta del agente y el comportamiento del entorno.
	 *
	 * @param central posición que se ha visitado
	 * @param hedor   boolean que indica si se ha sentido un hedor
	 * @param brisa   boolean que indica si se ha sentido una brisa
	 */
	private void actualizarAdyacentes(Posicion central, boolean hedor, boolean brisa) {
		//TODO Quitar parámetros redundantes (hedor y brisa)
		Posicion posAdyacente; // Posición adyacente
		Informacion infoAdyacente; // Información de la posición adyacente
		for (Orientacion o1 : Orientacion.values()) {
			posAdyacente = central.adyacente(o1);
			// Si la posición no es posible, se ignora
			if (!posPosible(posAdyacente)) {
				continue;
			}
			// Si la información es nueva, se guarda
			if (!existeRegistro(posAdyacente)) {
				infoAdyacente = new Informacion();
				infoAdyacente.setMonstruo(hedor ? Monstruo.POSIBLE : Monstruo.NO);
				infoAdyacente.setPrecipicio(brisa ? Precipicio.POSIBLE : Precipicio.NO);
				registrar(posAdyacente, infoAdyacente);
				continue;
			}
			infoAdyacente = consultar(posAdyacente);
			// Si antes no había monstruo ni precipicio, se deja igual
			if (infoAdyacente.getMonstruo() == Monstruo.NO
					&& infoAdyacente.getPrecipicio() == Precipicio.NO) {
				continue;
			}
			// Si no hay hedor ni brisa, no hay monstruo ni precipicio
			if (!hedor && !brisa) {
				infoAdyacente.setMonstruo(Monstruo.NO);
				infoAdyacente.setPrecipicio(Precipicio.NO);
				continue;
			}
			// Se comprueba si se puede confirmar que hay monstruo o precipicio
			boolean monstruoConfirmado = true;
			boolean precipicioConfirmado = true;
			for (Orientacion o2 : Orientacion.values()) {
				if (o1 != o2) {
					if (posibleMonstruo(central.adyacente(o2))) {
						monstruoConfirmado = false;
					}
					if (posiblePrecipicio(central.adyacente(o2))) {
						precipicioConfirmado = false;
					}
					if (!monstruoConfirmado && !precipicioConfirmado) {
						break;
					}
				}
			}
			if (hedor) {
				if (infoAdyacente.getMonstruo() == Monstruo.POSIBLE && monstruoConfirmado)
					infoAdyacente.setMonstruo(Monstruo.SI);
			} else {
				infoAdyacente.setMonstruo(Monstruo.NO);
			}
			if (brisa) {
				if (infoAdyacente.getPrecipicio() == Precipicio.POSIBLE && precipicioConfirmado)
					infoAdyacente.setPrecipicio(Precipicio.SI);
			} else {
				infoAdyacente.setPrecipicio(Precipicio.NO);
			}
		}
	}

	/**
	 * Actualiza toda la información que ya existe en la BC para intentar inferir nuevo conocimiento
	 */
	public void actualizarResto() {
		Iterator<Map.Entry<Posicion, Informacion>> it = bc.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Posicion, Informacion> par = it.next();
			Posicion posicion = (Posicion) par.getKey();
			Informacion informacion = (Informacion) par.getValue();
			// Si no se ha visitado, se ignora
			if (!informacion.isVisitado()) {
				continue;
			}
			boolean hedor=!informacion.noHayHedor();
			boolean brisa=!informacion.noHayBrisa();
			// Si no hay hedor ni brisa, se ignora
			if (!hedor && !brisa) {
				continue;
			}
			Posicion posAdyacente; // Posición adyacente
			Informacion inforAdyacente; // Información de la posición adyacente
			for (Orientacion o1 : Orientacion.values()) {
				posAdyacente = posicion.adyacente(o1);
				// Si no existe registro, se ignora
				if (!existeRegistro(posAdyacente)) {
					continue;
				}
				inforAdyacente = consultar(posAdyacente);
				// Si antes no había monstruo ni había un posible precipicio, se deja igual
				if (inforAdyacente.getMonstruo() == Monstruo.NO
						&& inforAdyacente.getPrecipicio() != Precipicio.POSIBLE) {
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
				if (monstruoConfirmado && inforAdyacente.getMonstruo() == Monstruo.NO) {
					informacion.setHedor(false);
				}
				if (hedor) {
					if (inforAdyacente.getMonstruo() == Monstruo.POSIBLE && monstruoConfirmado)
						inforAdyacente.setMonstruo(Monstruo.SI);
				} else {
					inforAdyacente.setMonstruo(Monstruo.NO);
				}
				if (brisa) {
					if (inforAdyacente.getPrecipicio() == Precipicio.POSIBLE && precipicioConfirmado)
						inforAdyacente.setPrecipicio(Precipicio.SI);
				} else {
					inforAdyacente.setPrecipicio(Precipicio.NO);
				}
			}
			it.remove();
		}
	}

	/**
	 * Determina la acción a realizar por el agente en base al conocimiento que tiene sobre el entorno. Si ya ha
	 * encontrado el tesoro, lo que hace es recorrer el camino de vuelta al punto de partida. Se elige una posición
	 * aleatoria de las adyacentes, dando prioridad a las no visitadas.
	 */
	public void elegirMovimiento() {
		//TODO Hacer algo más óptimo para volver a la casilla de salida
		if (tesoro) {
			movimiento = posActual.direccion(historial.pop());
			return;
		}
		List<Orientacion> visitadas = new ArrayList<>();
		List<Orientacion> nuevas = new ArrayList<>();
		for (Orientacion o : Orientacion.values()) {
			if (!posSegura(posActual.adyacente(o))) {
				continue;
			}
			if (consultar(posActual.adyacente(o)).isVisitado()) {
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
	 * Se mueve hacia una nueva posición y actualiza los datos sobre la posición actual y la orientación
	 */
	public void mover() {
		orientacion = movimiento;
		System.out.println("Me muevo hacia el " + movimiento);
		posAnterior = posActual;
		posActual = posActual.adyacente(movimiento);
		if (!tesoro) {
			historial.push(posActual);
		}
		System.out.println("Ahora estoy en " + posActual);
		if (tesoro && posActual.esInicial()) {
			terminado = true;
		}
	}

	/**
	 * Determina si puede atacar a algún monstruo. La decisión tiene en cuenta si hay un monstruo a la vista y si quedan
	 * suficientes flechas.
	 *
	 * @return se puede atacar a algún monstruo
	 */
	public boolean puedoAtacar() {
		return hayMonstruoVisible() && flechas > 0;
	}

	/**
	 * Ataca a un monstruo y devuelve la posición en la que estaba
	 *
	 * @return posición en la que estaba el monstruo
	 */
	public Posicion atacar() {
		Posicion posMonstruo = monstruoVisible();
		Informacion infoMonstruo = consultar(posMonstruo);
		flechas--;
		infoMonstruo.setMonstruo(Monstruo.NO);
		System.out.println("(Agente) He eliminado al monstruo de la posicion " + posMonstruo);
		return posMonstruo;
	}

	public Posicion getPosActual() {
		return posActual;
	}

	public Orientacion getOrientacion() {
		return orientacion;
	}

	public boolean isTerminado() {
		return terminado;
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
		bc.put(p, i);
	}

	/**
	 * Comprueba si existe un registro con información asociada a una posición en concreto
	 *
	 * @param p posición que se quiere comprobar
	 * @return boolean que indica si hay o no un registro
	 */
	public boolean existeRegistro(Posicion p) {
		return p.esPosible() && bc.containsKey(p);
	}

	private boolean hayMonstruoVisible() {
		for (HashMap.Entry<Posicion, Informacion> entry : bc.entrySet()) {
			Posicion posicion = entry.getKey();
			Informacion informacion = entry.getValue();
			if (informacion.getMonstruo() == Monstruo.SI
					&& (posicion.getFila() == this.posActual.getFila()
					|| posicion.getColumna() == this.posActual.getColumna())) {
				return true;
			}
		}
		return false;
	}

	private Posicion monstruoVisible() {
		Iterator<Map.Entry<Posicion, Informacion>> it = bc.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Posicion, Informacion> par = it.next();
			Posicion posicion = (Posicion) par.getKey();
			Informacion informacion = (Informacion) par.getValue();
			if (informacion.getMonstruo() == Monstruo.SI
					&& (posicion.getFila() == this.posActual.getFila()
					|| posicion.getColumna() == this.posActual.getColumna())) {
				return posicion;
			}
			it.remove();
		}
		return null;
	}

	/**
	 * Determina si una posición de la cueva (combinación de fila y columna) es segura para visitar dado el conocimiento
	 * obtenido
	 *
	 * @param p posición que se quiere comprobar
	 * @return boolean indicando si es segura
	 */
	private boolean posSegura(Posicion p) {
		return existeRegistro(p) && consultar(p).esSegura();
	}

	/**
	 * Determina si una posición de la cueva es posible dado el conocimiento actual
	 *
	 * @param p posición que se quiere comprobar
	 * @return boolean indicando si es posible
	 */
	private boolean posPosible(Posicion p) {
		return p.esPosible() && (!dimensionConocida || (p.getFila() <= dimension && p.getColumna() <= dimension));
	}

	/**
	 * Determina si es posible que en una posición haya un monstruo dado el conocimiento actual
	 *
	 * @param p posición que se comprueba
	 * @return boolean indicando si es posible
	 */
	private boolean posibleMonstruo(Posicion p) {
		return posPosible(p) && (!existeRegistro(p)
				|| consultar(p).getMonstruo() != Monstruo.NO);
	}

	/**
	 * Determina si es posible que en una posición haya un precipicio dado el conocimiento actual
	 *
	 * @param p posición que se comprueba
	 * @return boolean indicando si es posible
	 */
	private boolean posiblePrecipicio(Posicion p) {
		return posPosible(p) && (!existeRegistro(p)
				|| consultar(p).getPrecipicio() != Precipicio.NO);
	}

	public Posicion getPosAnterior() {
		return posAnterior;
	}

	public boolean isTesoro() {
		return tesoro;
	}

	public void setFlechas(int flechas) {
		this.flechas = flechas;
	}
}
