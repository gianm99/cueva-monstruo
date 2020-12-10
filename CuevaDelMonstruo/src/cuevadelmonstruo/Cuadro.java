/**
 * Cuadro
 *
 * 06/12/20
 */
package cuevadelmonstruo;

/**
 * Un cuadro de la cueva que tiene unas características determinadas
 *
 * @author gianm
 */
public class Cuadro {

	private boolean tesoro;		// Contiene un tesoro
	private boolean resplandor;	// Se puede ver un resplandor
	private boolean monstruo;	// Contiene un monstruo
	private boolean hedor;		// Se puede oler un hedor
	private boolean precipicio;	// Contiene un precipicio
	private boolean brisa;		// Se puede sentir una brisa

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

}
