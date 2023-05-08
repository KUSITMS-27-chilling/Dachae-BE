package chilling.encore.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    @JoinColumn(name = "userIdx")
    private User user;
    @ManyToOne
    @JoinColumn(name = "listenIdx")
    private ListenTogether listenTogether;

    @Column(columnDefinition = "TEXT")
    private String content;
    private int ref;
    private int refOrder;
    private int step;

    private Long parentIdx;
    private int childSum;
    private boolean isDelete;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
