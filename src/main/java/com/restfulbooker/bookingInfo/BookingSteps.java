package com.restfulbooker.bookingInfo;

import com.restfulbooker.constants.EndPoints;
import com.restfulbooker.model.BookingPojo;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import java.util.HashMap;


public class BookingSteps {
    
    @Step("Creating booking with firstName : {0}, lastname : {1}, totalprice : {2},depositpaid: {3}, checkIn: {4} , checkOut: {5}, additionalneeds : {6}")
    public ValidatableResponse createBooking(String firstname, String lastname, int totalprice, boolean depositpaid, String checkIn, String checkOut, String additionalneeds) {
        BookingPojo bookingPojo = BookingPojo.getBookingPojo(firstname, lastname, totalprice, depositpaid, checkIn, checkOut, additionalneeds);

        return SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .body(bookingPojo)
                .post()
                .then();
    }


    @Step("Getting the Booking information with id :{0}")
    public HashMap<String, Object> getBookingInfoByid(int bookingId) {
        String s1 = "findAll{it.id == '";
        String s2 = "'}.get(0)";
        return SerenityRest
                .given()
                .pathParam("bookingId", bookingId)
                .when()
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then().statusCode(200)
                .extract()
                .path(s1 + bookingId + s2);
    }


    @Step("Updating Booking information with Id:{0}, lastname : {1}, totalprice : {2},depositpaid: {3}, checkIn: {4} , checkOut: {5}, additionalneeds : {6} ")
    public ValidatableResponse updateBooking(int bookingId,String firstname, String lastname, int totalprice, boolean depositpaid, String checkIn, String checkOut, String additionalneeds) {
        BookingPojo userPojo = BookingPojo.getBookingPojo( firstname, lastname, totalprice, depositpaid, checkIn, checkOut, additionalneeds);
        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .pathParam("bookingId", bookingId)
                .body(userPojo)
                .when()
                .put(EndPoints.UPDATE_BOOKING_BY_ID)
                .then();
    }

    @Step("Deleting Booking information with studentId: {0}")
    public ValidatableResponse deleteBooking(int bookingId) {
        return SerenityRest.given().log().all()
                .pathParam("bookingId", bookingId)
                .when()
                .delete(EndPoints.DELETE_BOOKING_BY_ID)
                .then();
    }

    @Step("Getting Booking information with studentId: {0}")
    public ValidatableResponse getBookingId(int studentId) {
        return SerenityRest.given().log().all()
                .pathParam("studentID", studentId)
                .when()
                .get(EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then();
    }



}


