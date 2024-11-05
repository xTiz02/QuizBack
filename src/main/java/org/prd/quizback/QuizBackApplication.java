package org.prd.quizback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.LocalTime;


@SpringBootApplication
public class QuizBackApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(QuizBackApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(QuizBackApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /*WebClient client = WebClient.create("http://localhost:8085/sse");
        ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<ServerSentEvent<String>>() {};

        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/0358675001/stream")
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> logger.info("USER 1 Time: {} - event: name[{}], id [{}], content[{}] ",
                        LocalTime.now(), content.event(), content.id(), content.data()),
                error -> logger.error("Error receiving SSE: {}", error),
                () -> logger.info("Completed!!!"));


        WebClient client2 = WebClient.create("http://localhost:8085/sse");
        ParameterizedTypeReference<ServerSentEvent<String>> type2
                = new ParameterizedTypeReference<ServerSentEvent<String>>() {};

        Flux<ServerSentEvent<String>> eventStream2 = client2.get()
                .uri("/0358675001/stream")
                .retrieve()
                .bodyToFlux(type2);

        Disposable a = eventStream2.subscribe(
                content -> {
                    logger.info("USER 2 Time: {} - event: name[{}], id [{}], content[{}] ",
                        LocalTime.now(), content.event(), content.id(), content.data());
                    //cerrar la conexión después de recibir 1 mensaje

                    },
                error -> logger.error("Error receiving SSE: {}", error),
                () -> logger.info("Completed!!!"));*/



    }
}