package com.springfirst.learning.reactive.webclient.services;

import com.springfirst.learning.reactive.webclient.config.WebClientConfig;
import com.springfirst.learning.reactive.webclient.domain.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

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
                beerClientService.getBeers(null, null, null, null,null);
        BeerPagedList  beerPagedList = beerPagedListMono.block();
        assertThat(beerPagedList).isNotNull();
        //System.out.println(beerPagedList.getContent().size());
        assertThat(beerPagedList.getContent().size()).isGreaterThan(0);
    }

    @Test
    public void getBeersPageSize10() throws Exception {

        Mono<BeerPagedList> beerPagedListMono =
                beerClientService.getBeers(1, 10, null, null,null);
        BeerPagedList  beerPagedList = beerPagedListMono.block();
        assertThat(beerPagedList).isNotNull();
        assertThat(beerPagedList.getContent().size()).isEqualTo(10);
    }

    @Test
    public void getBeersNoRecords() throws Exception {

        Mono<BeerPagedList> beerPagedListMono =
                beerClientService.getBeers(2, 20, null, null,null);
        BeerPagedList  beerPagedList = beerPagedListMono.block();
        assertThat(beerPagedList).isNotNull();
        assertThat(beerPagedList.getContent().size()).isEqualTo(0);
    }




    @Test
    public void getBeerById() throws Exception {
    }


    @Test
    public void getBeerByUPC() throws Exception {
    }

    @Test
    public void createBeer() throws Exception {
    }

    @Test
    public void updateBeer() throws Exception {
    }

    @Test
    public void deleteBeer() throws Exception {
    }
}