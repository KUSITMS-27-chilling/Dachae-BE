package chilling.encore.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class LearningCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long learningCenterIdx;
    @Column(nullable = false)
    private String region;
    @Column(nullable = false)
    private String learningName;
    @Column(nullable = false)
    private double x;
    @Column(nullable = false)
    private double y;
}
