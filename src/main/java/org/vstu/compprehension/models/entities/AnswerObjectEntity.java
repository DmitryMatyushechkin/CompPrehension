package org.vstu.compprehension.models.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(
    name = "AnswerObject",
    uniqueConstraints = {
        @UniqueConstraint(
            name="uk_answerId__questionId",
            columnNames = {"answer_id", "question_id"}
        )
    }
)
public class AnswerObjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer_id", nullable = false)
    private Integer answerId;

    @Column(name = "hyperText")
    private String hyperText;

    @Column(name = "domainInfo")
    private String domainInfo;

    @Column(name = "isRightCol")
    private boolean isRightCol;

    @Column(name = "concept")
    private String concept;


    @OneToMany(mappedBy = "leftAnswerObject", fetch = FetchType.LAZY)
    private List<ResponseEntity> responsesLeft;

    @OneToMany(mappedBy = "rightAnswerObject", fetch = FetchType.LAZY)
    private List<ResponseEntity> responsesRight;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private QuestionEntity question;
    
    
}
