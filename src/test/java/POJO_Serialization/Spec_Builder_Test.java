package POJO_Serialization;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Spec_Builder_Test {

    public static void main(String[] args){

       // RestAssured.baseURI="https://rahulshettyacademy.com/";

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

        //Request Spec Builder (common part of code in given() block )
        RequestSpecification req= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/").addQueryParam("key","qaclick123")
                .setContentType(ContentType.JSON).build();

        //Response Spec Builder (common part of code in then() block )
        ResponseSpecification resspec=new ResponseSpecBuilder().expectStatusCode(200)
                .expectContentType(ContentType.JSON).build();

        //Spec Builder can be used in 2 ways

        //First ->

        Response res = given().log().all().spec(req)
                .body(p).when().post("maps/api/place/add/json")
                .then().spec(resspec).extract().response();

        String response=res.asString();
        System.out.println(response);

        //Second (we divide given(),when() in separate lines)

        RequestSpecification resp= given().log().all().spec(req).body(p);

        Response res2 = resp.when().post("maps/api/place/add/json").then().spec(resspec).extract().response();

        String response2=res2.asString();
        System.out.println(response2);

    }
}
