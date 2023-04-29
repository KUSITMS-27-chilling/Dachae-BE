package chilling.encore.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programIdx;

    @ManyToOne
    private LearningCenter learningCenter;

    private String programName;
    private String programContent;
    private String category;

    private LocalDate startDate;
    private LocalDate endDate;
}
