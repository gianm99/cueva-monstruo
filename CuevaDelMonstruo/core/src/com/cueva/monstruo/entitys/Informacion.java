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
	private boolean hedor;
	private boolean brisa;
	private Monstruo monstruo;
	private Precipicio precipicio;

	public Informacion() {
		visitado = false;
		hedor = false;
		brisa = false;
		monstruo = Monstruo.POSIBLE;
		precipicio = Precipicio.POSIBLE;
	}

	/**
	 * Determina si la información que se tiene sobre una posición indica que es segura para ser visitada
	 *
	 * @return boolean indicando si es segura
	 */
	public boolean esSegura() {
		return monstruo == Monstruo.NO && precipicio == Precipicio.NO;
	}

	public boolean noHayHedor() {
		return !hedor;
	}

	public void setHedor(boolean hedor) {
		this.hedor = hedor;
	}

	public boolean noHayBrisa() {
		return !brisa;
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
