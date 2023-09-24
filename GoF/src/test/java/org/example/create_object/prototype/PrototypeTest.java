package org.example.create_object.prototype;

import org.example.create_object.prototype._02_after.GitHubIssue;
import org.example.create_object.prototype._02_after.GitHubRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PrototypeTest {

    @Test
    @DisplayName("프로토타입 패턴을 사용한 객체 생성 - clone 메서드를 사용하여 객체를 생성한다.")
    void test() throws CloneNotSupportedException {

        GitHubRepository repository = new GitHubRepository();

        repository.setUser("jojoldu");
        repository.setName("springboot-webservice");

        GitHubIssue gitHubIssue = new GitHubIssue(repository);
        gitHubIssue.setId(1);
        gitHubIssue.setTitle("테스트 코드 작성");

        String url = gitHubIssue.getUrl();
        assertThat(url).isEqualTo("https://github.com/jojoldu/springboot-webservice/issues/1");


        GitHubIssue clone = (GitHubIssue) gitHubIssue.clone();

        assertThat(clone).isNotSameAs(gitHubIssue); // clone != gitHubIssue why? 다른 객체(레퍼런스)이다.
        assertThat(clone).isEqualTo(gitHubIssue); // clone.equals(gitHubIssue) == true why? 값은 같지만 다른 객체이다.


        assertThat(clone.getRepository()).isSameAs(gitHubIssue.getRepository()); // clone.getRepository() == gitHubIssue.getRepository() why? 같은 객체(레퍼런스)이다.
        assertThat(clone.getUrl()).isNotSameAs(gitHubIssue.getUrl()); // clone.getUrl() != gitHubIssue.getUrl() why? 다른 객체(레퍼런스)이다.
    }

    @Test
    @DisplayName("실무에서 clone 사용하기 - 프로토타입 패턴 x")
    void test1() {

        GitHubRepository repository1 = new GitHubRepository();
        repository1.setUser("jojoldu");
        repository1.setName("springboot-webservice");

        GitHubRepository repository2 = new GitHubRepository();
        repository2.setUser("nodejs");
        repository2.setName("nodejs-book");


        List<GitHubRepository> repositories = Arrays.asList(repository1, repository2);


        // 인터페이스를 사용하기 때문에(인터페이스는 clone이 없다.) 생성자를 통해 객체를 생성해야 한다.
        List<GitHubRepository> clone = new ArrayList<>(repositories);

        assertThat(clone).isNotSameAs(repositories); // clone != repositories why? 다른 객체(레퍼런스)이다.
        assertThat(clone).isEqualTo(repositories); // clone.equals(repositories) == true why? 값은 같지만 다른 객체이다.
    }


}