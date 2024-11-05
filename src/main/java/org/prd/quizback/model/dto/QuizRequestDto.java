package org.prd.quizback.model.dto;

import java.util.List;

public record QuizRequestDto(
        Integer numQuestions,
        List<String> categories,
        List<String> subcategories
        // String difficult,
) {
}