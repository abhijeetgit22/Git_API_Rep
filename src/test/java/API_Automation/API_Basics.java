package API_Automation;

import Files.Payload;
import Files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class API_Basics {

    public static void main(String[] args){

        //Validate if Add Place API is working as expected

        //given - all input details
        //when - submit the API - resource , http method
        //then - validate the response

        RestAssured.baseURI="https://rahulshettyacademy.com";

        //Running ADD place API
        /*given().log().all().queryParams("key", "qaclick123").header("Content-Type", "application/json")
        .body(Payload.AddPlace()).when().post("maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP"))
                .header("server","Apache/2.4.52 (Ubuntu)");*/

        //Add Place -> Update Place with new address -> Get place to validate if new address is present in response

        //Extracting response
        String response = given().log().all().queryParams("key", "qaclick123").header("Content-Type", "application/json")
                .body(Payload.AddPlace()).when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope",equalTo("APP"))
                .header("server","Apache/2.4.52 (Ubuntu)").extract().response().asString();

        System.out.println(response);

        JsonPath js = new JsonPath(response);  // this class is used for parisng json and takes string value as argument
        String placeID=js.getString("place_id");
        System.out.println(placeID);

        String newAddress="70 Abhieet walk, Africa";

        //Update Place

        given().log().all().queryParams("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeID+"\",\n" +
                        "\"address\":\""+newAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}\n")
                .when().put("maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));

        //Get Place
        String getPlaceResponse = given().log().all().queryParams("key","qaclick123")
                .queryParams("place_id",placeID)
                .when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();

        JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
        String actualAddress=js1.getString("address");
        System.out.println(actualAddress);

        Assert.assertEquals(actualAddress , newAddress);




    }
}
