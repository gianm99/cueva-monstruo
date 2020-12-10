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
		Cueva cueva = new Cueva(4, 4);
		cueva.agregarMonstruo(2, 4);
		cueva.agregarPrecipicio(2, 3);
		cueva.agregarPrecipicio(4, 4);
		cueva.agregarTesoro(4, 1);
		do {
			cueva.obtenerPercepciones();
			cueva.realizarAcciones();
		} while (!cueva.haTerminado());
	}

}
