package com.example.social_network.models.Entity;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoryViewId implements Serializable {
    private Long story;
    private Long viewer;
}
