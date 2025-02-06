package me.kimjaemin.springbootblog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Article> articles;

    @Column(name = "allowed_role", nullable = false)
    private String allowedRole;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Category(String name, String allowedRole) {
        this.name = name;
        this.articles = new ArrayList<>();
        if (allowedRole != null && allowedRole.equals("ROLE_ADMIN")) {
            this.allowedRole = "ROLE_ADMIN";
        } else {
            this.allowedRole = "ROLE_USER";
        }
    }

    public void update(String name, String allowedRole) {
        this.name = name;
        if (allowedRole != null && allowedRole.equals("ROLE_ADMIN")) {
            this.allowedRole = "ROLE_ADMIN";
        } else {
            this.allowedRole = "ROLE_USER";
        }
    }

}
