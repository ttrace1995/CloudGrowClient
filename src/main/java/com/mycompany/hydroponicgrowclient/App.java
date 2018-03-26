/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hydroponicgrowclient;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 *
 * @author ttracey
 */
public class App {
    
   
     
    public static void main(String args[]) throws IOException, Exception {
        
        EventListener evt = new EventListener();
        MessageSender.sendMessage("refresh:now");
        Interface gui = new Interface();
        gui.setVisible(true);
        ImageContainer.connectToContainer();
        
        
    }
}
