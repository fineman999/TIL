package org.hello.chapter01.item07.cache;

import java.util.Map;
import java.util.WeakHashMap;

public class PostRepository {
    private Map<CacheKey, Post> cache;

    public PostRepository() {
        cache = new WeakHashMap<>();
    }
    public Post getById(Integer id) {
        CacheKey cacheKey = new CacheKey(id);
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }
        // TODO DB에서 조회하거나 REST API를 통해 읽어올 수 있다.
        Post post = new Post();
        cache.put(cacheKey, post);
        return post;
    }

    public Map<CacheKey, Post> getCache() {
        return cache;
    }
}
