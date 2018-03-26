/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hydroponicgrowclient;


import java.io.IOException;
import com.microsoft.azure.eventhubs.*;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.time.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author tylertracey
 */
public class EventListener {
    
    private static String connStr = "Endpoint=sb://iothub-ns-canontesth-313478-1517ef4a1f.servicebus.windows.net/;EntityPath=canontesthub;SharedAccessKeyName=iothubowner;SharedAccessKey=WCUlILPQnuyqoJLFxL2tMNAl/Ro4h5ofMZgEiqSUQmU=";
    
    public EventListener() {
        Thread t = new Thread(() -> {
            try {
                startListener();
            } catch (IOException ex) {
                Logger.getLogger(EventListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        t.start();   
    }
    
    public static void startListener() throws IOException {
        // Create receivers for partitions 2 and 3.
        EventHubClient client0 = receiveMessages("0");
        EventHubClient client1 = receiveMessages("1");
//        EventHubClient client2 = receiveMessages("2");
//        EventHubClient client3 = receiveMessages("3");
        System.in.read();
    }
    // Create a receiver on a partition.
    public static EventHubClient receiveMessages(final String partitionId) {
        EventHubClient client = null;
        try {
            client = EventHubClient.createFromConnectionStringSync(connStr);
        } catch (EventHubException | IOException e) {
            System.out.println("Failed to create client: " + e.getMessage());
            System.exit(1);
        }
        try {
            // Create a receiver using the
            // default Event Hubs consumer group
            // that listens for messages from now on.
            client.createReceiver(EventHubClient.DEFAULT_CONSUMER_GROUP_NAME, partitionId, Instant.now())
                .thenAccept((PartitionReceiver receiver) -> {
            System.out.println("** Created receiver on partition " + partitionId);
            try {
                while (true) {
                    Iterable<EventData> receivedEvents = receiver.receive(100).get();
                    int batchSize = 0;
                    if (receivedEvents != null) {
                        System.out.println("Got some events");
                        for (EventData receivedEvent : receivedEvents) {
                       
                            if (receivedEvent.getProperties().containsValue("state_data")) {
                                System.out.println(String.format("| Message Payload: %s", new String(receivedEvent.getBytes(), Charset.defaultCharset())));
                                FileReadWrite.writeJSONData(new String(receivedEvent.getBytes(), Charset.defaultCharset()), "states.json");
                                FileReadWrite.setStateColors();
                            }
                            else if (receivedEvent.getProperties().containsValue("temp_humid_data")) {
                                System.out.println(String.format("| Message Payload: %s", new String(receivedEvent.getBytes(), Charset.defaultCharset())));
                                FileReadWrite.writeJSONData(new String(receivedEvent.getBytes(), Charset.defaultCharset()), "data.json");
                                FileReadWrite.setHumidity();
                                FileReadWrite.setTemperature();
                            }
                        
                            
//                            System.out.println(String.format("Offset: %s, SeqNo: %s, EnqueueTime: %s",
//                                    receivedEvent.getSystemProperties().getOffset(),
//                                    receivedEvent.getSystemProperties().getSequenceNumber(),
//                                    receivedEvent.getSystemProperties().getEnqueuedTime()));
//                            System.out.println(String.format("| Device ID: %s",
//                                    receivedEvent.getSystemProperties().get("iothub-connection-device-id")));
//                            System.out.println(String.format("| Message Payload: %s",
//                                    new String(receivedEvent.getBytes(), Charset.defaultCharset())));
                            batchSize++;
                        }
                    }
                    System.out.println(String.format("Partition: %s, ReceivedBatch Size: %s", partitionId, batchSize));
                } 
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Failed to receive messages: " + e.getMessage());
            }   catch (IOException ex) {
                    Logger.getLogger(EventListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(EventListener.class.getName()).log(Level.SEVERE, null, ex);
                }});
            } catch (EventHubException e) {
                System.out.println("Failed to create receiver: " + e.getMessage());
        }
        return client;
    } 
}
