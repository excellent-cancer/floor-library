package gray.light.support.web;

import org.springframework.web.reactive.function.server.ServerRequest;
import perishing.constraint.jdbc.Page;

import java.util.Optional;

public final class RequestSupport {
    
    public static Page extract(ServerRequest request) {
        Optional<String> pages = request.queryParam("pages");
        Optional<String> count = request.queryParam("count");

        return pages.isPresent() && count.isPresent() ?
                Page.newPage(pages.get(), count.get()) :
                Page.unlimited();
    }

}
