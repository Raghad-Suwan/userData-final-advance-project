package edu.najah.cap.data.proxy;

import edu.najah.cap.data.export.ValidateUser;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;
import edu.najah.cap.posts.PostService;

import java.util.*;


public class PostServiceProxy implements IPostService {


    private static final Map<String, List<Post>> posts = PostService.getPosts();
    private IPostService postService; // or PostService postService = new PostService();

    public PostServiceProxy(IPostService postService) {
        this.postService = postService;
    }

    @Override
    public void addPost(Post post) {
        postService.addPost(post);
    }

    @Override
    public List<Post> getPosts(String author) throws BadRequestException, NotFoundException {
        ValidateUser.validateUser(author);
        if (!posts.containsKey(author)) {
            throw new NotFoundException("User does not exist");
        }
        return posts.get(author);
    }

    @Override
    public void deletePost(String author, String id) throws BadRequestException, NotFoundException {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ValidateUser.validateUser(author);
        if (!posts.containsKey(author)) {
            throw new NotFoundException("User does not exist");
        }
        List<Post> authorPosts = posts.get(author);
        if (authorPosts != null) {
            Iterator<Post> iterator = authorPosts.iterator();
            while (iterator.hasNext()) {
                Post post = iterator.next();
                if (Objects.equals(post.getId(), id)) {
                    iterator.remove();
                }
            }
        }
    }
}