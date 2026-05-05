package OAuth2;

import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

public class oAuth_Test {
    public static void main(String[] args){

        System.setProperty("webdriver.chrome.driver","C://chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
        driver.findElement(By.xpath()).sendkeys();  //for username
        driver.findElement(By.xpath()).sendkeys(Keys.ENTER);
        Thread.sleep(3000);
        driver.findElement(By.xpath()).sendkeys();  //for password
        driver.findElement(By.xpath()).sendkeys(Keys.ENTER);
        Thread.sleep(3000);

        String url= driver.getCurrentUrl();

        //fetching auth code from url
       String partialCode =url.split("code=")[1];
       String code=  partialCode.split("&scope")[0];
       System.out.println(code);

       //urlEncodingEnabled(false) --> This is being used as the code we are getting in line above may contain special character
       // and if we that code as it is in given block rest assured will encrypt that by default hence to avoid that we can use this


        String accessTokenResponse= given().queryParams("code",code).urlEncodingEnabled(false)
                .queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type","authorization_code")
                .when().log().all()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();

        JsonPath js= new JsonPath(accessTokenResponse);
        String access_token = js.getString("access_token");

       String response = given().queryParams("access_token",access_token)
                .when().get("https://rahulshettyacademy.com/getCourse.php").asString();

        System.out.println(response);
    }
}
