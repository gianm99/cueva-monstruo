/*
  Información

  07/12/20
 */
package com.cueva.monstruo.entitys;

/**
 * Una información sobre una posición de la cueva
 *
 * @author gianm
 */
public class Informacion {

	private boolean visitado;
	private boolean considerado;
	private boolean hedor;
	private boolean brisa;
	private Monstruo monstruo;
	private Precipicio precipicio;

	public Informacion() {
		visitado = false;
		considerado = false;
		hedor = false;
		brisa = false;
		monstruo = Monstruo.POSIBLE;
		precipicio = Precipicio.POSIBLE;
	}

	public Informacion(Informacion informacion) {
		this.visitado = informacion.visitado;
		this.hedor = informacion.hedor;
		this.brisa = informacion.brisa;
		this.monstruo = informacion.monstruo;
		this.precipicio = informacion.precipicio;
		this.considerado = informacion.considerado;
	}

	/**
	 * Determina si la información que se tiene sobre una posición indica que es segura para ser visitada
	 *
	 * @return boolean indicando si es segura
	 */
	public boolean esSegura() {
		return monstruo == Monstruo.NO && precipicio == Precipicio.NO;
	}

	public boolean isHedor() {
		return hedor;
	}

	public void setHedor(boolean hedor) {
		this.hedor = hedor;
	}

	public boolean isBrisa() {
		return brisa;
	}

	public void setBrisa(boolean brisa) {
		this.brisa = brisa;
	}

	public Monstruo getMonstruo() {
		return monstruo;
	}

	public void setMonstruo(Monstruo monstruo) {
		this.monstruo = monstruo;
	}

	public Precipicio getPrecipicio() {
		return precipicio;
	}

	public void setPrecipicio(Precipicio precipicio) {
		this.precipicio = precipicio;
	}

	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}

	public boolean isConsiderado() {
		return considerado;
	}

	public void setConsiderado(boolean considerado) {
		this.considerado = considerado;
	}

	/**
	 * Distintos tipos de información que se puede tener sobre la existencia de un monstruo en una posición
	 */
	public enum Monstruo {
		POSIBLE,
		NO,
		SI
	}

	/**
	 * Distintos tipos de información que se puede tener sobre la existencia de un monstruo en una posición
	 */
	public enum Precipicio {
		POSIBLE,
		NO,
		SI
	}

}
