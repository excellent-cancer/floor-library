package gray.light.blog.service;

import gray.light.blog.entity.Blog;
import gray.light.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    private final BlogSourceService blogSourceService;

    public List<Blog> findBlogs(Long ownerId, Page page) {
        return blogRepository.findByOwnerId(ownerId, page.nullable());
    }

    public Optional<Blog> findBlog(Long id) {
        return blogRepository.find(id);
    }

    public boolean addBlog(Blog blog, byte[] content) {
        String downloadLink = blogSourceService.updateBlog(content);
        blog.setDownloadLink(downloadLink);
        return blogRepository.save(blog);
    }

}
