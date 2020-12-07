/**
 * Información
 *
 * 07/12/20
 */
package cuevadelmonstruo;

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
	private boolean bordeNorte;
	private boolean bordeEste;
	private boolean bordeSur;
	private boolean bordeOeste;
	private boolean visitado;

	public Informacion(Monstruo monstruo, Precipicio precipicio) {
		this.monstruo = monstruo;
		this.precipicio = precipicio;
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

	public void setBordeOeste(boolean bordeOeste) {
		this.bordeOeste = bordeOeste;
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
