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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 *
 * @author tylertracey
 */
public class ImageContainer {
    
     public static final String subscriptionKey = "efa2d0a4d88745b69833a70e4814904b";
    public static final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0/analyze";
    
    private static File downloadedFile;
    
    private static String accountName = "canonfilestorage";
    private static String accountKey = "AyY9OXK+Ebdt6cuGye/VSUiSY8Uguh52Xmv6S5LKB7l+T9kiR8iu+MU3+DlRLvrOaUUHHWQnIAGl7lhI/taDcg==";
    private static String containerName = "imagetestcontainer";
    private static String accountConnectionString;
    
    private static CloudStorageAccount storageAccount;
    private static CloudBlobClient blobClient = null;
    private static CloudBlobContainer container = null;
    
    private static String error_message = null;
    
    public static boolean makeConnectionWithContainer() {
        
//        accountName = GUI.accountNameValue.getText();
//        accountKey = GUI.accountKeyValue.getText();
//        containerName = GUI.containerNameValue.getText();
        
        accountConnectionString = 
            "DefaultEndpointsProtocol=https;" +
	    "AccountName="+accountName+";" +
	    "AccountKey="+accountKey+";";
        
        try {
            
            storageAccount = CloudStorageAccount.parse(accountConnectionString);
	    blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(containerName);
            
        } catch (Exception ex) {
            //Interface.debugTextArea.append(ex.getMessage()+" Make sure your credentials are correct.\n");
            storageAccount = null;
            blobClient = null;
            container = null;
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            
        } finally {
            try {
                if (container.listBlobs().iterator().hasNext()) {
                    //Interface.debugTextArea.append("Connection Successful!\n");
                    return true;
                }
                else {
                    //Interface.debugTextArea.append("The container you are trying to connect to is empty...\n");
                }
            }
            catch (Exception ex) {
               //Interface.debugTextArea.append(ex.getMessage()+"\n");
               return false;
            }
        }
        return false;
    }
    
    public static void populateMainPage() {
//        Interface.viewAccountNameValue.setText(accountName);
//        Interface.viewAccountNameValue.setForeground(Color.blue);
//        Interface.viewContainerNameValue.setText(containerName);
//        Interface.viewContainerNameValue.setForeground(Color.blue);

        for (ListBlobItem blobItem : container.listBlobs()) {
            Interface.imageFolderSelector.addItem(directorySplit(blobItem.getUri().getPath()));
        }
    }
    
    public static void clearMainPage() {
        
//        Interface.viewAccountNameValue.setText("Disconnected");
//        Interface.viewAccountNameValue.setForeground(Color.red);
//        Interface.viewContainerNameValue.setText("Disconnected");
//        Interface.viewContainerNameValue.setForeground(Color.red);
     
        Interface.imageFolderSelector.removeAllItems();
        Interface.viewFileSelectorValues.removeAllItems();
        
    }
    
    public static void populateImagesWithinFolder() {
        Interface.viewFileSelectorValues.removeAllItems();
        String dir = Interface.imageFolderSelector.getItemAt(Interface.imageFolderSelector.getSelectedIndex())+"/";
        String prefix = Interface.viewFilterPrefixValue.getText();
        try {
            CloudBlobDirectory directory = container.getDirectoryReference(dir);
            Iterable<ListBlobItem> blobItems = directory.listBlobs(prefix);
            
            for(ListBlobItem item : blobItems){
                CloudBlob blob = (CloudBlob)item;
                Interface.viewFileSelectorValues.addItem(directorySplit(blob.getName())); 
            } 
        } catch (Exception ex) {
            
        }
    }
    
    public static void downloadSelectedFile() {
        
        deleteOldFiles();
        
        String file = Interface.viewFileSelectorValues.getItemAt(Interface.viewFileSelectorValues.getSelectedIndex());
        String folder = Interface.imageFolderSelector.getItemAt(Interface.imageFolderSelector.getSelectedIndex());
        String path = folder +"/"+ file;
        
        try {
            
            CloudBlockBlob blob = container.getBlockBlobReference(path);
            downloadedFile = new File(System.getProperty("user.dir"), "downloadedFile.png");
            
            blob.downloadToFile(downloadedFile.getAbsolutePath());
            String imageURI = blob.getUri().toString();
            getImageRecognitionProperties(imageURI);
            
            
        } catch (URISyntaxException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StorageException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void getImageRecognitionProperties(String imageuri) {
        
        HttpClient httpclient = new DefaultHttpClient();

        try
        {
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("visualFeatures", "Categories,Description,Color");
            builder.setParameter("language", "en");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            StringEntity reqEntity = new StringEntity("{\"url\":\""+imageuri+"\"}");
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                System.out.println("REST Response:\n");
                System.out.println(json.toString(2));
                
            }
        }
        catch (Exception e)
        {
            // Display error message.
            System.out.println(e.getMessage());
        }
        
    }
    
    public static void displayFile() {
        try {
            BufferedImage myImage = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "downloadedFile.png"));
            ImageIcon myIcon = new ImageIcon(myImage);
            Interface.imageArea.setIcon(myIcon);
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void deleteOldFiles() {    
        if(downloadedFile != null) {
            downloadedFile.delete();
        }
        
    }
    
    public static void connectToContainer() {
        clearMainPage();
        boolean isConnected = makeConnectionWithContainer();
        if (isConnected) {
            populateMainPage();
        }
        else {
            clearMainPage();
        } 
    }
    
     public static String directorySplit(String dir) {
        String[] split = dir.split("/");
        return split[split.length-1];
    }
    
}
