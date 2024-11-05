package org.prd.quizback.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PromptRespDto(
        @JsonProperty(required = true, value = "questions_list") List<QuestionDataDto> questionsList
) {
    public record QuestionDataDto(
            @JsonProperty(required = true, value = "uuid") String uuid,
            @JsonProperty(required = true, value = "question") String question,
            @JsonProperty(required = true, value = "options") List<QuestionOption> options
    ) {
        record QuestionOption(
                @JsonProperty(required = true, value = "option") String option,
                @JsonProperty(required = true, value = "correct") boolean correct
        ) { }
    }

}