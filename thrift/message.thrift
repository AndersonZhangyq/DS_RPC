namespace java chat.struct

enum messageStatus{
    UNREAD = 1;
    SENT_TO_USER = 2;
}

struct ChatMessage{
    1: string _from,
    2: string _to,
    3: string content,
    4: i64 timestamp
    5: messageStatus status
}