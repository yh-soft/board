package com.yhsoft.board.api.article.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yhsoft.board.api.article.dto.CreateArticleRequest;
import com.yhsoft.board.api.article.dto.CreateArticleResponse;
import com.yhsoft.board.api.article.dto.GetArticleResponse;
import com.yhsoft.board.api.article.dto.ListArticleResponse;
import com.yhsoft.board.api.article.exception.ArticleNotFoundException;
import com.yhsoft.board.domain.article.dao.ArticleRepository;
import com.yhsoft.board.domain.board.dao.BoardRepository;
import com.yhsoft.board.domain.board.domain.BoardEntity;
import com.yhsoft.board.domain.user.dao.UserRepository;
import com.yhsoft.board.domain.user.domain.UserEntity;
import com.yhsoft.board.domain.user.domain.UserStatus;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
@DisplayName("ArticleService 테스트")
class ArticleServiceTest {

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BoardRepository boardRepository;

  private void cleanup() {
    articleRepository.deleteAll();
    boardRepository.deleteAll();
    userRepository.deleteAll();
  }

  @BeforeEach
  void beforeEach() {
    cleanup();
  }

  @AfterEach
  void afterEach() {
    cleanup();
  }

  private Long createUser() {
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("userA");
    userEntity.setPassword("1234");
    userEntity.setEmail("email@email.com");
    userEntity.setStatus(UserStatus.VERIFY);
    UserEntity saved = userRepository.save(userEntity);
    return saved.getUserId();
  }

  private Long createBoard() {
    BoardEntity boardEntity = new BoardEntity();
    boardEntity.setBoardName("boardA");
    BoardEntity saved = boardRepository.save(boardEntity);
    return saved.getBoardId();
  }

  @Test
  @DisplayName("게시글 작성 성공")
  void createNewArticle_withValidBoardIdAndUserId_success() {
    // Given
    Long userId = createUser();
    Long boardId = createBoard();

    // When
    CreateArticleResponse newArticle = articleService.createNewArticle(
        new CreateArticleRequest(boardId, "title", "content", userId));
    // Then
    assertThat(newArticle).hasFieldOrPropertyWithValue("boardId", boardId)
        .hasFieldOrProperty("articleId");
  }

  @Test
  @DisplayName("유효하지 않은 게시판 정보로 작성 요청 시 실패")
  void createNewArticle_withInvalidArticleId_throwError() {
    // Given
    Long userId = createUser();

    // When & Then
    assertThatThrownBy(() -> articleService.createNewArticle(
        new CreateArticleRequest(1L, "title", "content", userId))).isInstanceOf(
        DataIntegrityViolationException.class);
  }

  @Test
  @DisplayName("유효하지 않은 계정 정보로 작성 요청 시 실패")
  void createNewArticle_withInvalidUserId_throwError() {
    // Given
    Long boardId = createBoard();

    // When & Then
    assertThatThrownBy(() -> articleService.createNewArticle(
        new CreateArticleRequest(boardId, "title", "content", 1L))).isInstanceOf(
        DataIntegrityViolationException.class);
  }

  @Test
  @DisplayName("게시글이 여러 개 작성된 경우 해당 목록을 조회할 수 있어야 함")
  void getArticlesWithBoard_hasMultiple_returnList() {
    // Given
    Long userId = createUser();
    Long boardId1 = createBoard();
    Long boardId2 = createBoard();

    for (int i = 0; i < 30; ++i) {
      articleService.createNewArticle(
          new CreateArticleRequest(boardId1, "title0_" + i, "content" + i, userId));
      articleService.createNewArticle(
          new CreateArticleRequest(boardId2, "title1_" + i, "content" + i, userId));
    }
    // When
    ListArticleResponse result = articleService.getArticlesWithBoardId(boardId1, Optional.of(1));
    // Then
    assertThat(result.hasNext()).isTrue();
    assertThat(result.hasPrevious()).isTrue();
    assertThat(result.articles()).hasSize(10).extracting("title").isEqualTo(
        List.of("title0_19", "title0_18", "title0_17", "title0_16", "title0_15", "title0_14",
            "title0_13", "title0_12", "title0_11", "title0_10"));
  }

  @Test
  @DisplayName("존재하는 게시글 id로 조회했을 때 게시글을 확인할 수 있어야 함")
  void getArticle_withValidArticleId_returnSuccess() {
    // Given
    Long userId = createUser();
    Long boardId = createBoard();
    CreateArticleResponse newArticle = articleService.createNewArticle(
        new CreateArticleRequest(boardId, "title", "content", userId));
    Long articleId = newArticle.articleId();

    // When
    GetArticleResponse article = articleService.getArticleById(articleId);

    // Then
    assertThat(article).hasFieldOrPropertyWithValue("articleId", articleId)
        .hasFieldOrPropertyWithValue("title", "title")
        .hasFieldOrPropertyWithValue("content", "content")
        .hasFieldOrPropertyWithValue("username", "userA").hasFieldOrProperty("createdAt");
  }

  @Test
  @DisplayName("존재하지 않는 게시글 id로 조회했을 때 에러가 발생해야 함")
  void getArticle_withInvalidArticleId_throwError() {
    // When & Then
    assertThatThrownBy(() -> articleService.getArticleById(1L)).isExactlyInstanceOf(
        ArticleNotFoundException.class);
  }
}
