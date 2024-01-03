package edu.najah.cap.data.deletionHandler;
import edu.najah.cap.data.proxy.PostServiceProxy;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;

public class PostDeletionHandler implements ItemDeletionHandler<Post>{
    private final IPostService PostServiceProxy ;

    public PostDeletionHandler(IPostService postService) {
        this.PostServiceProxy = new PostServiceProxy(postService);
    }
    @Override
    public void handleDeletion(Post post) throws SystemBusyException, BadRequestException, NotFoundException {
        PostServiceProxy.deletePost(post.getAuthor(), post.getId());
    }
}
