package com.subtrack.server.messaging;

import com.subtrack.server.messaging.dto.OtpNotificationEvent;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

public class OtpEventPublisher {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "subtrack.otp.notification";

    public void publish(OtpNotificationEvent event) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);

        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;

        try {
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(QUEUE_NAME);

            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            ObjectMessage msg = session.createObjectMessage(event);
            producer.send(msg);

        } finally {
            if (producer != null) {
                try { producer.close(); } catch (Exception ignored) {}
            }
            if (session != null) {
                try { session.close(); } catch (Exception ignored) {}
            }
            if (connection != null) {
                try { connection.close(); } catch (Exception ignored) {}
            }
        }
    }
}
