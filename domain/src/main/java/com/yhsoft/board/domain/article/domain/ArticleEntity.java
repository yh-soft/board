package com.yhsoft.board.domain.article.domain;

import com.yhsoft.board.domain.board.domain.BoardEntity;
import com.yhsoft.board.domain.user.domain.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@Entity
@Table(name = "article")
public class ArticleEntity {

  @Id
  @GeneratedValue
  private Long articleId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private BoardEntity boardEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  private String title;
  private String content;

  @CreatedDate
  private LocalDate createdAt;
  @LastModifiedDate
  private LocalDate updatedAt;
}
