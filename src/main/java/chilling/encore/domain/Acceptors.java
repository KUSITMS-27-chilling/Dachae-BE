package chilling.encore.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Acceptors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acceptorIdx;
    @ManyToOne
    private User user;
    @ManyToOne
    private Suggest suggest;
}
