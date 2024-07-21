package com.yhsoft.board.domain.article.dao;

import com.yhsoft.board.domain.article.domain.ArticleEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

  Page<ArticleEntity> findArticleEntitiesByBoardEntity_BoardId(Long boardId, Pageable pageable);

  @Query("select a from ArticleEntity a join fetch a.userEntity u where a.articleId = :articleId")
  Optional<ArticleEntity> findArticleByArticleId(Long articleId);
}
