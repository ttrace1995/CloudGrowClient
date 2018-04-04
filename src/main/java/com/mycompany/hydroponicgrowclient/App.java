/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hydroponicgrowclient;


import java.io.IOException;


/**
 *
 * @author ttracey
 */
public class App {
    
   
     
    public static void main(String args[]) throws IOException, Exception {
        
        Interface gui = new Interface();
        
        EventListener evt = new EventListener();
        MessageSender.sendMessage("refresh:now");
        
        ImageContainer.connectToContainer();
        gui.setVisible(true);
        
        
    }
}
