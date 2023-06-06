package com.restfulbooker.studentinfo;

import com.restfulbooker.constants.EndPoints;
import com.restfulbooker.model.BookingDatesPojo;
import com.restfulbooker.model.BookingPojo;
import com.restfulbooker.testbase.TestBase;
import com.restfulbooker.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasValue;


@RunWith(SerenityRunner.class)
public class BookingCURDTest extends TestBase {
    static String firstName = "Anna" + TestUtils.getRandomValue();
    static String lastName = "Olins" + TestUtils.getRandomValue();
    static int totalPrice = 101;
    static boolean depositpaid = true;
    static BookingDatesPojo bookingdates;
    static String additionalneeds = "Breakfast";
    static int bookingId;

    public static String Token;

    @Title("This will create a new Booking")
    @Test
    public void test001() {
        bookingdates = new BookingDatesPojo();
        bookingdates.setCheckin("2018-01-01");
        bookingdates.setCheckout("2019-01-01");

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstName);
        bookingPojo.setLastname(lastName);
        bookingPojo.setTotalprice(totalPrice);
        bookingPojo.setDepositpaid(depositpaid);
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalneeds(additionalneeds);

        SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .body(bookingPojo)
                .post()
                .then().log().all().statusCode(200);

    }

    @Title("Verify if the student was added to the application")
    @Test
    public void test002() {
        String s1 = "firstName";

        HashMap<String, Object> bookingMap = SerenityRest.given()
                .when()
                .get(EndPoints.CREATE_BOOKING)
                .then().statusCode(200)
                .extract()
                .path(s1);
        Assert.assertThat(bookingMap, hasValue(firstName));
    }

    @Title("Update the user information and verify the updated information")
    @Test
    public void test003() {
        firstName = firstName + "_updated";
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstName);
        bookingPojo.setLastname(lastName);
        bookingPojo.setTotalprice(totalPrice);
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalneeds(additionalneeds);


        SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .pathParam("studentID", bookingId)
                .body(bookingPojo)
                .when()
                .put(EndPoints.UPDATE_BOOKING_BY_ID)
                .then().log().all().statusCode(200);

        String s1 = "findAll{it.firstName == '";
        String s2 = "'}.get(0)";
        HashMap<String, Object> studentMap = SerenityRest.given()
                .when()
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then().statusCode(200)
                .extract()
                .path(s1 + firstName + s2);
        Assert.assertThat(studentMap, hasValue(firstName));
    }

    @Title("Delete the student and verify if the student is deleted!")
    @Test
    public void test004() {
        SerenityRest.given().log().all()
                .pathParam("studentID", bookingId)
                .when()
                .delete(EndPoints.DELETE_BOOKING_BY_ID)
                .then()
                .statusCode(204);


    }
}