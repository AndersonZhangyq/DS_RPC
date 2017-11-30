package chat.demo;

import chat.service.ChatService;
import chat.struct.ChatMessage;
import chat.struct.messageStatus;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.LongUnaryOperator;

public class chatClient {
    private static final int SEND_MESSAGE = 1,
            GET_UNREAD_MESSAGE = 2,
            LOGOUT = 3;

    public static void main(String[] args) {
        try {
            TTransport transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol tProtocol = new TBinaryProtocol(transport);
            ChatService.Client remote_service = new ChatService.Client(tProtocol);

            Scanner get = new Scanner(System.in);

            System.out.println("You have login, enter your username: ");
            String userName = get.next();
            System.out.println("Hello " + userName);
            boolean logOut = false;

            // Automatic check if there is a new message unread.
            // Abandoned due to thrift debug point polluting the output
            // https://issues.apache.org/jira/browse/THRIFT-4062
            // Waiting for thrift-0.11.0
            /*ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
            service.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (remote_service.hasUnreadMessage(userName)) {
                            List<ChatMessage> unreadMessages = remote_service.sendUnreadMessage(userName);
                            // showUnreadMessage
                            for (ChatMessage message : unreadMessages) {
                                System.out.println("Message from: " + message._from + " "
                                        + new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date(message.timestamp)));
                                System.out.println(message.content);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 500, TimeUnit.MILLISECONDS);*/

            while (true) {
                System.out.println("Tell me what you want to do:");
                System.out.println(SEND_MESSAGE + ": Send a message");
                System.out.println(GET_UNREAD_MESSAGE + ": Get unread message");
                System.out.println(LOGOUT + ": Logout");
                int opCode = get.nextInt();
                switch (opCode) {
                    case SEND_MESSAGE:
                        System.out.println("Message to: ");
                        String _to = get.next();
                        System.out.println("Message content: ");
                        String content = get.next();
                        remote_service.receiveMessage(new ChatMessage(userName, _to, content, new Date().getTime(), messageStatus.UNREAD));
                        break;
                    case GET_UNREAD_MESSAGE:
                        try {
                            if (remote_service.hasUnreadMessage(userName)) {
                                List<ChatMessage> unreadMessages = remote_service.sendUnreadMessage(userName);
                                // showUnreadMessage
                                for (ChatMessage message : unreadMessages) {
                                    System.out.println("Message from: " + message._from + " "
                                            + new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date(message.timestamp)));
                                    System.out.println(message.content);
                                }
                            } else {
                                System.out.println("No unread messages!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case LOGOUT:
                        logOut = true;
                        break;
                }
                if (logOut) {
                    System.out.println("You have logged out.Exiting the client now......");
                    break;
                }
            }
            transport.close();

            //service.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
