/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hydroponicgrowclient;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author tylertracey
 */
public class FileReadWrite {
    
    public static String DATA_PATH = "/Users/tylertracey/Documents/HydroponicGrowClient/src/main/java/com/mycompany/hydroponicgrowclient/settings/data.json";
    public static String STATE_PATH = "/Users/tylertracey/Documents/HydroponicGrowClient/src/main/java/com/mycompany/hydroponicgrowclient/settings/states.json";
    
    public static synchronized void writeJSONData(String json, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/tylertracey/Documents/HydroponicGrowClient/src/main/java/com/mycompany/hydroponicgrowclient/settings/" + filename));
        writer.write(json);
     
        writer.close();
    }
    
    public static Color getStateColor(String device) {
       
        if ("light".equals(device)) {
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(STATE_PATH));
                JSONObject jsonObject =  (JSONObject) obj;
                
                long lightState = (Long) jsonObject.get("lightState");
                
                if (lightState == 1) {
                    return Color.green;
                }
                else {
                    return Color.red;
                }
            } catch (IOException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        if ("fan".equals(device)) {
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(STATE_PATH));
                JSONObject jsonObject =  (JSONObject) obj;
                
                long fanState = (Long) jsonObject.get("fanState");
                
                if (fanState == 1) {
                    return Color.green;
                }
                else {
                    return Color.red;
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        if ("pump".equals(device)) {
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(STATE_PATH));
                JSONObject jsonObject =  (JSONObject) obj;
                
                long pumpState = (Long) jsonObject.get("pumpState");
                
                if (pumpState == 1) {
                    return Color.green;
                }
                else {
                    return Color.red;
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return null;
        
    }
    
    
    public static void setStateColors() {
       
        
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(STATE_PATH));
                JSONObject jsonObject =  (JSONObject) obj;
                
                long lightState = (Long) jsonObject.get("lightState");
                long fanState = (Long) jsonObject.get("fanState");
                long pumpState = (Long) jsonObject.get("pumpState");
         
                
                if (lightState == 1) {
                    Interface.lightButton.setBackground(Color.green);
                }
                else {
                    Interface.lightButton.setBackground(Color.red);
                }
                
                if (fanState == 1) {
                    Interface.fanButton.setBackground(Color.green);
                }
                else {
                    Interface.fanButton.setBackground(Color.red);
                }
                
                if (pumpState == 1) {
                    Interface.pumpButton.setBackground(Color.green);
                }
                else {
                    Interface.pumpButton.setBackground(Color.red);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    
    public static long getLightState() {
        try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(STATE_PATH));
                JSONObject jsonObject =  (JSONObject) obj;
                
                return (Long) jsonObject.get("lightState");
                
                
            } catch (IOException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return 0;
    }
    
    public static long getFanState() {
        try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(STATE_PATH));
                JSONObject jsonObject =  (JSONObject) obj;
                
                return (Long) jsonObject.get("fanState");
                
                
            } catch (IOException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return 0;
    }
    
    public static long getPumpState() {
        try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(STATE_PATH));
                JSONObject jsonObject =  (JSONObject) obj;
                
                return (Long) jsonObject.get("pumpState");
                
                
            } catch (IOException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(FileReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return 0;
    }
        
        
        
    
    
    public static void setHumidity() throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        
        Object obj = parser.parse(new FileReader(DATA_PATH));
        JSONObject jsonObject =  (JSONObject) obj;
        double humidity = (Double) jsonObject.get("humidity");
        
        Interface.humidityValue.setText(String.valueOf(humidity) + " %");
        
    }
    
    public static void setTemperature() throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
 
        Object obj = parser.parse(new FileReader(DATA_PATH));
        JSONObject jsonObject =  (JSONObject) obj;

        double temperature = (Double) jsonObject.get("temperature");
        Interface.temperatureValue.setText(String.valueOf(temperature) + " F");
    }
    
}
