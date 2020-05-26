import com.ibm.mq.jms.MQQueueConnectionFactory;

import javax.jms.*;

public class JMSClass {
    public static void main(String[] args) throws JMSException {
        // Пишем сообщение в очередь MQ.IN
        sendMessage("MQ.IN", "Наше первое сообщение");
        // Читаем сообщение из очередь MQ.IN
        String message = receiveMessage("MQ.IN");
        // Пишем прочитанное сообщение из очередь MQ.IN в очередь MQ.OUT
        sendMessage("MQ.OUT", message);
        // Читаем сообщение из очереди MQ.OUT
        receiveMessage("MQ.OUT");
    }
    static void sendMessage(String mQName, String mQValue) throws JMSException {
        QueueConnectionFactory connFactory = new MQQueueConnectionFactory();
        QueueConnection conn = connFactory.createQueueConnection();
        QueueSession session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue q = session.createQueue(mQName);
        QueueSender sender = session.createSender(q);
        TextMessage msg = session.createTextMessage();
        msg.setText(mQValue);
        System.out.println("Sending the message: "+msg.getText());
        sender.send(msg);
        session.close();
        conn.close();
    }
    static String receiveMessage(String mQName) throws JMSException {
        QueueConnectionFactory connFactory = new MQQueueConnectionFactory();
        QueueConnection conn = connFactory.createQueueConnection();
        QueueSession session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue q = session.createQueue(mQName);
        QueueReceiver receiver = session.createReceiver(q);
        conn.start();
        Message m = receiver.receive();
        if(m instanceof TextMessage) {
            TextMessage txt = (TextMessage) m;
            System.out.println("Message Received: "+txt.getText());
            session.close();
            conn.close();
            return txt.getText();
        }
        return null;
    }
}
