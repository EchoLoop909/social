package com.example.social_network.models.dto;

import lombok.Data;

@Data
public class PostDto {
    private Long userId;                  // ID người đăng
    private String caption;               // nội dung
    private String location;              // địa điểm
//    private List<String> mediaUrls;       // danh sách ảnh/video
//    private List<CommentResponseDTO> comments; // danh sách comment
//    private Set<String> tags;             // danh sách tên tag (#food, #travel)
}
