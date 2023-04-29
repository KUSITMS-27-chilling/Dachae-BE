package chilling.encore.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class LearningCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long learningCenterIdx;
    private String region;
    private String learningName;
    private double x;
    private double y;
}
