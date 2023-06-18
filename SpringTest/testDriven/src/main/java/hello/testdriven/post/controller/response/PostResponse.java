package hello.testdriven.post.controller.response;

import hello.testdriven.post.domain.Post;
import hello.testdriven.user.controller.response.UserResponse;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PostResponse {

    private Long id;
    private String content;
    private Long createdAt;
    private Long modifiedAt;
    private UserResponse writer;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .writer(UserResponse.from(post.getWriter()))
                .build();
    }
}
