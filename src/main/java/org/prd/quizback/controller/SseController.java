package org.prd.quizback.controller;

import org.prd.quizback.model.dto.PromptRespDto;
import org.prd.quizback.service.RoomService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/sse")
public class SseController {

    private final RoomService roomService;

    public SseController(RoomService salaService) {
        this.roomService = salaService;
    }

    @GetMapping("/hellCheck")
    public String hellCheck(){
        return "SseController is working";
    }

    @GetMapping(value = "/{salaId}/stream")
    public Flux<ServerSentEvent<PromptRespDto>> streamRoomsEvents(@PathVariable String salaId) {
        //cuando se envia el mensaje se envia a todos los suscriptores y luego se debe eliminar el sink}
        return roomService.getRoomEvent(salaId);
    }

    @GetMapping(value = "/{salaId}/delete")
    public ResponseEntity<String> deleteRoom(@PathVariable String salaId) {
        roomService.deleteRoomEvent(salaId);
        return ResponseEntity.ok("Sala "+ salaId+" se elimino correctamente");
    }
}