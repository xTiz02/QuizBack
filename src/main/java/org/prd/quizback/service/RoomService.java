package org.prd.quizback.service;

import org.prd.quizback.model.dto.PromptRespDto;
import org.slf4j.Logger;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RoomService {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(RoomService.class);
    private final Map<String, Sinks.Many<PromptRespDto>> rooms = new ConcurrentHashMap<>();

    public void publicEvent(String salaId, PromptRespDto mensaje) {
        //Crea un nuevo Sinks.Many si no existe para la sala y no guarda los mensajes, solo los envía a los suscriptores.
        Sinks.Many<PromptRespDto> sink = rooms.computeIfAbsent(salaId, id -> Sinks.many().multicast().onBackpressureBuffer());
        //multicast envia el mensaje a todos los suscriptores
        //unicast envia el mensaje al ultimo suscriptor
        //reply se envia el mensaje al ultimo suscriptor y se guarda el mensaje
        //onBackpressureBuffer se guarda el mensaje y se envia a los suscriptores en el orden que llegaron los mensajes
        //el me
        //onBackpressureDrop se guarda el mensaje y se envia a los suscriptores, si hay mas mensajes se descartan y no se envian a los suscriptores restantes
        //onBackpressureLatest se guarda el mensaje y se envia a los suscriptores, si hay mas mensajes se descartan y se envia el ultimo mensaje
        //onBackpressureError se guarda el mensaje y se envia a los suscriptores, si hay mas mensajes se descartan y se envia un error al suscriptor que envio el mensaje
        //direAllOrNothing se envia el mensaje a todos los suscriptores o a ninguno si hay un error
        //direBestEffort se envia el mensaje a todos los suscriptores y si hay un error se envia a los suscriptores que no recibieron el mensaje
        Sinks.EmitResult result = sink.tryEmitNext(mensaje);//Envia el mensaje a todos los suscriptores
        System.out.println(result);
        logger.info("Actualmente se enviaran mensajes a " + sink.currentSubscriberCount() + " suscriptores de la sala "+salaId);
        /*sink.tryEmitComplete();
        rooms.remove(salaId);*/
    }

    public Flux<ServerSentEvent<PromptRespDto>> getRoomEvent(String roomId) {
        Sinks.Many<PromptRespDto> sink = rooms.computeIfAbsent(roomId, id -> Sinks.many().multicast().onBackpressureBuffer());
        return sink.asFlux()
                .doOnSubscribe(subscription -> {
                    /*countConnections.putIfAbsent(roomId, new AtomicInteger(0));//
                    int conexiones = countConnections.get(roomId).incrementAndGet();*/
                    logger.info("Cliente conectado a sala " + roomId);
                })
                .doOnCancel(() -> {
                    /*countConnections.get(roomId).decrementAndGet();*/
                    logger.error("OnCancel Cliente desconectado de sala" + roomId);
                    checkAndDeleteRoom(roomId);
                })
                .doOnDiscard(String.class, data -> { //Se ejecuta cuando se descarta un evento
                    logger.error("OnDiscard Se descartó un evento de la sala " + roomId);
                })
                .doOnComplete(() -> {
                    //esto se ejecuta cuando se completa la conexión a la sala y se envia el evento complete al cliente que se conecto a la sala, para que cierre la conexión
                    logger.info("OnComplete Se completó la conexión a la sala (para cada)" + roomId);
                })
                .map(data -> {
                    logger.info("Enviando evento a sala " + roomId);
                    return ServerSentEvent.builder(data)
                            .id(String.valueOf(data.hashCode()))
                            .event("room")
                            .build();
                });
    }

    private void checkAndDeleteRoom(String roomId) {
        Sinks.Many<PromptRespDto> sink = rooms.get(roomId);
        logger.info("Verificando si hay suscriptores en la sala:" + sink.currentSubscriberCount());
        if (sink.currentSubscriberCount() - 1 == 0) {
            logger.info("No hay más suscriptores en la sala " + roomId + ", eliminando evento.");
            deleteRoomEvent(roomId);
        }
    }



    public void deleteRoomEvent(String roomId) {
        Sinks.Many<PromptRespDto> sink = rooms.get(roomId);
        if (sink != null) {
            sink.tryEmitComplete();
            rooms.remove(roomId);
            logger.info("Sala " + roomId + " eliminada.");
        }
    }


}