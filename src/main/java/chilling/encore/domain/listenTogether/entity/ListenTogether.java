package chilling.encore.domain.listenTogether.entity;

import chilling.encore.domain.comments.listenTogether.entity.ListenComments;
import chilling.encore.domain.program.entity.Program;
import chilling.encore.domain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ListenTogether {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listenIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programIdx")
    private Program program;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false)
    private int hit;
    @Column(nullable = false)
    private int goalNum;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "listenTogether")
    List<ListenComments> listexnComments = new ArrayList<>();
    @OneToMany(mappedBy = "listenTogether")
    List<Participants> participants = new ArrayList<>();

    public void upHit() {
        hit++;
    }
}
