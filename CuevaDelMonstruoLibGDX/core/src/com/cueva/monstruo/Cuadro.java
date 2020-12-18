/*
  Cuadro

  06/12/20
 */
package com.cueva.monstruo;

/**
 * Un cuadro de la cueva que tiene unas características determinadas
 *
 * @author gianm
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Cuadro {

	private boolean tesoro;		// Contiene un tesoro
	private boolean resplandor;	// Se puede ver un resplandor
	private boolean monstruo;	// Contiene un monstruo
	private boolean hedor;		// Se puede oler un hedor
	private boolean precipicio;	// Contiene un precipicio
	private boolean brisa;		// Se puede sentir una brisa
	private boolean agente;		// Contiene al agente
//	private Rectangle2D.Float rec;
	private Imagen imagen;

	public Cuadro() {
//		this.rec = rec;
//		this.rec.x+=2;
//		this.rec.y+=2;
//		this.rec.width-=2;
//		this.rec.height-=2;
	}

//	public void paintComponent(Graphics g) {
//		Graphics2D g2d = (Graphics2D) g;
//		Rectangle2D.Float borde = new Rectangle2D.Float(this.rec.x - 2, this.rec.y - 2,
//				this.rec.width + 2, this.rec.height + 2);
//		g2d.setColor(Color.BLACK);
//		g2d.fill(borde);
//		g2d.setColor(Color.WHITE);
//		g2d.fill(this.rec);
//		if (agente && tesoro) {
//			imagen = new Imagen(Imagen.AGENTE_TESORO);
//			this.imagen.paintComponent(g, this.rec.x, this.rec.y);
//		} else if (agente) {
//			imagen = new Imagen(Imagen.AGENTE);
//			this.imagen.paintComponent(g, this.rec.x, this.rec.y);
//		} else if (tesoro) {
//			imagen = new Imagen(Imagen.TESORO);
//			this.imagen.paintComponent(g, this.rec.x, this.rec.y);
//		} else if (monstruo && precipicio) {
//			imagen = new Imagen(Imagen.MONSTRUO_PRECIPICIO);
//			this.imagen.paintComponent(g, this.rec.x, this.rec.y);
//		} else if (monstruo) {
//			imagen = new Imagen(Imagen.MONSTRUO);
//			this.imagen.paintComponent(g, this.rec.x, this.rec.y);
//		} else if (precipicio) {
//			imagen = new Imagen(Imagen.PRECIPICIO);
//			this.imagen.paintComponent(g, this.rec.x, this.rec.y);
//		}
//	}
//
	//================================================================================
	// Getters y setters
	//================================================================================
	public boolean isTesoro() {
		return tesoro;
	}

	public void setTesoro(boolean tesoro) {
		this.tesoro = tesoro;
	}

	public boolean isResplandor() {
		return resplandor;
	}

	public void setResplandor(boolean resplandor) {
		this.resplandor = resplandor;
	}

	public boolean isMonstruo() {
		return monstruo;
	}

	public void setMonstruo(boolean monstruo) {
		this.monstruo = monstruo;
	}

	public boolean isHedor() {
		return hedor;
	}

	public void setHedor(boolean hedor) {
		this.hedor = hedor;
	}

	public boolean isPrecipicio() {
		return precipicio;
	}

	public void setPrecipicio(boolean precipicio) {
		this.precipicio = precipicio;
	}

	public boolean isBrisa() {
		return brisa;
	}

	public void setBrisa(boolean brisa) {
		this.brisa = brisa;
	}

	public boolean isAgente() {
		return agente;
	}

	public void setAgente(boolean agente) {
		this.agente = agente;
	}
}
