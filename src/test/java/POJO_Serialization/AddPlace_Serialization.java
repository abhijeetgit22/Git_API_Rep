package POJO_Serialization;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class AddPlace_Serialization {

    public static void main(String[] args){

        RestAssured.baseURI="https://rahulshettyacademy.com/";

        AddPlacePOJO p= new AddPlacePOJO();
        p.setAccuracy(50);
        p.setAddress("29, side layout, cohen 09");
        p.setLanguage("French-IN");
        p.setName("Frontline house");
        p.setPhone_number("(+91) 983 893 3937");
        p.setWebsite("http://google.com");

        List<String> typeList= new ArrayList<String>();
        typeList.add("shoe park");
        typeList.add("shop");
        p.setTypes(typeList);

        Location loc= new Location();
        loc.setLat(-38.383494);
        loc.setLng(33.427362);

        p.setLocation(loc);


        Response res = given().log().all().queryParams("key","qaclick123")
                .body(p).when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).extract().response();

        String response=res.asString();
        System.out.println(response);

    }
}
