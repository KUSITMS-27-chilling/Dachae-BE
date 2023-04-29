package chilling.encore.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ListenComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listenCommentIdx;

    @ManyToOne
    private User user;
    @ManyToOne
    private ListenTogether listenTogether;

    @Column(columnDefinition = "TEXT")
    private String content;
    private int ref;
    private int refOrder;
    private int step;

    private Long parentIdx;
    private int childSum;
    private boolean isDelete;

    private LocalDateTime createdAt;
}
