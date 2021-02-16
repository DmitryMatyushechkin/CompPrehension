package com.example.demo.models.businesslogic;

import com.example.demo.models.businesslogic.backend.Backend;
import com.example.demo.models.businesslogic.backend.JenaBackend;
import com.example.demo.models.businesslogic.domains.ProgrammingLanguageExpressionDomain;
import com.example.demo.models.entities.BackendFactEntity;
import com.example.demo.models.entities.EnumData.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProgrammingLanguageExpressionDomainTest {

    @Autowired
    ProgrammingLanguageExpressionDomain domain;

    @Test
    public void testName() {
        assertEquals(domain.getName(), "ProgrammingLanguageExpressionDomain");
    }

    @Test
    public void testLaws() {
        assertNotNull(domain.getPositiveLaw("single_token_binary_execution"));
        assertTrue(domain.getPositiveLaw("single_token_binary_execution").isPositiveLaw());
    }

    @Test
    public void testQuestionGeneration() throws Exception {
        QuestionRequest qr = new QuestionRequest();
        qr.setTargetConcepts(List.of(
                domain.getConcept("precedence")
        ));
        qr.setAllowedConcepts(List.of(
                domain.getConcept("operator_binary_+"),
                domain.getConcept("operator_binary_*")
        ));
        qr.setDeniedConcepts(List.of(
                domain.getConcept("associativity")
        ));
        assertEquals("a == b < c", domain.makeQuestion(qr, Language.ENGLISH).getQuestionText().getText());

        QuestionRequest qr2 = new QuestionRequest();
        qr2.setTargetConcepts(List.of(
                domain.getConcept("associativity")
        ));
        qr2.setAllowedConcepts(List.of(
                domain.getConcept("operator_binary_+")
        ));
        qr2.setDeniedConcepts(List.of(
                domain.getConcept("precedence")
        ));
        assertEquals("a + b + c", domain.makeQuestion(qr2, Language.ENGLISH).getQuestionText().getText());

        QuestionRequest qr3 = new QuestionRequest();
        qr3.setTargetConcepts(List.of(
                domain.getConcept("associativity"),
                domain.getConcept("precedence")
        ));
        qr3.setAllowedConcepts(List.of(
                domain.getConcept("operator_binary_*"),
                domain.getConcept("operator_binary_+")
        ));
        qr3.setDeniedConcepts(List.of(

        ));
        assertEquals("a + b + c * d", domain.makeQuestion(qr3, Language.ENGLISH).getQuestionText().getText());

        QuestionRequest qr4 = new QuestionRequest();
        qr4.setTargetConcepts(List.of());
        qr4.setAllowedConcepts(List.of());
        qr4.setDeniedConcepts(List.of());
        assertEquals("Choose associativity of operator binary +",
                domain.makeQuestion(qr4, Language.ENGLISH).getQuestionText().getText());
    }

    @Test
    public void testQuestionSolve() throws Exception {
        QuestionRequest qr = new QuestionRequest();
        qr.setTargetConcepts(List.of(
                domain.getConcept("precedence")
        ));
        qr.setAllowedConcepts(List.of(
                domain.getConcept("operator_binary_+"),
                domain.getConcept("operator_binary_*")
        ));
        qr.setDeniedConcepts(List.of(
                domain.getConcept("associativity")
        ));
        Question question = domain.makeQuestion(qr, Language.ENGLISH);
        assertEquals("a == b < c", question.getQuestionText().getText());

        List<Tag> tags = new ArrayList<>();
        for (String tagString : List.of("basics", "operators", "order", "evaluation", "C++")) {
            Tag tag = new Tag();
            tag.setName(tagString);
            tags.add(tag);
        }

        Backend backend = new JenaBackend();
        List<BackendFactEntity> solution = backend.solve(
                domain.getQuestionLaws(question.getQuestionDomainType(), tags),
                question.getStatementFacts(),
                domain.getSolutionVerbs(question.getQuestionDomainType(), new ArrayList<>()));
        assertFalse(solution.isEmpty());
    }
}