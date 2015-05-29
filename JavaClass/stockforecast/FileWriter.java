/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockforecast;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marc M
 */
public class FileWriter {

    public static void writeToFile(String filePath, ArrayList<Double> openArray, ArrayList<Double> closeArray) {
        String firstLine = "3";
        String secondLine = "30";

        try {
            PrintWriter writer = new PrintWriter(filePath);
            writer.println(firstLine);
            writer.println(secondLine);
            for (int i = 29; i >= 0; i--) {
                String line = "1.0," + openArray.get(i) + "," + closeArray.get(i) + ",-0.99";
                writer.println(line);
            }
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
