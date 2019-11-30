package vn.hcmus.fit.truyenfull.data.specification;

import lombok.Data;

/**
 * Created by Asus on 11/29/2019.
 */
@Data
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    private boolean orPredicate;

    public SearchCriteria(final String key, final String operation, final Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
