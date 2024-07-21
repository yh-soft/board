package com.yhsoft.board.api.article.controller;

import com.yhsoft.board.api.article.dto.CreateArticleRequest;
import com.yhsoft.board.api.article.dto.CreateArticleResponse;
import com.yhsoft.board.api.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

  private final ArticleService articleService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CreateArticleResponse createArticle(@Validated @RequestBody CreateArticleRequest request) {
    return articleService.createNewArticle(request);
  }
}
