package cuevadelmonstruo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author gianm
 */
public class Juego extends JFrame implements ActionListener {

	private Cueva cueva;
	private javax.swing.JButton botonAvanzar;
	private javax.swing.JButton botonReiniciar;
	private javax.swing.JButton botonAutomatico;
	private javax.swing.JButton botonMonstruo;
	private javax.swing.JButton botonPrecipicio;
	private javax.swing.JButton botonTamCueva;
	private javax.swing.JButton botonTerminar;
	private javax.swing.JButton botonTesoro;
	private javax.swing.JSpinner columnaMonstruo;
	private javax.swing.JSpinner columnaPrecipicio;
	private javax.swing.JSpinner columnaTesoro;
	private javax.swing.JDialog dialogoConfiguracion;
	private javax.swing.JSpinner filaMonstruo;
	private javax.swing.JSpinner filaPrecipicio;
	private javax.swing.JSpinner filaTesoro;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem menuAvanzar;
	private javax.swing.JMenuItem menuReiniciar;
	private javax.swing.JSpinner tamCueva;
	private JPanel principal;

	public Juego() throws InterruptedException {
		// Dialogo
		initComponentes();
		// Crear el JPanel principal
		principal = new JPanel();
		principal.setLayout(new BorderLayout());
		dialogoConfiguracion.pack();
		dialogoConfiguracion.setLocationRelativeTo(null);
		dialogoConfiguracion.setVisible(true);
		botonTamCueva.setEnabled(true);
		botonTesoro.setEnabled(false);
		botonMonstruo.setEnabled(false);
		botonPrecipicio.setEnabled(false);
		botonTerminar.setEnabled(false);
		cueva = new Cueva();
		cueva.setPreferredSize(new Dimension(550, 550));
		principal.setSize(cueva.getPreferredSize());
		principal.setBackground(Color.BLACK);
		// Botones
		botonReiniciar = new JButton("Reiniciar");
		botonReiniciar.addActionListener(this);
		botonReiniciar.setActionCommand("reiniciar");
		principal.add(botonReiniciar, BorderLayout.WEST);
		botonAvanzar = new JButton("Avanzar");
		botonAvanzar.addActionListener(this);
		botonAvanzar.setActionCommand("avanzar");
		principal.add(botonAvanzar, BorderLayout.EAST);

		botonAutomatico = new JButton("Automático");
		botonAutomatico.addActionListener(this);
		botonAutomatico.setActionCommand("auto");
		principal.add(botonAutomatico, BorderLayout.SOUTH);
		this.setResizable(false);
		// Agregar el JPanel principal
		this.add(principal);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws InterruptedException {
		Juego juego = new Juego();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		if (action.equals("reiniciar")) {
			dialogoConfiguracion.pack();
			dialogoConfiguracion.setLocationRelativeTo(null);
			dialogoConfiguracion.setVisible(true);
			botonTamCueva.setEnabled(true);
			botonTesoro.setEnabled(false);
			botonMonstruo.setEnabled(false);
			botonPrecipicio.setEnabled(false);
			botonTerminar.setEnabled(false);
			principal.remove(cueva);
			cueva = new Cueva();
			cueva.setPreferredSize(new Dimension(550, 550));
			principal.revalidate();
			this.repaint();
		} else if (action.equals("avanzar")) {
			if (!cueva.haTerminado()) {
				cueva.obtenerPercepciones();
				cueva.realizarAcciones();
				principal.revalidate();
				principal.paintImmediately(0, 0, principal.getWidth(), principal.getHeight());
			}
		} else if (action.equals("auto")) {
			do {
				cueva.obtenerPercepciones();
				cueva.realizarAcciones();
				principal.revalidate();
				principal.paintImmediately(0, 0, principal.getWidth(), principal.getHeight());
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
				}
			} while (!cueva.haTerminado());

		}
	}

