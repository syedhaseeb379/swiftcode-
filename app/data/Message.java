package data;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {
    public String text;
    public FeedResponse feedResponse;
    public Sender sender;

    public enum Sender {USER, BOT}

    ;


}
