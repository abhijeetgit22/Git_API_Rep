package Files;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DynamicJSON {

    @Test(dataProvider = "BooksData")
    public void addBook(String isbn , String aisle){

        RestAssured.baseURI="http://216.10.245.166";

        //dynamically adding values into payload
        String response = given().log().all().header("Content-Type","application/json")
                .body(Payload.AddBook(isbn ,aisle )).when().post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js= ReusableMethods.rawToJson(response);
        String id = js.get("ID");
        System.out.println(id);
    }

    @DataProvider(name ="BooksData")
    public Object[][] getData(){
        return new Object[][] {{"ghyt","7652"},{"aserf","23451"},{"ouhg","4567"}};
    }
}
