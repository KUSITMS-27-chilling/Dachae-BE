package chilling.encore.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
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
    @JoinColumn(name = "userIdx")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programIdx")
    private Program program;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int hit;

    private int goalNum;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "listenTogether")
    List<ListenComments> listenComments = new ArrayList<>();
}
