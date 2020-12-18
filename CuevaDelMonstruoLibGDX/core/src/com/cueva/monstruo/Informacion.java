/*
  Información

  07/12/20
 */
package com.cueva.monstruo;

/**
 * Una información sobre una posición de la cueva
 *
 * @author gianm
 */
public class Informacion {

	private boolean hedor;
	private boolean brisa;
	private boolean resplandor;
	private Monstruo monstruo;
	private Precipicio precipicio;
	private boolean visitado;

	/**
	 * Determina si la información que se tiene sobre una posición indica que es segura para ser
	 * visitada
	 *
	 * @return boolean indicando si es segura
	 */
	public boolean segura() {
		return monstruo == Monstruo.NO && precipicio == Precipicio.NO;
	}

	//================================================================================
	// Getters y setters
	//================================================================================
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

	public boolean isResplandor() {
		return resplandor;
	}

	public void setResplandor(boolean resplandor) {
		this.resplandor = resplandor;
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

	//================================================================================
	// Enums
	//================================================================================
	/**
	 * Distintos tipos de información que se puede tener sobre la existencia de un monstruo en una
	 * posición
	 */
	public enum Monstruo {
		NO, // No hay un monstruo
		POSIBLE, // Es posible que haya un monstruo
		SI			// Hay un monstruo
	}

	/**
	 * Distintos tipos de información que se puede tener sobre la existencia de un monstruo en una
	 * posición
	 */
	public enum Precipicio {
		NO, // No hay un precipicio
		POSIBLE, // Es posible que haya un precipicio
		SI			// Hay un precipicio
	}

}