	public void initComponentes() {
		dialogoConfiguracion = new javax.swing.JDialog();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		tamCueva = new javax.swing.JSpinner();
		jLabel3 = new javax.swing.JLabel();
		filaTesoro = new javax.swing.JSpinner();
		jLabel4 = new javax.swing.JLabel();
		columnaTesoro = new javax.swing.JSpinner();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		filaMonstruo = new javax.swing.JSpinner();
		jLabel7 = new javax.swing.JLabel();
		columnaMonstruo = new javax.swing.JSpinner();
		botonMonstruo = new javax.swing.JButton();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		filaPrecipicio = new javax.swing.JSpinner();
		jLabel10 = new javax.swing.JLabel();
		columnaPrecipicio = new javax.swing.JSpinner();
		botonPrecipicio = new javax.swing.JButton();
		botonTerminar = new javax.swing.JButton();
		jLabel11 = new javax.swing.JLabel();
		botonTamCueva = new javax.swing.JButton();
		botonTesoro = new javax.swing.JButton();

		dialogoConfiguracion.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		dialogoConfiguracion.setTitle("Configuración");
		dialogoConfiguracion.setAlwaysOnTop(true);
		dialogoConfiguracion.setSize(new java.awt.Dimension(494, 246));
		dialogoConfiguracion.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				dialogoConfiguracionWindowClosed(evt);
			}
		});

		jLabel1.setText("¿En qué posición debe estar el tesoro?");

		jLabel2.setText("¿Qué tamaño debe tener la cueva? Elige n, la cueva es de nxn:");

		tamCueva.setModel(new javax.swing.SpinnerNumberModel(2, 1, 10, 1));
		tamCueva.setName("tamCuevaSpinner"); // NOI18N

		jLabel3.setText("Fila:");

		filaTesoro.setModel(new javax.swing.SpinnerNumberModel(2, 1, 10, 1));

		jLabel4.setText("Columna:");

		columnaTesoro.setModel(new javax.swing.SpinnerNumberModel(2, 1, 10, 1));

		jLabel5.setText("Poner un monstruo en esta posición:");

		jLabel6.setText("Fila:");

		filaMonstruo.setModel(new javax.swing.SpinnerNumberModel(2, 1, 10, 1));

		jLabel7.setText("Columna:");

		columnaMonstruo.setModel(new javax.swing.SpinnerNumberModel(2, 1, 10, 1));

		botonMonstruo.setText("Confirmar");
		botonMonstruo.setEnabled(false);
		botonMonstruo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				botonMonstruoActionPerformed(evt);
			}
		});

		jLabel8.setText("Poner un precipicio en esta posición:");

		jLabel9.setText("Fila:");

		filaPrecipicio.setModel(new javax.swing.SpinnerNumberModel(2, 1, 10, 1));

		jLabel10.setText("Columna:");

		columnaPrecipicio.setModel(new javax.swing.SpinnerNumberModel(2, 1, 10, 1));

		botonPrecipicio.setText("Confirmar");
		botonPrecipicio.setEnabled(false);
		botonPrecipicio.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				botonPrecipicioActionPerformed(evt);
			}
		});

		botonTerminar.setText("Terminar");
		botonTerminar.setEnabled(false);
		botonTerminar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				botonTerminarActionPerformed(evt);
			}
		});

		jLabel11.setText("Solo se pondrán monstruos o precipicios en zonas permitidas");

		botonTamCueva.setText("Confirmar");
		botonTamCueva.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				botonTamCuevaMouseClicked(evt);
			}
		});
		botonTamCueva.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				botonTamCuevaActionPerformed(evt);
			}
		});

		botonTesoro.setText("Confirmar");
		botonTesoro.setEnabled(false);
		botonTesoro.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				botonTesoroActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout dialogoConfiguracionLayout = new javax.swing.GroupLayout(dialogoConfiguracion.getContentPane());
		dialogoConfiguracion.getContentPane().setLayout(dialogoConfiguracionLayout);
		dialogoConfiguracionLayout.setHorizontalGroup(
				dialogoConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(dialogoConfiguracionLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(dialogoConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(dialogoConfiguracionLayout.createSequentialGroup()
												.addGroup(dialogoConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
														.addGroup(dialogoConfiguracionLayout.createSequentialGroup()
																.addComponent(jLabel2)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(tamCueva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(dialogoConfiguracionLayout.createSequentialGroup()
																.addComponent(jLabel5)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(jLabel6)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(filaMonstruo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(18, 18, 18)
																.addComponent(jLabel7)
																.addGap(10, 10, 10)
																.addComponent(columnaMonstruo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(dialogoConfiguracionLayout.createSequentialGroup()
																.addComponent(jLabel8)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(jLabel9)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(filaPrecipicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(18, 18, 18)
																.addComponent(jLabel10)
																.addGap(10, 10, 10)
																.addComponent(columnaPrecipicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(dialogoConfiguracionLayout.createSequentialGroup()
																.addComponent(jLabel1)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(jLabel3)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
																.addComponent(filaTesoro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(18, 18, 18)
																.addComponent(jLabel4)
																.addGap(10, 10, 10)
																.addComponent(columnaTesoro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
												.addGap(18, 18, 18)
												.addGroup(dialogoConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(dialogoConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(botonMonstruo)
																.addComponent(botonPrecipicio)
																.addComponent(botonTamCueva, javax.swing.GroupLayout.Alignment.TRAILING))
														.addComponent(botonTesoro, javax.swing.GroupLayout.Alignment.TRAILING)))
										.addComponent(jLabel11))
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogoConfiguracionLayout.createSequentialGroup()
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(botonTerminar)
								.addGap(206, 206, 206))
		);
		dialogoConfiguracionLayout.setVerticalGroup(
				dialogoConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(dialogoConfiguracionLayout.createSequentialGroup()
								.addGap(35, 35, 35)
								.addGroup(dialogoConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel2)
										.addComponent(tamCueva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(botonTamCueva))
								.addGap(18, 18, 18)
								.addGroup(dialogoConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel1)
										.addComponent(jLabel3)
										.addComponent(filaTesoro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel4)
										.addComponent(columnaTesoro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(botonTesoro))
								.addGap(18, 18, 18)
								.addGroup(dialogoConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel5)
										.addComponent(jLabel6)
										.addComponent(filaMonstruo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel7)
										.addComponent(columnaMonstruo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(botonMonstruo))
								.addGap(18, 18, 18)
								.addGroup(dialogoConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel8)
										.addComponent(jLabel9)
										.addComponent(filaPrecipicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel10)
										.addComponent(columnaPrecipicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(botonPrecipicio))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(jLabel11)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(botonTerminar)
								.addContainerGap())
		);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Cueva del monstruo");
		setBackground(new java.awt.Color(153, 153, 153));
		setName("Ventana principal"); // NOI18N
		pack();
	}

	private void botonTamCuevaMouseClicked(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:

	}

	private void botonTamCuevaActionPerformed(java.awt.event.ActionEvent evt) {
		principal.remove(cueva);
		cueva.configurar((Integer) tamCueva.getValue());
		javax.swing.JButton source = (javax.swing.JButton) evt.getSource();
		source.setEnabled(false);
		// Activar el resto de botones
		botonTesoro.setEnabled(true);
		botonMonstruo.setEnabled(true);
		botonPrecipicio.setEnabled(true);
	}

	private void botonTesoroActionPerformed(java.awt.event.ActionEvent evt) {
		int fila = (Integer) filaTesoro.getValue();
		int columna = (Integer) columnaTesoro.getValue();
		if (fila == 1 && columna == 1) {
			return;
		}
		if (cueva.agregarTesoro(fila, columna)) {
			javax.swing.JButton source = (javax.swing.JButton) evt.getSource();
			source.setEnabled(false);
			botonTerminar.setEnabled(true);
		}
	}

	private void dialogoConfiguracionWindowClosed(java.awt.event.WindowEvent evt) {
		// TODO add your handling code here:
	}

	private void botonTerminarActionPerformed(java.awt.event.ActionEvent evt) {
		dialogoConfiguracion.dispose();
		dialogoConfiguracion.setVisible(false);
		cueva.agregarAgente();
		principal.add(cueva, BorderLayout.CENTER);
		this.revalidate();
		this.pack();
		this.setVisible(true);
	}

	private void botonMonstruoActionPerformed(java.awt.event.ActionEvent evt) {
		int fila = (Integer) filaMonstruo.getValue();
		int columna = (Integer) columnaMonstruo.getValue();
		cueva.agregarMonstruo(fila, columna);
	}

	private void botonPrecipicioActionPerformed(java.awt.event.ActionEvent evt) {
		int fila = (Integer) filaPrecipicio.getValue();
		int columna = (Integer) columnaPrecipicio.getValue();
		cueva.agregarPrecipicio(fila, columna);
	}

}
