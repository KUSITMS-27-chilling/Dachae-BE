package chilling.encore.domain.center.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Center {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionIdx;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String region;
    @Column(nullable = false)
    private int favCount;
    @Column(nullable = false)
    private String tell;

    public void plusFavCount() {
        favCount++;
    }
}
