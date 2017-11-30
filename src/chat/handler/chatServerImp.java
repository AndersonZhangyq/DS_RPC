package chat.handler;

import chat.service.ChatService;
import chat.struct.ChatMessage;
import chat.struct.messageStatus;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chatServerImp implements ChatService.Iface {
    private Map<String,List<ChatMessage>> messageDB = new HashMap<>();

    @Override
    public boolean hasUnreadMessage(String user_name) throws TException {
        if (!messageDB.containsKey(user_name))
            return false;
        else{
            System.out.println("hasUnreadMessage: ( " + user_name + " )");
            List<ChatMessage> messages = messageDB.get(user_name);
            return messages.get(messages.size() - 1).status == messageStatus.UNREAD;
        }
    }

    @Override
    public void receiveMessage(ChatMessage message) throws TException {
        System.out.println("receiveMessage: ( from: " + message._from
                + " to: "+ message._to
                + " content: " + message.content
                + " status: " + message.status + " )");
        String _to = message._to;
        if (messageDB.containsKey(_to)){
            messageDB.get(_to).add(message);
        }else{
            List<ChatMessage> messageList = new ArrayList<>();
            messageList.add(message);
            messageDB.put(_to, messageList);
        }
    }

    @Override
    public List<ChatMessage> sendUnreadMessage(String user_name) throws TException {
        System.out.println("sendUnreadMessage: ( " + user_name + " )");
        if (hasUnreadMessage(user_name)){
            List<ChatMessage> messageUnread = messageDB.get(user_name);
            int size = messageUnread.size(), i = size - 1;
            for(i = size - 1; i >= 0; i--){
                if (messageUnread.get(i).status == messageStatus.SENT_TO_USER)
                    break;
                else
                    messageUnread.get(i).status = messageStatus.SENT_TO_USER;
            }
            messageUnread = messageUnread.subList(i + 1,size);
            return messageUnread;
        }
        return null;
    }
}
