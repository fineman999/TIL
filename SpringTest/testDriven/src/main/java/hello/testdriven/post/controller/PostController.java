package hello.testdriven.post.controller;

import hello.testdriven.post.controller.port.PostService;
import hello.testdriven.post.controller.response.PostResponse;
import hello.testdriven.post.domain.PostUpdate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물(posts)")
@Builder
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable long id) {
        return ResponseEntity
            .ok()
            .body(PostResponse.from(postService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable long id, @RequestBody
    PostUpdate postUpdate) {
        return ResponseEntity
            .ok()
            .body(PostResponse.from(postService.update(id, postUpdate)));
    }


}