/**
 * Cueva
 *
 * 06/12/20
 */
package cuevadelmonstruo;

/**
 * Una cueva con unas características determinadas
 *
 * @author gianm
 */
public class Cueva {

	private final int columnas;
	private final int filas;
	private final Cuadro[][] cuadros;
	static final int COLUMNAS_MIN = 2;
	static final int FILAS_MIN = 2;
	static final int COLUMNAS_MAX = 100;
	static final int FILAS_MAX = 100;

	public Cueva(int filas, int columnas) {
		this.filas = filas;
		this.columnas = columnas;
		cuadros = new Cuadro[filas][columnas];
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				cuadros[i][j] = new Cuadro();
			}
		}
	}

}
