package com.softwebdevelopers.ecommerce.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BasePage {

    private int pageNo;

    private int pageSize;
}
