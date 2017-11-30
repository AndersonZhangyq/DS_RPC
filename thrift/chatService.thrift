include "message.thrift"

namespace java chat.service

service ChatService{
    bool hasUnreadMessage(1: string user_name),
    oneway void receiveMessage(1: message.ChatMessage message),
    list<message.ChatMessage> sendUnreadMessage(1: string user_name)
}