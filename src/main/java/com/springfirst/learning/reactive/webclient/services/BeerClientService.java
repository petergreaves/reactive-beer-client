package com.springfirst.learning.reactive.webclient.services;

import com.springfirst.learning.reactive.webclient.domain.Beer;
import com.springfirst.learning.reactive.webclient.domain.BeerPagedList;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;


public interface BeerClientService {

    Mono<Beer> getBeerById(String beerId, boolean showInventoryOnHand);
    Mono<BeerPagedList> getBeers(Integer pageNumber,
                                 Integer pageSize,
                                 Boolean showInventoryOnHand,
                                 String beerName,
                                 String  beerStyle);
    Mono<Beer> getBeerByUPC(String upc);
    Mono<ResponseEntity> createBeer(Beer newBeer);
    Mono<ResponseEntity> updateBeer(Beer updatedBeer);
    Mono<ResponseEntity> deleteBeer(String beerId);


}
