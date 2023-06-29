package chilling.encore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewIdx", nullable = false)
    private Review review;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false)
    private boolean isDelete;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentIdx")
    private ReviewComments parent;

    @JsonManagedReference
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewComments> child;

}
