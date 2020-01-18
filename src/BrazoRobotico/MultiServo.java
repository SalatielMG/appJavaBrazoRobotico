/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BrazoRobotico;

/**
 *
 * @author alan_
 */
import gnu.io.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiServo {

    Enumeration listaPuertos = CommPortIdentifier.getPortIdentifiers();
    CommPortIdentifier puertoId;
    PrintStream arduinoOut;
    SerialPort puerto;

    MultiServo(String pto) {
        while (listaPuertos.hasMoreElements()) {
            puertoId = (CommPortIdentifier) listaPuertos.nextElement();
            if (puertoId.getName().equals(pto)) {
                break;
            }
        }
        try {
            puerto = (SerialPort) puertoId.open("Serial", 1000);
            puerto.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            arduinoOut = new PrintStream(puerto.getOutputStream());
        } catch (PortInUseException | IOException | UnsupportedCommOperationException ex) {
            Logger.getLogger(MultiServo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviar(int num) {
        arduinoOut.write(num);
    }
}
