package com.yhsoft.board.domain.article.dao;

import com.yhsoft.board.domain.article.domain.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

}
