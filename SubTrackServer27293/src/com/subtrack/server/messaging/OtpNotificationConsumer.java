package com.subtrack.server.messaging;

import com.subtrack.server.messaging.dto.OtpNotificationEvent;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class OtpNotificationConsumer {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "otp.queue";

    public void start() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(QUEUE_NAME);
        MessageConsumer consumer = session.createConsumer(destination);

        consumer.setMessageListener(message -> {
            try {
                if (message instanceof ObjectMessage) {
                    OtpNotificationEvent e = (OtpNotificationEvent) ((ObjectMessage) message).getObject();
                    System.out.println("[EMAIL-SIM] To: " + e.getEmail()
                            + " | User: " + e.getUsername()
                            + " | OTP: " + e.getOtp());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        System.out.println("OtpNotificationConsumer started. Listening on queue: " + QUEUE_NAME);
    }
}
