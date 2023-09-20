package hello.testdriven.post.infrastructure;

import hello.testdriven.post.domain.Post;
import hello.testdriven.user.infrastructure.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "modified_at")
    private Long modifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity writer;

    public static PostEntity fromModel(Post post) {
        PostEntity postEntity = new PostEntity();
        postEntity.id = post.getId();
        postEntity.content = post.getContent();
        postEntity.modifiedAt = post.getModifiedAt();
        postEntity.createdAt = post.getCreatedAt();
        postEntity.writer = UserEntity.fromModel(post.getWriter());
        return postEntity;
    }

    public Post toModel() {
        return Post.builder()
                .id(id)
                .content(content)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .writer(writer.toModel())
                .build();
    }

}