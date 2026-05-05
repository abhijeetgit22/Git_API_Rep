package POJO;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class POJO_Deserialization {

    public static void main(String[] args){

        RestAssured.baseURI= "https://rahulshettyacademy.com/";

        //OAuth Token
        String oAuthResponse=given().log().all().formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret","erZOWM9g3UtwNRj340YYaK_W")
                .formParam("grant_type","client_credentials")
                .formParam("scope","trust")
                .when().post("oauthapi/oauth2/resourceOwner/token")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath js= new JsonPath(oAuthResponse);
        String accessToken = js.get("access_token");
        System.out.println(accessToken);

        //GetCourseDetails

        GetCourse gc =   given().log().all().queryParams("access_token",accessToken)
                .when().log().all().get("/oauthapi/getCourseDetails").as(GetCourse.class);

        //Here we are converting the json response into Java object using GetCourse class

        System.out.println(gc.getLinkedIn());
        System.out.println(gc.getInstructor());

        //Printing title of Soap UI course using hardcoding
        String soapUITitle= gc.getCourses().getApi().get(1).getCourseTitle();
        System.out.println(soapUITitle);

        //Printing price of Soap UI course dynamically using iteration

        List<Api > apicourses= gc.getCourses().getApi();
        for (int i=0;i<apicourses.size();i++){
            if(apicourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
                System.out.println(apicourses.get(i).getPrice());

        }

        //Printing all course Titles of Web Automation
        List <WebAutomation> webAutomationCourses = gc.getCourses().getWebAutomation();
        for (int i=0;i<webAutomationCourses.size();i++){
            System.out.println(webAutomationCourses.get(i).getCourseTitle());
        }

        //Matching the list of course titles of web automation from an expected array of titles

        //expected array
        String[] expectedArr= {"Selenium Webdriver Java","Cypress","Protractor"};

        //Adding titles into Actual List
        ArrayList<String> actualList = new ArrayList<String>();

        for (int i=0;i<webAutomationCourses.size();i++){
            actualList.add(webAutomationCourses.get(i).getCourseTitle());

        }

        //Converting expected Array into List
        List<String > expectedList=Arrays.asList(expectedArr);

        Assert.assertTrue(expectedList.equals(actualList));






    }
}
