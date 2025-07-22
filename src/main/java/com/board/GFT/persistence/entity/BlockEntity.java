package com.board.GFT.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BlockEntity {

    private Long id;
    private OffsetDateTime blockedAt;
    private String blockreason;
    private OffsetDateTime unblockedAt;
    private String unblockreason;
}
