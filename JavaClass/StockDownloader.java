/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stockforecast;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.ArrayList;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 *
 * @author Marc
 */
public class StockDownloader {
    
    public static final int DATE = 0;
    public static final int OPEN = 1;
    public static final int HIGH = 2;
    public static final int LOW = 3;
    public static final int CLOSE = 4;
    public static final int VOLUME= 5;
    public static final int ADJCLOSE = 6;
    
    private ArrayList<GregorianCalendar> dates;
    private ArrayList<Double> opens;
    private ArrayList<Double> highs;
    private ArrayList<Double> lows;
    private ArrayList<Double> closes;
    private ArrayList<Integer> volumes;
    private ArrayList<Double> adjCloses;
    
    
    public StockDownloader(String symbol, GregorianCalendar start, GregorianCalendar end)
    {
        dates = new ArrayList<GregorianCalendar>();
        opens = new ArrayList<Double>();
        highs = new ArrayList<Double>();
        lows = new ArrayList<Double>();
        closes = new ArrayList<Double>();
        volumes = new ArrayList<Integer>();
        adjCloses = new ArrayList<Double>();
        
        
        //http://real-chart.finance.yahoo.com/table.csv?s=IBM&a=00&b=2&c=1962&d=04&e=21&f=2015&g=d&ignore=.csv
        String url = "http://real-chart.finance.yahoo.com/table.csv?s=" + symbol + 
                "&a=" + start.get(Calendar.MONTH) +
                "&b=" + start.get(Calendar.DAY_OF_MONTH) + 
                "&c=" + start.get(Calendar.YEAR) +
                "&d=" + end.get(Calendar.MONTH) + 
                "&e=" + end.get(Calendar.DAY_OF_MONTH) + 
                "&f=" + end.get(Calendar.YEAR) +
                "&g=d&ignore=.csv";
        
        try
        {
            URL yhoofin = new URL(url);
            URLConnection data = yhoofin.openConnection();
            Scanner input = new Scanner(data.getInputStream());
            if(input.hasNext()) // erste Linie überspringen, weil in der ersten nur die Header stehen
            {
                input.nextLine();
            }
            
            //Am index 0 der Arrays steht der neueste Wert. 
            while(input.hasNext())
            {
                String line = input.nextLine();
//                System.out.println(line);
                String [] parts = line.split(",");
                String [] dateArray = parts[0].split("-");
                GregorianCalendar date = new GregorianCalendar(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2]));
                dates.add(date);
                opens.add(Double.parseDouble(parts[1]));
                highs.add(Double.parseDouble(parts[2]));
                lows.add(Double.parseDouble(parts[3]));
                closes.add(Double.parseDouble(parts[4]));
                volumes.add(Integer.parseInt(parts[5]));
                adjCloses.add(Double.parseDouble(parts[6]));
                
            }
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }
    
    public ArrayList<GregorianCalendar> getDates()
    {
        return dates;
    }
    
    public ArrayList<Double> getOpens()
    {
        return opens;
    }
    
    public ArrayList<Double> getHighs()
    {
        return highs;
    }
    
    public ArrayList<Double> getLows()
    {
        return lows;
    }
    
    public ArrayList<Double> getCloses()
    {
        return closes;
    }
    
    public ArrayList<Integer> getVolumes()
    {
        return volumes;
    }
    
    public ArrayList<Double> getAdjCloses()
    {
        return adjCloses;
    }
}
