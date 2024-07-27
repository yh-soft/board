package com.yhsoft.board.api.article.dto;

import com.yhsoft.board.domain.article.domain.ArticleEntity;
import com.yhsoft.board.domain.board.domain.BoardEntity;
import com.yhsoft.board.domain.user.domain.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateArticleRequest(@NotNull Long boardId, @NotBlank @Length(max = 60) String title,
                                   @NotBlank @Length(max = 255) String content) {

  public ArticleEntity toEntity(Long userId) {
    ArticleEntity entity = new ArticleEntity();
    BoardEntity boardEntity = new BoardEntity();
    boardEntity.setBoardId(boardId);
    entity.setBoardEntity(boardEntity);
    entity.setTitle(title);
    entity.setContent(content);
    UserEntity userEntity = new UserEntity();
    userEntity.setUserId(userId);
    entity.setUserEntity(userEntity);

    return entity;
  }
}
