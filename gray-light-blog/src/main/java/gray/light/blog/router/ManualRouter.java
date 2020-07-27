package gray.light.blog.router;

import gray.light.blog.customizer.BlogCustomizer;
import gray.light.blog.customizer.TagCustomizer;
import gray.light.blog.entity.Blog;
import gray.light.blog.entity.Tag;
import gray.light.blog.indices.BlogIndex;
import gray.light.blog.search.BlogSearchOptions;
import gray.light.blog.service.TagService;
import gray.light.blog.service.WritableBlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ManualRouter {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private final WritableBlogService writableBlogService;

    private final BlogSearchOptions blogSearchOptions;

    private final TagService tagService;

    private List<Tag> tags;

    @PostConstruct
    public void xx() {
        tags = new ArrayList<>(tagService.allTags());
    }

    private List<Tag> ramd() {
        int size = ThreadLocalRandom.current().nextInt(10) + 10;

        Set<Tag> tagsSet = new HashSet<>();

        for (int i, j = 0; j < size; ) {
            i = ThreadLocalRandom.current().nextInt(tags.size());
            Tag tag = tags.get(i);
            if (!tagsSet.contains(tag)) {
                tagsSet.add(tag);
                j++;
            }
        }

        return new ArrayList<>(tagsSet);
    }

    @Bean
    public RouterFunction<ServerResponse> manualTag() {

        HandlerFunction<ServerResponse> handler = request -> {
            String[] tags = request.queryParam("tags").get().split(",");

            for (String tag : tags) {
                try {
                    log.error(tag);
                    tagService.createTag(TagCustomizer.wrap(tag));
                } catch (Exception e) {
                    log.error("重复: {}", tag, e);
                }

            }

            return ServerResponse.ok().build();
        };


        return RouterFunctions.route(RequestPredicates.GET("/manual/tags"), handler);
    }

    @Bean
    public RouterFunction<ServerResponse> manualScan() {
        return RouterFunctions.route(RequestPredicates.GET("/manual/md"), this::scanHandler);
    }

    private Mono<ServerResponse> scanHandler(ServerRequest request) {
        String pathParam = request.queryParam("path").get();


        for (int i = 2; i <= 13; i++) {
            long finalI = i;
            executor.execute(() -> addMarkdown(pathParam, finalI));
        }


        return ServerResponse.ok().build();
    }

    private void addMarkdown(final String pathParam, long ownerId) {
        Path path = Paths.get(pathParam);
        MarkdownVisitor visitor = new MarkdownVisitor();

        try {
            Files.walkFileTree(path, visitor);

            for (Path p : visitor.files) {
                Blog blog = BlogCustomizer.ofLocal(p, ownerId);
                byte[] content = Files.readAllBytes(p);

                log.error(blog.toString());
                writableBlogService.addBlog(blog, content);

                List<Tag> tags = ramd();
                log.error(tags.stream().map(Tag::getName).collect(Collectors.joining(",")));
                writableBlogService.addTags(blog, tags);

                BlogIndex blogIndex = BlogIndex.builder().title(blog.getTitle()).
                        id(blog.getId()).
                        tags(tags.stream().map(Tag::getName).collect(Collectors.toList())).
                        content(new String(content)).
                        build();

                log.error(blogIndex.getTitle());
                blogSearchOptions.addDocument(blogIndex);
            }
        } catch (IOException e) {
            log.error("Failed to visit files: ", e);
        }
    }


    private static class MarkdownVisitor implements FileVisitor<Path> {

        private List<Path> files = new LinkedList<>();

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (ignored(dir)) {
                return FileVisitResult.SKIP_SUBTREE;
            }

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {


            if (!ignored(file) && isMarkdown(file)) {
                files.add(file);
            }

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.TERMINATE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        private boolean ignored(Path path) {
            return path.getFileName().toString().startsWith(".");
        }

        private boolean isMarkdown(Path path) {
            String name = path.getFileName().toString();
            int index = name.lastIndexOf(".");
            return index > -1 && "md".equals(name.substring(index + 1));
        }
    }


}
