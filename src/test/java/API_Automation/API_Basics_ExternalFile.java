package API_Automation;

import Files.Payload;
import Files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class API_Basics_ExternalFile {

    public static void main(String[] args) throws IOException {

        //Validate if Add Place API is working as expected

        //given - all input details
        //when - submit the API - resource , http method
        //then - validate the response

        RestAssured.baseURI="https://rahulshettyacademy.com";

        //Add Place -> Update Place with new address -> Get place to validate if new address is present in response

        //Here we are reading the payload of Add place API from external JSON file
        //While doing thi we need to read the content of file and convert to string
        //content of file convereted to BYTE -> Byte to string

        //Files.readAllBytes(Path path) --> we cannot directly pass the string path under Files.readAllBytes()
        // as it does not accept String , it accept Path Class

        //Extracting response
        String response = given().log().all().queryParams("key", "qaclick123").header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get("C:\\Users\\ASUS\\Desktop\\API_Testing\\AddPlace.json")))).when().post("maps/api/place/add/json")
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
