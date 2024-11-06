package org.prd.quizback.controller;

import org.prd.quizback.model.dto.PromptRespDto;
import org.prd.quizback.model.dto.QuizRequestDto;
import org.prd.quizback.service.ChatService;
import org.prd.quizback.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;
    private final RoomService roomService;

    public ChatController(ChatService chatService , RoomService salaService) {
        this.chatService = chatService;
        this.roomService = salaService;
    }

    @GetMapping("/hellCheck")
    public String hellCheck(){
        return "ChatController is working";
    }

    @PostMapping("/{salaId}/send")
    public ResponseEntity<String> generateQuestions(
            @PathVariable(name = "salaId") String salaId,@RequestBody QuizRequestDto quizRequestDto
    ){
        try{
            PromptRespDto list = chatService.chat(quizRequestDto);
            roomService.publicEvent(salaId, list);
             return ResponseEntity.ok("Se generaron correctamente las preguntas");
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }


}