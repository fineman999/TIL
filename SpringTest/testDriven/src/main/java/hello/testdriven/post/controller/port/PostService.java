package hello.testdriven.post.controller.port;

import hello.testdriven.post.domain.Post;
import hello.testdriven.post.domain.PostCreate;
import hello.testdriven.post.domain.PostUpdate;
import org.springframework.transaction.annotation.Transactional;

public interface PostService {
    Post getById(long id);

    @Transactional
    Post create(PostCreate postCreate);

    @Transactional
    Post update(long id, PostUpdate postUpdate);
}
