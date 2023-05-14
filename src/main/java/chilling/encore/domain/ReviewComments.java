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
    private int childSum;
    private boolean isDelete;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentIdx", referencedColumnName = "reviewCommentIdx")
    private ReviewComments parent;

    @JsonManagedReference
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewComments> child;

    /**
     *   private String brdno; //게시글Idx !!
     *     private String reno; //댓글 Idx !!
     *     private String rewriter; //댓글쓴 Idx !!
     *     private String redeleteflag; //삭제여부
     *     private String rememo; //내용 !!
     *     private String redate; //작성날짜 !!
     *     private String reparent; //부모댓글 !!
     *     private String redepth; //깊이 !!
     *     private Integer reorder; //순서 !!
     */
}
