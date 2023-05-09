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
public class ReviewComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewCommentIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewIdx")
    private Review review;

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
