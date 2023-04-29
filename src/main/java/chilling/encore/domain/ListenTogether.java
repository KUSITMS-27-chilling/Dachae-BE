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
    private User user;

    @ManyToOne
    private Program program;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int hit;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
