package API_Automation;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.File;

import static io.restassured.RestAssured.*;

public class BugTest {

    public static void main(String[] args) {

        RestAssured.baseURI="https://abhijeetsrivastava0722.atlassian.net/";

        //Create Bug
        String createIssueresponse =given().header("Content-Type","application/json")
                .header("Authorization","Basic YWJoaWplZXQuc3JpdmFzdGF2YTA3MjJAZ21haWwuY29tOkFUQVRUM3hGZkdGMDZuanZJcEhzWnJIYkp2amNsd3QyN3l6Ty1jZVROdW1ZanVwLVlLR0U4NDhWVGpKRGh6N3ROdmxOakt6LXktejNPM2hSZ1dMaU13b3BRX1h4SzFyRVdKWC1ERGlhaWl2eHlyejM1QjBXc3drNkVLeVZ2WVlUQXhCa3NMY2pIa1lZd1FhQlFTa1dISGY3aENGUkhIQ3A5TS05Q3FNTUpaVkNPQV9zQXhQVUhCOD0zOUExQTY0RQ==")
                .body("{\n" +
                        "    \"fields\": {\n" +
                        "       \"project\":\n" +
                        "       {\n" +
                        "          \"key\": \"SCRUM\"\n" +
                        "       },\n" +
                        "       \"summary\": \"URL is not working - Automation Rest Assured\",\n" +
                        "       \"issuetype\": {\n" +
                        "          \"name\": \"Bug\"\n" +
                        "       }\n" +
                        "   }\n" +
                        "}\n")
                .log().all().when().post("rest/api/3/issue").then().log().all().assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath js= new JsonPath(createIssueresponse);
        String issueId=js.get("id");
        System.out.println(issueId);

        //Add Attachment

        given().pathParams("key",issueId)
                .header("X-Atlassian-Token","no-check")
                .header("Authorization","Basic YWJoaWplZXQuc3JpdmFzdGF2YTA3MjJAZ21haWwuY29tOkFUQVRUM3hGZkdGMDZuanZJcEhzWnJIYkp2amNsd3QyN3l6Ty1jZVROdW1ZanVwLVlLR0U4NDhWVGpKRGh6N3ROdmxOakt6LXktejNPM2hSZ1dMaU13b3BRX1h4SzFyRVdKWC1ERGlhaWl2eHlyejM1QjBXc3drNkVLeVZ2WVlUQXhCa3NMY2pIa1lZd1FhQlFTa1dISGY3aENGUkhIQ3A5TS05Q3FNTUpaVkNPQV9zQXhQVUhCOD0zOUExQTY0RQ==")
                .multiPart("file",new File("C:\\Users\\ASUS\\Desktop\\new updates\\aa.jpg")).log().all()
                .when().post("rest/api/3/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);

        //multipart(File file) --> this  is used to send attachments which is passed as form-data in postman.
        // We need t pass object of the file along with the path of file

        //pathParams () --> can be used for passing path parameters
        //{} --> this can be used inside post to pass path parameters




    }

}
