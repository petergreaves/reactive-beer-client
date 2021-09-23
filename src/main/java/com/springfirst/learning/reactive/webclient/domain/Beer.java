package com.springfirst.learning.reactive.webclient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Beer {

    private String id;
    private String name;
    private String upc;
    private Integer quantityOnHand;
    private String price;
    private String beerStyle;


}
