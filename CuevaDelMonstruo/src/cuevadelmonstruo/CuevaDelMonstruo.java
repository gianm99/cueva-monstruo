/**
 * CuevaDelMonstruo
 *
 * 06/12/2020
 */
package cuevadelmonstruo;

/**
 * Problema de la cueva del monstruo
 *
 * @author gianm
 */
public class CuevaDelMonstruo {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Cueva cueva = new Cueva(6, 5);
		cueva.agregarMonstruo(2, 3);
		cueva.agregarTesoro(1, 5);
		do {
			cueva.obtenerPercepciones();
			cueva.realizarAcciones();
		} while (!cueva.haTerminado());
	}

}
