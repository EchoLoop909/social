package com.example.social_network.models.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name = "setting_key", length = 100)
    private String key;

    @Column(columnDefinition = "TEXT")
    private String value;
}
