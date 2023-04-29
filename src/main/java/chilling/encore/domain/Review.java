package chilling.encore.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewIdx;

    @ManyToOne
    private User user;

    @ManyToOne
    private Program program;

    private int week;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String image;
    private int hit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
