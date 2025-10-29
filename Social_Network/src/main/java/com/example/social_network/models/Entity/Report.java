package com.example.social_network.models.Entity;

import com.example.social_network.models.Entity.enums.ReportStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "reports")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="reporter_id")
    private User reporter;

    @ManyToOne @JoinColumn(name="reported_user_id")
    private User reportedUser;

    @ManyToOne @JoinColumn(name="reported_post_id")
    private Post reportedPost;

    @ManyToOne @JoinColumn(name="reported_comment_id")
    private Comment reportedComment;

    @Column(length=255)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.OPEN;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
