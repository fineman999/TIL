package hello.testdriven.post.service.port;

import hello.testdriven.post.domain.Post;

import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(long id);

    Post save(Post post);
}
