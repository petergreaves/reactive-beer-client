package com.springfirst.learning.reactive.webclient.services;

import com.springfirst.learning.reactive.webclient.domain.Beer;
import com.springfirst.learning.reactive.webclient.domain.BeerPagedList;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface BeerClientService {

    Mono<Beer> getBeerById(UUID beerId, boolean showInventoryOnHand);
    Mono<BeerPagedList> getBeers(Integer pageNumber,
                                 Integer pageSize,
                                 Boolean showInventoryOnHand,
                                 String beerName,
                                 String  beerStyle);
    Mono<Beer> getBeerByUPC(String upc);
    Mono<ResponseEntity<Void>> createBeer(Beer newBeer);
    Mono<ResponseEntity<Void>> updateBeer(UUID id, Beer updatedBeer);
    Mono<ResponseEntity<Void>> deleteBeer(UUID beerId);


}
