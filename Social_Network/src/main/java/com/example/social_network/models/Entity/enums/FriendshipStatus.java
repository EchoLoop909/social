package com.example.social_network.models.Entity.enums;

public enum FriendshipStatus {
    PENDING,    // Đang chờ xác nhận
    ACCEPTED,   // Đã là bạn bè
    BLOCKED,    // Bị chặn
    FOLLOWING,  // Đang theo dõi (nếu không cần xác nhận)
    NONE        // Không có mối quan hệ nào
}
