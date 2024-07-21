package com.yhsoft.board.api.article.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yhsoft.board.api.article.dto.CreateArticleRequest;
import com.yhsoft.board.api.article.dto.CreateArticleResponse;
import com.yhsoft.board.api.article.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ArticleController.class)
@DisplayName("ArticleController 테스트")
class ArticleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ArticleService articleService;

  @Test
  @DisplayName("article 작성 성공")
  void createArticle_withValidPayload_returnSuccess() throws Exception {
    // Given
    given(articleService.createNewArticle(new CreateArticleRequest(
        1L,
        "title",
        "content",
        1L
    ))).willReturn(new CreateArticleResponse(1L, 1L));
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/article").contentType(APPLICATION_JSON).content("""
            {"boardId": 1, "title": "title", "content": "content", "userId": 1}
            """));
    // Then
    perform.andExpect(status().isCreated())
        .andExpect(jsonPath("$.boardId").value(1L))
        .andExpect(jsonPath("$.articleId").value(1L));
  }

  @Test
  @DisplayName("게시판 정보 없이 article 작성을 시도하면 에러 발생")
  void createArticle_withoutBoardId_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/article").contentType(APPLICATION_JSON).content("""
            {"title": "title", "content": "content", "userId": 1}
            """));
    // Then
    perform.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시판 글 작성 시 제목의 길이가 60자를 넘어가면 에러 발생")
  void createArticle_withTitleLengthLargerThan60_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/article").contentType(APPLICATION_JSON).content("""
            {"boardId": 1, "title": "%s", "content": "content", "userId": 1}
            """.formatted("A".repeat(61))));
    // Then
    perform.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시판 글 작성 시 제목이 빈 문자열인 경우 에러 발생")
  void createArticle_withEmptyTitle_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/article").contentType(APPLICATION_JSON).content("""
            {"boardId": 1, "title": "  ", "content": "content", "userId": 1}
            """));
    // Then
    perform.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시판 글 작성 시 내용의 길이가 255자를 넘는 경우 에러 발생")
  void createArticle_withContentLengthLargerThan255_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/article").contentType(APPLICATION_JSON).content("""
            {"boardId": 1, "title": "title", "content": "%s", "userId": 1}
            """.formatted("A".repeat(256))));
    // Then
    perform.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시판 글 작성 시 내용이 빈 문자열인 경우 에러 발생")
  void createArticle_withEmptyContent_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/article").contentType(APPLICATION_JSON).content("""
            {"boardId": 1, "title": "title", "content": "  ", "userId": 1}
            """));
    // Then
    perform.andExpect(status().isBadRequest());
  }
}
