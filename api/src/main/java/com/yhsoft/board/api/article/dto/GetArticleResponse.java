package com.yhsoft.board.api.article.dto;

import com.yhsoft.board.domain.article.domain.ArticleEntity;
import java.time.LocalDateTime;

public record GetArticleResponse(Long articleId, String title, String content,
                                 LocalDateTime createdAt, String username) {

  public static GetArticleResponse fromEntity(ArticleEntity article) {
    return new GetArticleResponse(
        article.getArticleId(),
        article.getTitle(),
        article.getContent(),
        article.getCreatedAt(),
        article.getUserEntity().getUsername()
    );
  }
}
