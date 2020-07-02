package gray.light.support.web;

import lombok.RequiredArgsConstructor;
import perishing.constraint.treasure.chest.collection.Entry2;
import perishing.constraint.treasure.chest.collection.FinalVariables;

@RequiredArgsConstructor
public class RequestParam<T> {

    private final Entry2<String, RequestParamExtractor<T>> entry;

    public String getKey() {
        return entry.get1();
    }

    public RequestParamExtractor<T> getExtractor() {
        return entry.get2();
    }

    public T get(FinalVariables<String> variables) {
        return variables.get(getKey());
    }

    public boolean contains(FinalVariables<String> variables) {
        return variables.contains(getKey());
    }


}
