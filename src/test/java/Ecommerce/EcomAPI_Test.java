package Ecommerce;

import POJO.Ecom_Login_Request;
import POJO.Ecom_Login_Response;
import POJO.OrderDetails;
import POJO.Orders;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class EcomAPI_Test {

    public static void main(String[] args){

        //Login
        RequestSpecification req= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
                .setContentType(ContentType.JSON).build();

        Ecom_Login_Request loginRequest = new Ecom_Login_Request();
        loginRequest.setUserEmail("abhijeetsrivastava2296@gmail.com");
        loginRequest.setUserPassword("Abhijeet22@#");

        //relaxedHTTPSValidation() -- This can be used for bypassing SSL certificates
        RequestSpecification loginReq = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);

        Ecom_Login_Response loginResponse= loginReq.when().post("api/ecom/auth/login")
                .then().log().all().extract().response().as(Ecom_Login_Response.class);

        System.out.println(loginResponse.getToken());
        String token = loginResponse.getToken();
        System.out.println(loginResponse.getUserId());
        String userid = loginResponse.getUserId();


        //Create Product
        RequestSpecification createProdbaseReq= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
                .addHeader("Authorization",token).build();

        RequestSpecification reqCreateProd= given().log().all().spec(createProdbaseReq)
                .param("productName","qwertyAbhiLat")
                .param("productAddedBy",userid).param("productCategory","fashion")
                .param("productSubCategory","shirts").param("productPrice","11500")
                .param("productDescription","Addias Originals")
                .param("productFor","women")
                .multiPart("productImage",new File("C:\\Users\\ASUS\\Desktop\\new updates\\sample.jpg"));

        String createProdResp= reqCreateProd.when().post("api/ecom/product/add-product").
                then().log().all().extract().response().asString();

        JsonPath js = new JsonPath(createProdResp);
        String productId= js.get("productId");
        System.out.println("Product id - "+productId);

        //Create Order
        RequestSpecification createOrderbaseReq= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
                .setContentType(ContentType.JSON).addHeader("Authorization",token)
                .build();

        OrderDetails orderDetailObj = new OrderDetails();
        orderDetailObj.setCountry("India");
        orderDetailObj.setProductOrderedId(productId);

        List<OrderDetails> orderDetailList = new ArrayList<OrderDetails>();
        orderDetailList.add(orderDetailObj);

        Orders orderObj = new Orders();
        orderObj.setOrders(orderDetailList);

        RequestSpecification ReqcreateOrder= given().log().all().spec(createOrderbaseReq).body(orderObj);
        String createOrderResp= ReqcreateOrder.when().post("api/ecom/order/create-order").then().log().all()
                .extract().response().asString();

        System.out.println(createOrderResp);

        //DeleteProduct

        RequestSpecification deleteProdbaseReq= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
                .setContentType(ContentType.JSON).addHeader("Authorization",token)
                .build();

        RequestSpecification deleteProdReq= given().log().all().spec(deleteProdbaseReq)
                .pathParams("productId",productId);

        String deleteProdResp = deleteProdReq.when().delete("api/ecom/product/delete-product/{productId}")
                .then().log().all().extract().response().asString();

        // {} --> This indicates the path parameters , we can add the key mentioned in given block

        System.out.println(deleteProdResp);

        JsonPath js1= new JsonPath(deleteProdResp);
        String actualMessage = js1.get("message");
        Assert.assertEquals("Product Deleted Successfully",actualMessage);





    }
}
