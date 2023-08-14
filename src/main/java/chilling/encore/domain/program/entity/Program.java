package chilling.encore.domain.program.entity;

import chilling.encore.domain.LearningCenter.entity.LearningCenter;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learningCenterIdx", nullable = false)
    private LearningCenter learningCenter;
    @Column(nullable = false)
    private String programName;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
}
