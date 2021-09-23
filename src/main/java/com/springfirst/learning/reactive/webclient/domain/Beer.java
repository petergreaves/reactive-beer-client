package com.springfirst.learning.reactive.webclient.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Beer {

    private UUID id;
    private String beerName;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private String beerStyle;


}
