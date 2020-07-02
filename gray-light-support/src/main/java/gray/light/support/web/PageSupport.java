package gray.light.support.web;

import perishing.constraint.jdbc.Page;

import java.util.List;

public final class PageSupport {


    public static  <T> PageChunk<T> page(List<?> terrible, Page pageRequest) {
        if (pageRequest.isUnlimited()) {
            @SuppressWarnings("unchecked")
            List<T> items = (List<T>) terrible;

            return new PageChunk<>(pageRequest.getPage(), pageRequest.getPer(), items.size(), items);
        } else {
            @SuppressWarnings("unchecked")
            List<T> items = (List<T>) terrible.get(0);
            @SuppressWarnings("unchecked")
            int total = (int) ((List<T>) terrible.get(1)).get(0);

            return new PageChunk<>(pageRequest.getPage(), pageRequest.getPer(), total, items);
        }
    }



}
