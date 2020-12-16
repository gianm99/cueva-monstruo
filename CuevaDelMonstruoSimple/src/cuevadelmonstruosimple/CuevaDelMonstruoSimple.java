package cuevadelmonstruosimple;

/**
 *
 * @author gianm
 */
import javax.swing.*;

public class CuevaDelMonstruoSimple extends JFrame {

	public Cueva cueva;

	public CuevaDelMonstruoSimple() throws InterruptedException {
		super("Cueva del monstruo");
		cueva = new Cueva();
		cueva.configurar(6,6);
		cueva.agregarMonstruo(2, 2);
		cueva.agregarTesoro(5, 5);
		cueva.agregarPrecipicio(3, 3);
		cueva.agregarPrecipicio(3, 4);
		cueva.agregarMonstruo(3, 4);
		this.setSize(cueva.getPreferredSize());
		this.add(cueva);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws InterruptedException {
		CuevaDelMonstruoSimple cuevaDelMonstruo = new CuevaDelMonstruoSimple();
//		cuevaDelMonstruo.setVisible(true);
	}

}
