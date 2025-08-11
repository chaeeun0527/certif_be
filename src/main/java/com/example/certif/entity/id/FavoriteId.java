package com.example.certif.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FavoriteId {
    @Column(name = "certificate_id")
    private Long certificateId;

    @Column(name = "user_id")
    private Long userId;
}
