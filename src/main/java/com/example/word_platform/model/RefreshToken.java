package com.example.word_platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
  @Id
  private Long id;

  @Column(nullable = false, unique = true)
  private String token;

  @OneToOne
  @ToString.Exclude
  private AppUser user;

  public void revoke() {
    user.setRefreshToken(null);
    setUser(null);
  }
}
