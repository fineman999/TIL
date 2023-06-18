package hello.testdriven.post.service;

import hello.testdriven.common.domain.exception.ResourceNotFoundException;
import hello.testdriven.common.service.port.ClockHolder;
import hello.testdriven.post.controller.port.PostService;
import hello.testdriven.post.domain.Post;
import hello.testdriven.post.domain.PostCreate;
import hello.testdriven.post.domain.PostUpdate;

import hello.testdriven.post.service.port.PostRepository;
import hello.testdriven.user.domain.User;
import hello.testdriven.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Builder
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    @Override
    public Post getById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    @Transactional
    @Override
    public Post create(PostCreate postCreate) {
        User writer = userRepository.getById(postCreate.getWriterId());
        Post post = Post.from(writer, postCreate, clockHolder);
        return postRepository.save(post);
    }

    @Transactional
    @Override
    public Post update(long id, PostUpdate postUpdate) {
        Post post = getById(id);
        post = post.update(postUpdate, clockHolder);
        return postRepository.save(post);
    }
}