package BrazoRobotico;

import java.io.IOException;

public class main {
    
    static final public int BASE = 1;
    static final public int BRAZODERECHO = 2;
    static final public int BRAZOIZQUIERDO = 3;
    static final public int PINZA = 4;
    static final public MultiServo conexion = new MultiServo("COM3");
    
    public static void main(String[] args) throws IOException {
        //new Interfaz();
        //new CurvedSliderTest();
        new InterfazPersonzalizada();
    }

}
