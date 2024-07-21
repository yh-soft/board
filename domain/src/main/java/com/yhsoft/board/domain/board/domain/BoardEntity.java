package com.yhsoft.board.domain.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "board")
public class BoardEntity {
    @Id
    @GeneratedValue
    private Long boardId;

    @Column(nullable = false)
    private String boardName;
}
