package com.kr.moviecatalogservice.resources;

import com.kr.moviecatalogservice.models.CatalogItem;
import com.kr.moviecatalogservice.models.Movie;
import com.kr.moviecatalogservice.models.Rating;
import com.kr.moviecatalogservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    @HystrixCommand(fallbackMethod = "getCatalogFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "9000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
                    /*@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "180000")*/
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
    }
                   /* ,
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "180000") }*/
                    )
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {


        UserRating userRating = getUserRating(userId);
        return userRating.getRatings().stream()
                .map(rating -> getCatalogItem(rating))
                .collect(Collectors.toList());

    }


    public List<CatalogItem> getCatalogFallback(@PathVariable("userId") String userId) {
        return Arrays.asList( new CatalogItem("No movie found", "NA", 0));

    }
//    @HystrixCommand(fallbackMethod = "getUserRatingFallback")
     private UserRating getUserRating(String userId) {
         return  restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
     }
 /*   private UserRating getUserRatingFallback(String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(Arrays.asList(new Rating("",0)));
        return userRating;
    }*/

//    @HystrixCommand(fallbackMethod = "getCatalogItemFallback")
    private CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }
   /* private CatalogItem getCatalogItemFallback(Rating rating) {
        return new CatalogItem("No movie found", "NA", 0);
    }*/
}

/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/