package com.yhsoft.board.api.article.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ListArticleResponse(List<Article> articles, boolean hasPrevious, boolean hasNext) {

  public record Article(Long articleId, String title, String content, String username,
                        LocalDateTime createdAt) {

  }
}
