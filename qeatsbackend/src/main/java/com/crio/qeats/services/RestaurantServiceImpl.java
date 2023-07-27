
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.utils.*;
import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;

  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;

  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
    List<Restaurant> restaurants = new ArrayList<>();

    // Get the appropriate serving radius based on the current time
    Double servingRadiusInKms = getCurrentServingRadius(currentTime);

    try {
      // Fetch all restaurants close by using the restaurantRepositoryService
      restaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(
          getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), currentTime, servingRadiusInKms);
    } catch (Exception e) {
      log.error("Error fetching restaurants: {}", e.getMessage());
      // Handle the exception accordingly (e.g., return an error response)
    }

    return GetRestaurantsResponse.builder()
        .restaurants(restaurants)
        .build();
  }

  // Helper method to determine the serving radius based on the current time
  private Double getCurrentServingRadius(LocalTime currentTime) {
    return isPeakHours(currentTime) ? peakHoursServingRadiusInKms : normalHoursServingRadiusInKms;
  }

  // Helper method to check if the given time is within peak hours
  private boolean isPeakHours(LocalTime currentTime) {
    // You can implement the logic to determine peak hours based on your requirements
    // For example, assuming peak hours are between 6 PM and 9 PM
    LocalTime peakStart = LocalTime.of(18, 0);
    LocalTime peakEnd = LocalTime.of(21, 0);
    return currentTime.isAfter(peakStart) && currentTime.isBefore(peakEnd);
  }

  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(GetRestaurantsRequest getRestaurantsRequest,LocalTime currentTime, Double servingRadiusInKms) {
    List<Restaurant> restaurants = new ArrayList<>();

    
    try {
      // Fetch all restaurants close by using the restaurantRepositoryService
      restaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), currentTime, servingRadiusInKms);
    } catch (Exception e) {
      log.error("Error fetching restaurants: {}", e.getMessage());
      // Handle the exception accordingly (e.g., return an error response)
    }

    return GetRestaurantsResponse.builder()
        .restaurants(restaurants)
        .build();
  }

}

