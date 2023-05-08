package chilling.encore.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Suggest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long suggestIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;

    private String region;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String teacherName;
    private String category;
    @CreationTimestamp
    private LocalDate createdAt;
    private LocalDate endDate;
}
