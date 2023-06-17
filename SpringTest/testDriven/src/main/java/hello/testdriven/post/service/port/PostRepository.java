package hello.testdriven.post.service.port;

import hello.testdriven.post.infrastructure.PostEntity;

import java.util.Optional;

public interface PostRepository {
    Optional<PostEntity> findById(long id);

    PostEntity save(PostEntity postEntity);
}
