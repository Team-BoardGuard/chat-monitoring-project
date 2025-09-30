package com.example.authapp.post;

import com.example.authapp.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.postRepository.findAll(pageable);
    }

    public Post getPost(Long id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()) {
            Post post1 = post.get();
            post1.setViewCount(post1.getViewCount() + 1);
            this.postRepository.save(post1);
            return post1;
        } else {
            throw new RuntimeException("Post not found"); // Or a custom exception
        }
    }

    public void create(String title, String content, User author) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCreateDate(LocalDateTime.now());
        post.setAuthor(author);
        this.postRepository.save(post);
    }

    public void update(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        this.postRepository.save(post);
    }

    public void delete(Post post) {
        this.postRepository.delete(post);
    }

    public void vote(Post post, User user) {
        if (post.getVoter().contains(user)) {
            post.getVoter().remove(user);
        } else {
            post.getVoter().add(user);
        }
        this.postRepository.save(post);
    }

    public List<Post> getPopularList(int count) {
        List<Post> allPosts = this.postRepository.findAll();
        return allPosts.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getVoter().size(), p1.getVoter().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
