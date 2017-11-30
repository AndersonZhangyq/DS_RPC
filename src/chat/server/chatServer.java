package chat.server;


import chat.handler.chatServerImp;
import chat.service.ChatService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class chatServer{
    private static ChatService.Processor processor;
    private static chatServerImp handler;


    public static void main(String[] args){
        handler = new chatServerImp();
        processor = new ChatService.Processor(handler);
        Runnable simple = new Runnable() {
            @Override
            public void run() {
                simple(processor);
            }
        };
        new Thread(simple).start();
    }

    private static void simple(ChatService.Processor handler){
        try {
            TServerTransport tServerTransport = new TServerSocket(9090);
            TServer tServer = new TThreadPoolServer(new TThreadPoolServer.Args(tServerTransport)
                    .processor(handler));

            // Easy for debug
            //TServer tServer = new TSimpleServer(new TServer.Args(tServerTransport).processor(handler));

            System.out.println("Starting thread pool server......");
            tServer.serve();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
