package com.example.social_network.models.Entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "post_tags")
@IdClass(PostTagId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostTag {
    @Id
    @Column(name = "post_id")
    private Long postId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;
}
