package com.yhsoft.board.api.article.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yhsoft.board.api.article.dto.CreateArticleRequest;
import com.yhsoft.board.api.article.dto.CreateArticleResponse;
import com.yhsoft.board.api.article.dto.GetArticleResponse;
import com.yhsoft.board.api.article.dto.ListArticleResponse;
import com.yhsoft.board.api.article.exception.ArticleNotFoundException;
import com.yhsoft.board.api.article.service.ArticleService;
import com.yhsoft.board.security.JwtConfiguration;
import com.yhsoft.board.security.SpringSecurityConfig;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ArticleController.class)
@Import({JwtConfiguration.class, SpringSecurityConfig.class})
@DisplayName("ArticleController 테스트")
class ArticleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ArticleService articleService;

  @WithMockUser(authorities = "ROLE_USER")
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

  @WithMockUser(authorities = "ROLE_USER")
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

  @WithMockUser(authorities = "ROLE_USER")
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

  @WithMockUser(authorities = "ROLE_USER")
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

  @WithMockUser(authorities = "ROLE_USER")
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

  @WithMockUser(authorities = "ROLE_USER")
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

  @Test
  @DisplayName("로그인을 하지 않은 경우 게시글 작성시 에러가 발생해야 함")
  void createArticle_withNoUser_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/article").contentType(APPLICATION_JSON).content("""
            {"boardId": 1, "title": "title", "content": "content", "userId": 1}
            """));
    // Then
    perform.andExpect(status().isForbidden());
  }

  @WithMockUser(authorities = "ROLE_USER")
  @Test
  @DisplayName("게시글 목록을 조회했을 때 적절한 목록을 보여줘야 함")
  void getArticlesWithBoardId_withValidBoardId_returnOk() throws Exception {
    // Given
    given(articleService.getArticlesWithBoardId(1L, Optional.of(1))).willReturn(
        new ListArticleResponse(
            List.of(new ListArticleResponse.Article(1L, "title", "content", "userA",
                LocalDateTime.now())),
            true,
            false
        )
    );
    // When
    ResultActions perform = mockMvc.perform(
        get("/api/v1/article").queryParam("boardId", "1").queryParam("page", "1"));
    // Then
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.articles").isArray())
        .andExpect(jsonPath("$.hasPrevious").value(true))
        .andExpect(jsonPath("$.hasNext").value(false));
  }

  @Test
  @DisplayName("로그인을 하지 않은 경우 게시글 목록을 조회했을 때 에러가 발생해야 함")
  void getArticlesWithBoardId_withNoUser_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        get("/api/v1/article").queryParam("boardId", "1").queryParam("page", "1"));
    // Then
    perform.andExpect(status().isForbidden());
  }

  @WithMockUser(authorities = "ROLE_USER")
  @Test
  @DisplayName("적절한 게시글 id로 조회했을 때 해당 게시글 내용을 보여줘야 함")
  void getArticleByArticleId_withValidArticleId_returnOk() throws Exception {
    // Given
    given(articleService.getArticleById(1L)).willReturn(
        new GetArticleResponse(1L, "title", "content", LocalDateTime.now(), "userA")
    );
    // When
    ResultActions perform = mockMvc.perform(
        get("/api/v1/article/{articleId}", 1L));
    // Then
    perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.articleId").value(1L))
        .andExpect(jsonPath("$.title").value("title"))
        .andExpect(jsonPath("$.content").value("content"))
        .andExpect(jsonPath("$.createdAt").isNotEmpty())
        .andExpect(jsonPath("$.username").value("userA"));
  }

  @WithMockUser(authorities = "ROLE_USER")
  @Test
  @DisplayName("존재하지 않은 게시글 id로 조회했을 때 에러가 발생해야 함")
  void getArticleByArticleId_withInvalidArticleId_raiseError() throws Exception {
    // Given
    given(articleService.getArticleById(1L)).willThrow(new ArticleNotFoundException(
        "게시글을 찾을 수 없습니다."
    ));
    // When
    ResultActions perform = mockMvc.perform(
        get("/api/v1/article/{:articleId}", 1L));
    // Then
    perform.andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("로그인을 하지 않은 경우 게시글 조회시 에러가 발생해야 함")
  void getArticleByArticleId_withNoUser_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        get("/api/v1/article/{articleId}", 1L));
    // Then
    perform.andExpect(status().isForbidden());
  }
}
