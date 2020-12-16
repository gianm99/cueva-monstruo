/**
 * Imagen
 *
 * 13/12/20
 */
package cuevadelmonstruosimple;

/**
 * Una imagen que se dibujará sobre una posición de la cueva
 *
 * @author gianm
 */
import java.awt.Graphics;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class Imagen {

	private BufferedImage img;

	public static final String AGENTE = "img/agente.png";
	public static final String AGENTE_TESORO = "img/agente-tesoro.png";
	public static final String MONSTRUO = "img/monstruo.png";
	public static final String MONSTRUO_PRECIPICIO = "img/monstruo-precipicio.png";
	public static final String PRECIPICIO = "img/precipicio.png";
	public static final String TESORO = "img/tesoro.png";

	public Imagen(String s) {
		try {
			img = ImageIO.read(new File(s));
		} catch (IOException e) {
			System.out.println("Problema al leer la imagen");
		}
	}

	void paintComponent(Graphics g, float x, float y) {
		g.drawImage(img, (int) x, (int) y, Cueva.COSTADO, Cueva.COSTADO, null);
	}

}
