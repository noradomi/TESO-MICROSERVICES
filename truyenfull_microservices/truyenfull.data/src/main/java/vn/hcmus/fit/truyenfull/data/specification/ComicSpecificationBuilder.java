package vn.hcmus.fit.truyenfull.data.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.hcmus.fit.truyenfull.data.model.Comic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Asus on 11/29/2019.
 */
public class ComicSpecificationBuilder {
    private final List<SearchCriteria> params;

    public ComicSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public ComicSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Comic> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(ComicSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i)
                    .isOrPredicate()
                    ? Specification.where(result)
                    .or(specs.get(i))
                    : Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }
}
