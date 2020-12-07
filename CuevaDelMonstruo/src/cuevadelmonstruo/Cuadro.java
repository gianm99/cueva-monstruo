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
	private boolean bordeNorte;	// Hay un borde en la zona norte
	private boolean bordeEste;	// Hay un borde en la zona este
	private boolean bordeSur;	// Hay un borde en la zona sur
	private boolean bordeOeste;	// Hay un borde en la zona oeste

	//================================================================================
	// Getters y setters
	//================================================================================
	public void setBordeOeste(boolean bordeOeste) {
		this.bordeOeste = bordeOeste;
	}

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

	public boolean isBordeNorte() {
		return bordeNorte;
	}

	public void setBordeNorte(boolean bordeNorte) {
		this.bordeNorte = bordeNorte;
	}

	public boolean isBordeEste() {
		return bordeEste;
	}

	public void setBordeEste(boolean bordeEste) {
		this.bordeEste = bordeEste;
	}

	public boolean isBordeSur() {
		return bordeSur;
	}

	public void setBordeSur(boolean bordeSur) {
		this.bordeSur = bordeSur;
	}

	public boolean isBordeOeste() {
		return bordeOeste;
	}
}
