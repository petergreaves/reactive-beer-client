package com.springfirst.learning.reactive.webclient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Beer {

    private String id;
    private String beerName;
    private String upc;
    private Integer quantityOnHand;
    private String price;
    private String beerStyle;


}
