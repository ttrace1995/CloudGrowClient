/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hydroponicgrowclient;

import com.microsoft.azure.sdk.iot.service.*;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author tylertracey
 */
public class MessageSender {
    
    private static final String connectionString = "HostName=CanonTestHub.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=WCUlILPQnuyqoJLFxL2tMNAl/Ro4h5ofMZgEiqSUQmU=";
    private static final String deviceId = "CloudGrow";
    private static final IotHubServiceClientProtocol protocol = IotHubServiceClientProtocol.AMQPS;
    
    public static void sendMessage(String message) throws IOException, URISyntaxException, Exception {
       
        ServiceClient serviceClient = ServiceClient.createFromConnectionString(connectionString, protocol);

        if (serviceClient != null) {
            serviceClient.open();
            FeedbackReceiver feedbackReceiver = serviceClient.getFeedbackReceiver();
            
            if (feedbackReceiver != null) {
                feedbackReceiver.open();
            }

            Message messageToSend = new Message(message);
            messageToSend.setDeliveryAcknowledgement(DeliveryAcknowledgement.Full);

            serviceClient.send(deviceId, messageToSend);
            System.out.println("Message sent to device");

            FeedbackBatch feedbackBatch = feedbackReceiver.receive(10000);
            if (feedbackBatch != null) {
                System.out.println("Message feedback received, feedback time: "
                    + feedbackBatch.getEnqueuedTimeUtc().toString());
            }

            if (feedbackReceiver != null) feedbackReceiver.close();
            serviceClient.close();
        }
    }
}
