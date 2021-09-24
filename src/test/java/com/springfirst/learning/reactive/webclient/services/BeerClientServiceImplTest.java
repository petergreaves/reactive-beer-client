package com.springfirst.learning.reactive.webclient.services;

import com.springfirst.learning.reactive.webclient.config.WebClientConfig;
import com.springfirst.learning.reactive.webclient.domain.Beer;
import com.springfirst.learning.reactive.webclient.domain.BeerPagedList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class BeerClientServiceImplTest {

    BeerClientService beerClientService;

    @BeforeEach
    void setUp() {

        // mimics DI at lower cost
        beerClientService = new BeerClientServiceImpl(new WebClientConfig().webclient());
    }


    @Test
    public void getBeers() throws Exception {

        Mono<BeerPagedList> beerPagedListMono =
                beerClientService.getBeers(null, null, null, null, null);
        BeerPagedList beerPagedList = beerPagedListMono.block();
        assertThat(beerPagedList).isNotNull();
        //System.out.println(beerPagedList.getContent().size());
        assertThat(beerPagedList.getContent().size()).isGreaterThan(0);
    }

    @Test
    public void getBeersPageSize10() throws Exception {

        Mono<BeerPagedList> beerPagedListMono =
                beerClientService.getBeers(1, 10, null, null, null);
        BeerPagedList beerPagedList = beerPagedListMono.block();
        assertThat(beerPagedList).isNotNull();
        assertThat(beerPagedList.getContent().size()).isEqualTo(10);
    }

    @Test
    public void getBeersNoRecords() throws Exception {

        Mono<BeerPagedList> beerPagedListMono =
                beerClientService.getBeers(2, 20, null, null, null);
        BeerPagedList beerPagedList = beerPagedListMono.block();
        assertThat(beerPagedList).isNotNull();
        assertThat(beerPagedList.getContent().size()).isEqualTo(0);
    }


    @Test
    public void getBeerByIdNoInventory() throws Exception {

        BeerPagedList beerPagedList = getSimpleList();
        Beer firstBeer = beerPagedList.stream().findFirst().get();

        Mono<Beer> testBeerFoundByID = beerClientService.getBeerById(firstBeer.getId(), false);
        Beer b = testBeerFoundByID.block();

        assertThat(b).isNotNull();
        assertThat(b.getBeerName().equalsIgnoreCase(firstBeer.getBeerName()));
    }

    @Test
    public void getBeerByIdWithInventory() throws Exception {

        BeerPagedList beerPagedList = getSimpleList();
        Beer firstBeerWithQOHGtrThan0 = beerPagedList.stream().filter(b -> b.getQuantityOnHand() > 0).findFirst().get();

        Mono<Beer> testBeerFoundByID = beerClientService.getBeerById(firstBeerWithQOHGtrThan0.getId(), true);
        Beer b = testBeerFoundByID.block();

        assertThat(b).isNotNull();
        assertThat(b.getBeerName().equalsIgnoreCase(firstBeerWithQOHGtrThan0.getBeerName()));
        assertThat(b.getQuantityOnHand().equals(firstBeerWithQOHGtrThan0.getQuantityOnHand()));

    }

    @Test
    public void getBeerByUPC() throws Exception {

        BeerPagedList beerPagedList = getSimpleList();
        Beer firstBeer = beerPagedList.stream().findFirst().get();

        Mono<Beer> testBeerFoundByUPC = beerClientService.getBeerByUPC(firstBeer.getUpc());
        Beer b = testBeerFoundByUPC.block();

        assertThat(b).isNotNull();
        assertThat(b.getBeerName().equalsIgnoreCase(firstBeer.getBeerName()));
    }


    @Test
    public void createBeer() throws Exception {

        Beer beer = Beer.builder()
                .beerName("Old Smokey")
                .upc("2342342342")
                .beerStyle("IPA")
                .price(new BigDecimal(10.99))
                .build();

        ResponseEntity created = beerClientService.createBeer(beer).block();

        URI location = created.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(created).isNotNull();
        assertThat(created.getStatusCode().equals(HttpStatus.CREATED));
    }

    @Test
    public void updateBeer() throws Exception {

        BeerPagedList beerPagedList = getSimpleList();
        Beer toUpdate = beerPagedList.stream().findFirst().get();

        Beer updated = Beer.builder()
                .beerName("Old Smokey 2")
                .upc(toUpdate.getUpc())
                .beerStyle(toUpdate.getBeerStyle())
                .price(toUpdate.getPrice())
                .build();

        ResponseEntity<Void> responseEntityMono = beerClientService.updateBeer(toUpdate.getId(), updated).block();

        assertThat(responseEntityMono.getStatusCode().equals(HttpStatus.NO_CONTENT));
    }


//    @Test
//    public void mapAndFlapMap() throws Exception {
//
//        String[] sArray = new String[]{"a", "b", "c", "d"};
//        Arrays.stream(sArray)
//                .map(s -> Mono.just(s.toUpperCase(Locale.ROOT)))
//                .flatMap(sUpper -> sUpper);
//    }

    @Test
    public void functionalGetByID() throws Exception {

        AtomicReference<String> beerName = new AtomicReference<>();
        CountDownLatch countDownLatch  = new CountDownLatch(1);

        beerClientService.getBeers(null, null, null, null, null)
                .map(beers -> beers.getContent().get(0).getId())
                .map(beerID -> beerClientService.getBeerById(beerID, false))
                .flatMap(beerMono -> beerMono)
                .subscribe(beer -> {
                    System.out.println(beer.getBeerName());
                    beerName.set(beer.getBeerName());
                    assertThat(beer.getBeerName()).isNotNull();
                    countDownLatch.countDown();
                });

       countDownLatch.await();
       assertThat(beerName.get()).isEqualTo("Mango Bobs");

    }

    @Test
    public void deleteBeer() throws Exception {

        BeerPagedList beerPagedList = getSimpleList();
        Beer toDelete = beerPagedList.stream().findFirst().get();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClientService.deleteBeer(toDelete.getId());
        ResponseEntity<Void> responseEntity = responseEntityMono.block();

        assertThat(responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT));
    }

    @Test
    public void deleteBeerNotFound() throws Exception {

        Mono<ResponseEntity<Void>> responseEntityMono = beerClientService.deleteBeer(UUID.randomUUID());

        Assertions.assertThatThrownBy(() -> {
            ResponseEntity<Void> responseEntity = responseEntityMono.block();
        }).isInstanceOf(org.springframework.web.reactive.function.client.WebClientResponseException.class);

    }


    private BeerPagedList getSimpleList() {
        return beerClientService.getBeers(null, null, null, null, null).block();
    }


}