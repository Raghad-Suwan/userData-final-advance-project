package edu.najah.cap.data.handler;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;

public class PostDeletionHandler implements ItemDeletionHandler<Post>{
    private final IPostService postService;
    public PostDeletionHandler(IPostService postService) {
        this.postService = postService;
    }
    @Override
    public void handleDeletion(Post post) throws SystemBusyException, BadRequestException, NotFoundException {
        postService.deletePost(post.getAuthor(), post.getId());
    }
}
