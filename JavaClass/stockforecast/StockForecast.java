/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stockforecast;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 *
 * @author Marc
 */
public class StockForecast {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GregorianCalendar start = new GregorianCalendar(1960, 4, 20);
        GregorianCalendar end = new GregorianCalendar();
        String stock = "MCD";
        StockDownloader loader = new StockDownloader(stock, start, end);
        ArrayList<Double> closes = loader.getCloses();
        ArrayList<Double> opens = loader.getOpens();
//        movingAverage_Follow_Strategie(closes, 200);
//        System.out.println(closes.get(201));
 
//        System.out.println("Maximum " + getMaximum(closes) + " Minumum " + getMinimum(closes) + " Today " + closes.get(0));
 
//        System.out.println("normiert " + getNormed(closes, 0));

        int timeSpanForTest = 1000;
        System.out.println("Gewinn " + stock + " letzten " + timeSpanForTest + "Tage = " + movingAverageTest(closes, timeSpanForTest));
        
//        writeNormedToFile(opens, closes);
  
//        int days = 15;
//        System.out.println("vorhersage " + movingAverageForecast(closes, days) + "Actual " + closes.get(0) + "differenz " 
//                + (movingAverageForecast(closes, days) - closes.get(0)));
    }
    
    /**
     * Nimmt die letzten angegebene anzahl von Tagen und macht darauf basierend
     * eine vorhersage anhand der kurzfristigen Kursentwicklung
     * @param closes
     * @param days
     * @return Den letzten Close wert + die kursentwicklung. Also die Vorhersage
     * für den nächsten Tag.
     */
    public static double movingAverageForecast(ArrayList<Double> closes, int days)
    {
        double tempAverage = 0;
        for(int i = days; i > 0; i--)
        {
            tempAverage += closes.get(i) - closes.get(i - 1);
        }
        double average = tempAverage / days;
        return closes.get(1) + average;
        
    }
    
    /**
     * Normiert die letzten 30 Tage der closes und opens, und gibt diese an
     * den FileWriter weiter der diese dann in einer bestimten Form in eine
     * Datei schreibt.
     * @param opens
     * @param closes 
     */
    public static void writeNormedToFile(ArrayList<Double> opens, ArrayList<Double> closes)
    {
        ArrayList<Double> opensNormed = new ArrayList<>();
        ArrayList<Double> closesNormed = new ArrayList<>();
        //30 steht für die Anzahl der Zeilen die in die Datei geschrieben werden
        for(int i = 0; i < 30; i++)
        {
            opensNormed.add(getNormed(opens, i));
            closesNormed.add(getNormed(closes, i));
        }
        
        FileWriter.writeToFile("C://Users//Marc M//Desktop//StockTest.txt", opensNormed, closesNormed);
        
    }
    /**
     * Testes den Einsatz von Geld in einer Aktie in einem bestimmten
     * Zeitraum, wenn MA50 größer wird als der MA200 dann wird die Aktie gekauft
     * wenn der MA50 unter den MA200 Fällt dann wird die Aktie verkauft.
     * UM das herau zu finden wird die methode movingAverage_Follow_Strategie
     * verwendet
     * @param list
     * @param timeSpan
     * @return gibt den gewinn zurück
     */
    public static double movingAverageTest(ArrayList<Double> list, int timeSpan)
    {
        double gewinn = 0;
        double gekauftFuer = 0;
        boolean aktuellGekauft = false;
        for(int i = timeSpan; i > 0; i--)
        {
            if(movingAverage_Follow_Strategie(list, i) && !aktuellGekauft)
            {
                aktuellGekauft = true;
                gekauftFuer = list.get(i);
                
            }
            else if(!movingAverage_Follow_Strategie(list, i) && aktuellGekauft)
            {
                aktuellGekauft = false;
                gewinn = gewinn - gekauftFuer + list.get(i);
            }
            else
            {
                if(aktuellGekauft)
                {    
                    System.out.println("aktuell In der Aktie");
                }
            }
            
        }
        if(aktuellGekauft)
        {
            gewinn = gewinn - gekauftFuer + list.get(1);
        }
        return gewinn;
    }
    
    /**
     * liefert True zurück wenn man die aktie kaufen sollte
     * @param list
     * @param currentDay
     * @return gibt true zurück wenn man die Aktie kaufen sollte
     */
    public static boolean movingAverage_Follow_Strategie(ArrayList<Double> list, int currentDay)
    {
        //200 Day average ausrechnen
        double tempAverage = 0;
        for (int i = currentDay; i < currentDay + 200; i++) {
            tempAverage += list.get(i);
        }
        double average = tempAverage / 200;

        //50 Day average ausrechnen
        double _tempAverage = 0;
        for (int i = currentDay; i < currentDay + 50; i++) {
            _tempAverage += list.get(i);
        }
        double _average = _tempAverage / 50;

        System.out.println("200 Day average = " + average + "\n" + "50 Day average = " + _average);
        if (average < _average) {
            System.out.println("KAUFEN");
            return true;
        } else {
            System.out.println("NICHT KAUFEN");
            return false;
        }
    }
    
    /**
     * Gibt die normierte Zahl zwischen 0 und 1 eines bestimmten Tages zurück
     * @param list
     * @param day
     * @return 
     */
    public static double getNormed(ArrayList<Double> list, int day)
    {
        return list.get(day) / getMaximum(list);
//        double ergebnisTest = (closes.get(0) + Math.abs(getMinimum(closes))) / getMaximum(closes);
    }
    
    
    public static double getMaximum(ArrayList<Double> list)
    {
        double max = Double.MIN_VALUE;
        for (Double list1 : list) {
            if (list1 > max) {
                max = list1;
            }
        }
        return max;
    }
    
    public static double getMinimum(ArrayList<Double> list)
    {
        double min = Double.MAX_VALUE;
        for (Double list1 : list) {
            if (list1 < min) {
                min = list1;
            }
        }
        return min;
    }
    
}
