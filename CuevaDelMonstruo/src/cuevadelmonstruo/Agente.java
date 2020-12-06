/**
 * Agente
 *
 * 06/12/2020
 */
package cuevadelmonstruo;

/**
 * Un agente que razona para encontrar el tesoro en la cueva del monstruo
 *
 * @author gianm
 */
public class Agente {

    public Orientacion orientacion;
    private boolean hedor; // Detecta hedor
    private boolean brisa; // Detecta una brisa
    private boolean resplandor; // Ve un resplandor
    private boolean golpe; // Siente un golpe
    private boolean gemido; // Escucha un (aterrorizante) gemido

    /**
     * Girar 90 grados en sentido horario
     */
    public void girarDerecha() {
        orientacion = orientacion.derecha();
    }

    /**
     * Girar -90 grados en sentido horario
     */
    public void girarIzquierda() {
        orientacion = orientacion.izquierda();
    }

    /**
     * Orientacion que puede tener un agente
     */
    enum Orientacion {
        NORTE, ESTE, SUR, OESTE;
        private static final Orientacion[] vals = values();

        /**
         * Girar una posición hacia adelante en el sentido horario
         *
         * @return la siguiente posición en sentido horario
         */
        public Orientacion derecha() {
            return vals[Math.floorMod((this.ordinal() + 1), vals.length)];
        }

        /**
         * Girar una posición hacia atrás en el sentido horario
         *
         * @return la anterior posición posición en sentido horario
         */
        public Orientacion izquierda() {
            return vals[Math.floorMod((this.ordinal() - 1), vals.length)];
        }
    }
}
