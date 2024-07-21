package com.yhsoft.board.api.article.service;

import com.yhsoft.board.api.article.dto.CreateArticleRequest;
import com.yhsoft.board.api.article.dto.CreateArticleResponse;
import com.yhsoft.board.domain.article.dao.ArticleRepository;
import com.yhsoft.board.domain.article.domain.ArticleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ArticleService {

  private final ArticleRepository articleRepository;

  @Transactional
  public CreateArticleResponse createNewArticle(CreateArticleRequest request) {
    ArticleEntity entity = request.toEntity();
    ArticleEntity saved = articleRepository.save(entity);

    return new CreateArticleResponse(
        saved.getBoardEntity().getBoardId(),
        saved.getArticleId()
    );
  }
}
