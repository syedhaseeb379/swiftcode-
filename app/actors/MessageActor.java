package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.FeedResponse;
import data.Message;
import data.NewsAgentResponse;
import services.FeedService;
import services.NewsAgentService;

import java.util.UUID;

import static data.Message.Sender.BOT;
import static data.Message.Sender.USER;

public class MessageActor extends UntypedActor {
    //Self-Reference the actor
    //PROPS
    //Object of Feed Service
    //Object of NewsAgentService
    //Define another actor Reference
    public static Props props(ActorRef out) {
        return Props.create(MessageActor.class, out);
    }

    private final ActorRef out;

    public MessageActor(ActorRef out) {
        this.out = out;
    }

    public FeedService feedService = new FeedService();
    private NewsAgentService newsAgentService = new NewsAgentService();

    @Override
    public void onReceive(Object message) throws Throwable {
        //Send back the response
        ObjectMapper objectMapper = new ObjectMapper();
        Message messageObject = new Message();
        if (message instanceof String) {

            messageObject.text = (String) message;
            messageObject.sender = USER;
            out.tell(objectMapper.writeValueAsString(messageObject), self());
            String query = newsAgentService.getNewsAgentResponse("Find" + message, UUID.randomUUID()).query;
            FeedResponse feedResponse = feedService.getFeedByQuery(query);
            messageObject.text = (feedResponse.title == null) ? "No results found" : "Showing results for: " + query;
            messageObject.feedResponse = feedResponse;
            messageObject.sender = BOT;
            out.tell(objectMapper.writeValueAsString(messageObject), self());
        }

    }
}