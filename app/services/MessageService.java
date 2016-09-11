package services;

import akka.actor.Cancellable;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import data.Message;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.twirl.api.Html;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public interface MessageService {

    default Source<Html, ?> getMessageSource(String token, String channelId, WSClient wsClient, ObjectMapper objectMapper) throws ExecutionException, InterruptedException, IOException {
        final Source<String, Cancellable> tickSource = Source.tick(Duration.Zero(), Duration.create(5000, MILLISECONDS), "TICK");
        return tickSource.map((tick) -> {
            List<Message> messageList = getMessages(token, channelId, wsClient, objectMapper);
            return views.html.dashboard.post_list.render(messageList);
        });
    }

    default List<Message> getMessages(String token, String channelId, WSClient wsClient, ObjectMapper objectMapper) throws ExecutionException, InterruptedException, IOException {
        final WSRequest request1 = wsClient
                .url("https://slack.com/api/channels.history")
                .setQueryParameter("token", token)
                .setQueryParameter("channel", channelId);
        final CompletionStage<WSResponse> responsePromise1 = request1.get();
        final JsonNode json = responsePromise1.thenApply(WSResponse::asJson).toCompletableFuture().get().get("messages");
        return objectMapper.readValue(json.toString(), TypeFactory.defaultInstance().constructCollectionType(List.class, Message.class));
    }

}