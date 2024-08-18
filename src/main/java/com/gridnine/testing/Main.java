package com.gridnine.testing;

import com.gridnine.testing.builder.FlightBuilder;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.filter.FlightFilter;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Flight> flights = new FlightBuilder().createFlights();
        System.out.println("Данные без фильтров \n" + flights + "\n");

        List<Flight> flightsArrivalBeforeDeparturePresent = new FlightFilter(flights)
                .arrivalBeforeDeparturePresent().build();
        System.out.println("Отъезд до настоящего момента \n" + flightsArrivalBeforeDeparturePresent + "\n");

        List<Flight> flightsArrivalBeforeDeparture = new FlightFilter(flights)
                .filterArrivalBeforeDeparture().build();
        System.out.println("Прибытие перед отправлением \n" + flightsArrivalBeforeDeparture + "\n");

        List<Flight> flightsTimeMoreTwoHours = new FlightFilter(flights)
                .timeMoreTwoHours().build();
        System.out.println("Нахождения на земле более двух часов \n" + flightsTimeMoreTwoHours + "\n");
    }
}