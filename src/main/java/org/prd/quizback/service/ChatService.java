package org.prd.quizback.service;

import org.prd.quizback.model.dto.PromptRespDto;
import org.prd.quizback.model.dto.QuizRequestDto;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private String template = """
            Genera una lista de {num} preguntas de tipo quiz en español. Las preguntas deben ser
            de dificultad {difficult} y estar relacionadas con algún tema general de {categories}.
            Usa las siguientes subcategorías para agregar contexto adicional a cada pregunta y enfocarla en
            aspectos específicos: {subCategories}.
            Cada pregunta debe ser formulada claramente y debe estar acompañada de máximo 4 opciones de respuesta y solo habrá una respuesta correcta.
            Alterna entre preguntas de verdadero/falso y preguntas que tengan una opciones cortas, como una palabra o un numero, o opciones largas como una definición.
            Ademas, cada opción de respuesta puede tener máximo 30 caracteres. Aquí tienes un ejemplo para inspirarte:
            Categoría: Tecnología
            Subcategoría: Java
            Pregunta ejemplo: ¿Cuál de las siguientes opciones NO es un tipo de dato primitivo en Java?
            Opciones:
            String
            int
            boolean
            double
            Asegúrate de que cada pregunta sea comprensible, relevante y esté basada en conocimientos actuales.
            Finalmente el formato de salida debe ser el siguiente y remueve el ```json outer brackets.
            {format}
            """;

    private final VertexAiGeminiChatModel chatModel;

    public ChatService(VertexAiGeminiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public PromptRespDto chat(QuizRequestDto quizRequestDto){
        //Convertir la respuesta a una lista de QuestionDto
        System.out.println(quizRequestDto);
        BeanOutputConverter<PromptRespDto> outputConvert = new BeanOutputConverter<>(PromptRespDto.class);
        String format = outputConvert.getFormat();
        String categories = String.join(",", quizRequestDto.categories());
        String subCategories = String.join(",", quizRequestDto.subcategories());
        PromptTemplate promptTemplate = new PromptTemplate(template, Map.of("num", quizRequestDto.numQuestions(),
                "difficult", "medio",
                "categories", categories,
                "subCategories", subCategories,
                "format", format));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        Generation generation = this.chatModel.call(prompt).getResult();

        return outputConvert.convert(generation.getOutput().getContent());
    }
}

/*
* BeanOutputConverter<ActorsFilmsRecord> outputConvert = new BeanOutputConverter<>(ActorsFilmsRecord.class);

		String format = outputConvert.getFormat();
		String template = """
				Generate the filmography of 5 movies for Tom Hanks.
				Remove the ```json outer brackets.
				{format}
				""";
		PromptTemplate promptTemplate = new PromptTemplate(template, Map.of("format", format));
		Prompt prompt = new Prompt(promptTemplate.createMessage());
		Generation generation = this.chatModel.call(prompt).getResult();

		ActorsFilmsRecord actorsFilms = outputConvert.convert(generation.getOutput().getContent());
		assertThat(actorsFilms.actor()).isEqualTo("Tom Hanks");
		assertThat(actorsFilms.movies()).hasSize(5);*/