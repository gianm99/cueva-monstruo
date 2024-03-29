/*
  Cuadro

  06/12/20
 */
package com.cueva.monstruo.entitys;

/**
 * Un cuadro de la cueva que tiene unas características determinadas
 *
 * @author gianm
 */
public class Cuadro {

    private boolean tesoro;                    //hay un tesoro
    private boolean encontrado;                 //el tesoro ha sido encontrado
    private boolean monstruo;                //hay un monstruo
    private boolean hedor;                    //hay hedor
    private boolean precipicio;                //hay un precipicio
    private boolean brisa;                    //hay brisa
    private boolean agente;                    //el agente está en el cuadro

    public boolean isTesoro() {
        return tesoro;
    }

    public void setTesoro(boolean tesoro) {
        this.tesoro = tesoro;
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

    public void setEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }

    public boolean isEncontrado() {
        return encontrado;
    }
}
