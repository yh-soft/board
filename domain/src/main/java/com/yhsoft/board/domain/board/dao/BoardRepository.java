package com.yhsoft.board.domain.board.dao;

import com.yhsoft.board.domain.board.domain.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

}
