package com.springfirst.learning.reactive.webclient.services;

import com.springfirst.learning.reactive.webclient.config.WebClientProperties;
import com.springfirst.learning.reactive.webclient.domain.Beer;
import com.springfirst.learning.reactive.webclient.domain.BeerPagedList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerClientServiceImpl implements BeerClientService {

    private final WebClient webClient;

    @Override
    public Mono<Beer> getBeerById(UUID beerId, boolean showInventoryOnhand) {

        return webClient
                .get()
                .uri(uribuilder ->
                        uribuilder.path(WebClientProperties.BEER_PATH_BY_ID)
                                .queryParamIfPresent("showInventoryOnhand", Optional.ofNullable(showInventoryOnhand))
                                .build(beerId)
                )
                .retrieve()
                .bodyToMono(Beer.class);

    }

    @Override
    public Mono<BeerPagedList> getBeers(Integer pageNumber, Integer pageSize,
                                        Boolean showInventoryOnhand,
                                        String beerName, String beerStyle) {
        return webClient
                .get()
                .uri(uribuilder ->
                        uribuilder.path(WebClientProperties.BEER_PATH)
                                .queryParamIfPresent("pageNumber", Optional.ofNullable(pageNumber))
                                .queryParamIfPresent("pageSize", Optional.ofNullable(pageSize))
                                .queryParamIfPresent("beerName", Optional.ofNullable(beerName))
                                .queryParamIfPresent("beerStyle", Optional.ofNullable(beerStyle))
                                .queryParamIfPresent("showInventoryOnhand", Optional.ofNullable(showInventoryOnhand))
                                .build()
                )
                .retrieve()
                .bodyToMono(BeerPagedList.class);
    }

    @Override
    public Mono<Beer> getBeerByUPC(String upc) {
        return webClient
                .get()
                .uri(uribuilder ->
                        uribuilder.path(WebClientProperties.BEER_UPC_PATH)
                                .build(upc)
                )
                .retrieve()
                .bodyToMono(Beer.class);
    }

    @Override
    public Mono<ResponseEntity<Void>> createBeer(Beer newBeer) {

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_PATH).build())
                .body(BodyInserters.fromValue(newBeer))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<Void>> updateBeer(UUID id, Beer updatedBeer) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_PATH_BY_ID).build(id))
                .body(BodyInserters.fromValue(updatedBeer))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteBeer(UUID beerId) {
        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_PATH_BY_ID).build(beerId))
                .retrieve()
                .toBodilessEntity();
    }
}
