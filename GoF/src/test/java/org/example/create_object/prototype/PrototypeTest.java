package org.example.create_object.prototype;

import org.example.create_object.prototype._01_before.GitHubIssue;
import org.example.create_object.prototype._01_before.GitHubRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrototypeTest {

    @Test
    @DisplayName("프로토타입을 사용하지 않은 객체 생성")
    void test() {

        GitHubRepository repository = new GitHubRepository();

        repository.setUser("jojoldu");
        repository.setName("springboot-webservice");

        GitHubIssue gitHubIssue = new GitHubIssue(repository);
        gitHubIssue.setId(1);
        gitHubIssue.setTitle("테스트 코드 작성");

        String url = gitHubIssue.getUrl();
        assertThat(url).isEqualTo("https://github.com/jojoldu/springboot-webservice/issues/1");


//        GitHubIssue clone = gitHubIssue.clone();

        // clone != gitHubIssue why? 다른 객체(레퍼런스)이다.
        // clone.equals(gitHubIssue) == true why? 값은 같지만 다른 객체이다.
    }
}