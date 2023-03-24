package com.companyName.projectName.modelAttribute;

import com.mongodb.lang.Nullable;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Unwrapped;

@Data
public class ProductQueryParameter {
    private String keyword;
    private String orderBy;
    private String sortRule;
    private int priceFrom;
    @Unwrapped.Nullable
    private int priceTo;
}
