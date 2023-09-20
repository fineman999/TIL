package hello.testdriven.post.controller;

import hello.testdriven.post.domain.PostCreate;
import hello.testdriven.post.controller.response.PostResponse;
import hello.testdriven.post.controller.port.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시물(posts)")
@Builder
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostCreateController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreate postCreate) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(PostResponse.from(postService.create(postCreate)));
    }
}