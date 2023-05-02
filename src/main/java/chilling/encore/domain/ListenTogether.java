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
public class ListenTogether {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listenIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;
    @ManyToOne
    @JoinColumn(name = "programIdx")
    private Program program;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int hit;

    private int goalNum;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
