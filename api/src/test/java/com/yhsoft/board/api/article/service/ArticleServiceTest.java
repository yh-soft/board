package com.yhsoft.board.api.article.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yhsoft.board.api.article.dto.CreateArticleRequest;
import com.yhsoft.board.api.article.dto.CreateArticleResponse;
import com.yhsoft.board.domain.article.dao.ArticleRepository;
import com.yhsoft.board.domain.board.dao.BoardRepository;
import com.yhsoft.board.domain.board.domain.BoardEntity;
import com.yhsoft.board.domain.user.dao.UserRepository;
import com.yhsoft.board.domain.user.domain.UserEntity;
import com.yhsoft.board.domain.user.domain.UserStatus;
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
        new CreateArticleRequest(
            boardId,
            "title",
            "content",
            userId
        )
    );
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
        new CreateArticleRequest(
            1L,
            "title",
            "content",
            userId
        )
    )).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  @DisplayName("유효하지 않은 계정 정보로 작성 요청 시 실패")
  void createNewArticle_withInvalidUserId_throwError() {
    // Given
    Long boardId = createBoard();

    // When & Then
    assertThatThrownBy(() -> articleService.createNewArticle(
        new CreateArticleRequest(
            boardId,
            "title",
            "content",
            1L
        )
    )).isInstanceOf(DataIntegrityViolationException.class);
  }
}
