/*
  Agente

  06/12/2020
 */
package com.cueva.monstruo.entitys;

import com.cueva.monstruo.entitys.Informacion.Monstruo;
import com.cueva.monstruo.entitys.Informacion.Precipicio;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Un agente que razona para encontrar el tesoro en la cueva del monstruo
 *
 * @author gianm
 */
@SuppressWarnings("SpellCheckingInspection")
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
	private final HashMap<Posicion, Informacion> bc; //base de conocimiento
	private final ArrayDeque<Posicion> porVisitar; //posiciones por visitar
	ArrayList<ArrayDeque<Posicion>> caminos;
	ArrayDeque<Posicion> camino;
	private ArrayDeque<Orientacion> pasos; //camino fijo hacia una posición
	private static final int INFINITO = 1000000;

	public Agente(Posicion posicion, Orientacion orientacion) {
		this.posActual = posicion;
		this.posAnterior = posicion;
		this.orientacion = orientacion;
		this.bc = new HashMap<>();
		porVisitar = new ArrayDeque<>();
		pasos = new ArrayDeque<>();
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
			pasos = buscarCamino(posActual, new Posicion(1, 1));
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
		//actualizar la posición actual
		Informacion infoActual = new Informacion();
		infoActual.setHedor(hedor);
		infoActual.setBrisa(brisa);
		infoActual.setMonstruo(Monstruo.NO);
		infoActual.setPrecipicio(Precipicio.NO);
		infoActual.setVisitado(true);
		registrar(posActual, infoActual);
		//actualizar las posiciones adyacentes
		actualizarAdyacentes(posActual);
		//actualizar el resto de posiciones
		actualizarResto();
		//guardar las posiciones seguras adyacentes no visitadas en orden Sur, Este, Norte, Oeste
		for (int i = Orientacion.values().length - 1; i >= 0; i--) {
			Posicion adyacente = posActual.adyacente(Orientacion.values()[i]);
			if (!posSegura(adyacente) || consultar(bc, adyacente).isVisitado()) {
				continue;
			}
			if (!porVisitar.contains(adyacente) && !consultar(bc, adyacente).isConsiderado()) {
				porVisitar.push(adyacente);
				consultar(bc, adyacente).setConsiderado(true);
			}
		}
	}

	/**
	 * Actualizar la información sobre las posiciones adyacentes a la que se ha visitado en base a las percepciones
	 * obtenidas. Se infiere conocimiento basándose en lo que ya hay en la base de conocimiento y las reglas lógicas que
	 * definen la conducta del agente y el comportamiento del entorno.
	 *
	 * @param central posición que se ha visitado
	 */
	private void actualizarAdyacentes(Posicion central) {
		Posicion posAdyacente;
		Informacion infoAdyacente;
		boolean hedor = consultar(bc, central).isHedor();
		boolean brisa = consultar(bc, central).isBrisa();
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
			infoAdyacente = consultar(bc, posAdyacente);
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
			Posicion posicion = par.getKey();
			Informacion informacion = par.getValue();
			// Si no se ha visitado, se ignora
			if (!informacion.isVisitado()) {
				continue;
			}
			boolean hedor = informacion.isHedor();
			boolean brisa = informacion.isBrisa();
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
				inforAdyacente = consultar(bc, posAdyacente);
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
		}
	}

	/**
	 * Determina la acción a realizar por el agente en base al conocimiento que tiene sobre el entorno. Si ya ha
	 * encontrado el tesoro, lo que hace es recorrer el camino de vuelta al punto de partida. Se elige una posición
	 * aleatoria de las adyacentes, dando prioridad a las no visitadas.
	 */
	public void elegirMovimiento() {
		//si ya ha encontrado el tesoro o está dirigiéndose a una posición, sigue el camino
		if (!pasos.isEmpty()) {
			movimiento = pasos.pop();
			return;
		}
		if (!porVisitar.isEmpty()) {
			//si quedan posiciones por visitar, elige una
			Posicion proximaPos = porVisitar.pop();
			if (proximaPos.esAdyacente(posActual)) {
				movimiento = posActual.direccion(proximaPos);
			} else {
				pasos = buscarCamino(posActual, proximaPos);
				movimiento = pasos.pop();
			}
		} else {
			//si no, elige un movimiento al azar
			List<Orientacion> visitadas = new ArrayList<>();
			List<Orientacion> nuevas = new ArrayList<>();
			for (Orientacion o : Orientacion.values()) {
				if (!posSegura(posActual.adyacente(o))) {
					continue;
				}
				if (consultar(bc, posActual.adyacente(o)).isVisitado()) {
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
	}

	/**
	 * Devuelve el camino que hay que recorrer desde una posición para llegar a otra
	 *
	 * @param origen  posición inicial del camino
	 * @param destino posición final del camino
	 * @return secuencia de movimientos que se tienen que hacer
	 */
	private ArrayDeque<Orientacion> buscarCamino(Posicion origen, Posicion destino) {
		HashMap<Posicion, Informacion> conocimiento = clonarBaseConocimiento(bc);
		//marcar todas las posiciones como no visitadas
		for (Map.Entry<Posicion, Informacion> par : conocimiento.entrySet()) {
			Posicion posicion = par.getKey();
			consultar(conocimiento, posicion).setVisitado(false);
		}
		//buscar el camino más corto de origen a destino
		caminos = new ArrayList<>();
		camino = new ArrayDeque<>();
		busquedaEnProfundidad(conocimiento, origen, destino);
		ArrayDeque<Posicion> caminoMinimo = new ArrayDeque<>();
		int distanciaMinima = INFINITO;
		for (ArrayDeque<Posicion> actual : caminos) {
			if (actual.size() < distanciaMinima) {
				distanciaMinima = actual.size();
				caminoMinimo = actual;
			}
		}
		caminoMinimo.removeFirst(); //quitar el origen del camino
		//convertir la lista de posiciones a movimientos
		return convertirAMovimientos(origen, caminoMinimo);
	}

	private void busquedaEnProfundidad(HashMap<Posicion, Informacion> conocimiento,
									   Posicion origen, Posicion destino) {
		if (consultar(conocimiento, origen).isVisitado()) return;
		consultar(conocimiento, origen).setVisitado(true);
		camino.add(origen);
		if (origen.equals(destino)) {
			caminos.add(camino.clone());
			consultar(conocimiento, origen).setVisitado(false);
			camino.removeLast();
			return;
		}
		//buscar recursivamente las posiciones seguras adyacentes en orden Sur, Este, Norte, Oeste
		for (Orientacion o : Orientacion.values()) {
			Posicion adyacente = origen.adyacente(o);
			if (posSegura(adyacente)) busquedaEnProfundidad(conocimiento, adyacente, destino);
		}
		camino.removeLast();
		consultar(conocimiento, origen).setVisitado(false);
	}

	public HashMap<Posicion, Informacion> clonarBaseConocimiento(HashMap<Posicion, Informacion> bc) {
		HashMap<Posicion, Informacion> copia = new HashMap<>();
		//marcar todas las posiciones como no visitadas
		for (Map.Entry<Posicion, Informacion> par : bc.entrySet()) {
			Posicion posicion = par.getKey();
			Informacion informacion = par.getValue();
			copia.put(posicion, new Informacion(informacion));
		}
		return copia;
	}

	/**
	 * Convierte una lista de posiciones a movimientos
	 *
	 * @param origen     posición inicial
	 * @param posiciones lista de posiciones a convertir
	 * @return lista de movimientos
	 */
	private ArrayDeque<Orientacion> convertirAMovimientos(Posicion origen, ArrayDeque<Posicion> posiciones) {
		ArrayDeque<Orientacion> movimientos = new ArrayDeque<>();
		Posicion posicion = origen;
		Orientacion movimiento;
		while (!posiciones.isEmpty()) {
			movimiento = posicion.direccion(posiciones.removeFirst());
			movimientos.add(movimiento);
			posicion = posicion.adyacente(movimiento);
		}
		return movimientos;
	}

	/**
	 * Se mueve hacia una nueva posición y actualiza los datos sobre la posición actual y la orientación
	 */
	public void mover() {
		orientacion = movimiento;
		System.out.println("Me muevo hacia el " + movimiento);
		posAnterior = posActual;
		posActual = posActual.adyacente(movimiento);
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
		Informacion infoMonstruo = consultar(bc, posMonstruo);
		flechas--;
		infoMonstruo.setMonstruo(Monstruo.NO);
		System.out.println("(Agente) He eliminado al monstruo de la posicion " + posMonstruo);
		return posMonstruo;
	}

	/**
	 * Devolver la información asociada a una posición
	 *
	 * @param p posición que se quiere consultar
	 * @return percepción asociada a la posición que se consulta
	 */
	public Informacion consultar(HashMap<Posicion, Informacion> bc, Posicion p) {
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

	/**
	 * Comprueba si hay un monstruo que sea visible para el agente (horizontalmente o verticalmente)
	 *
	 * @return si hay un monstruo visible
	 */
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

	/**
	 * Devuelve la posición del primer monstruo visible que se encuentra (horizontalmente o verticalmente)
	 *
	 * @return la posición del primer monstruo que se encuentra
	 */
	private Posicion monstruoVisible() {
		Iterator<Map.Entry<Posicion, Informacion>> it = bc.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Posicion, Informacion> par = it.next();
			Posicion posicion = par.getKey();
			Informacion informacion = par.getValue();
			if (informacion.getMonstruo() == Monstruo.SI
					&& (posicion.getFila() == this.posActual.getFila()
					|| posicion.getColumna() == this.posActual.getColumna())) {
				return posicion;
			}
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
		return existeRegistro(p) && consultar(bc, p).esSegura();
	}

	/**
	 * Determina si una posición de la cueva es posible dado el conocimiento actual
	 *
	 * @param p posición que se quiere comprobar
	 * @return boolean indicando si es posible
	 */
	private boolean posPosible(@NotNull Posicion p) {
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
				|| consultar(bc, p).getMonstruo() != Monstruo.NO);
	}

	/**
	 * Determina si es posible que en una posición haya un precipicio dado el conocimiento actual
	 *
	 * @param p posición que se comprueba
	 * @return boolean indicando si es posible
	 */
	private boolean posiblePrecipicio(Posicion p) {
		return posPosible(p) && (!existeRegistro(p)
				|| consultar(bc, p).getPrecipicio() != Precipicio.NO);
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
