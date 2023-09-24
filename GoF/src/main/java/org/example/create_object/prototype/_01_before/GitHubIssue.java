package org.example.create_object.prototype._01_before;

public class GitHubIssue {
    private int id;
    private String title;

    private GitHubRepository repository;

    public GitHubIssue(GitHubRepository repository) {
        this.repository = repository;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GitHubRepository getRepository() {
        return repository;
    }

    public void setRepository(GitHubRepository repository) {
        this.repository = repository;
    }

    public String getUrl() {
        return String.format("https://github.com/%s/%s/issues/%d", repository.getUser(), repository.getName(), id);
    }
}
