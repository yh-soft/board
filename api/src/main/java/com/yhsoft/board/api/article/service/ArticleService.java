package com.yhsoft.board.api.article.service;

import com.yhsoft.board.api.article.dto.CreateArticleRequest;
import com.yhsoft.board.api.article.dto.CreateArticleResponse;
import com.yhsoft.board.api.article.dto.GetArticleResponse;
import com.yhsoft.board.api.article.dto.ListArticleResponse;
import com.yhsoft.board.api.article.exception.ArticleNotFoundException;
import com.yhsoft.board.domain.article.dao.ArticleRepository;
import com.yhsoft.board.domain.article.domain.ArticleEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ArticleService {

  public static final int PAGE_SIZE = 10;
  private final ArticleRepository articleRepository;

  @Transactional
  public CreateArticleResponse createNewArticle(CreateArticleRequest request) {
    ArticleEntity entity = request.toEntity();
    ArticleEntity saved = articleRepository.save(entity);

    return new CreateArticleResponse(saved.getBoardEntity().getBoardId(), saved.getArticleId());
  }

  public ListArticleResponse getArticlesWithBoardId(Long boardId, Optional<Integer> pageNumber) {
    Page<ArticleEntity> result = articleRepository.findArticleEntitiesByBoardEntity_BoardId(boardId,
        PageRequest.of(pageNumber.orElse(0), PAGE_SIZE, Sort.by(Order.desc("articleId"))));

    return new ListArticleResponse(result.getContent().stream().map(
        item -> new ListArticleResponse.Article(item.getArticleId(), item.getTitle(),
            item.getContent(), item.getUserEntity().getUsername(), item.getCreatedAt())).toList(),
        result.hasPrevious(), result.hasNext());
  }

  public GetArticleResponse getArticleById(Long articleId) {
    ArticleEntity article = articleRepository.findArticleByArticleId(
        articleId).orElseThrow(() -> new ArticleNotFoundException("게시글을 찾을 수 없습니다."));

    return GetArticleResponse.fromEntity(article);
  }
}
