package API_Automation;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class GraphQL_Practice {
    public static void main(String[] args ){

        //Query
        String response =given().log().all().header("Content-Type","application/json")
                .body("{\"query\":\"query($characterId: Int!,$episodeId: Int!){\\n  \\n  character(characterId: $characterId)\\n  {\\n    name\\n    gender\\n    status\\n    id\\n    type\\n  }\\n  location(locationId: 29242)\\n  {\\n    name\\n    dimension\\n  }\\n  episode(episodeId: $episodeId)\\n  {\\n    name\\n    air_date\\n    episode\\n  }\\n  characters(filters:{name :\\\"Rahul\\\"})\\n  {\\n    info\\n    {\\n      count\\n    }\\n    result\\n    {\\n      id\\n      name\\n      type\\n    }\\n    \\n  }\\n  episodes(filters:{name: \\\"hulu\\\"})\\n  {\\n    result\\n    {\\n      id\\n      name\\n      air_date\\n      episode\\n    }\\n  }\\n}\",\"variables\":{\"characterId\":21617,\"episodeId\":19650}}")
                .when().post("https://rahulshettyacademy.com/gq/graphql")
                .then().extract().response().asString();

        System.out.println(response);

        JsonPath js = new JsonPath(response);
        String characterName=js.getString("data.character.name");
        Assert.assertEquals(characterName,"Abhijeet");

        //Mutations
        String mutation_response =given().log().all().header("Content-Type","application/json")
                .body("{\"query\":\"mutation($locationName:String!,$characterName:String!,$episodeName:String!)\\n{\\n  createLocation(location: {name:$locationName,type:\\\"Northzone\\\",dimension:\\\"234\\\"}) {\\n    id\\n  }\\n  \\n  createCharacter(character: {name:$characterName,type:\\\"Macho\\\",status:\\\"Alive\\\",species:\\\"human\\\",gender:\\\"male\\\",image:\\\"png\\\",\\n    originId:29245,locationId:29245}) {\\n    id\\n  }\\n  \\n  createEpisode(episode:{name:$episodeName,air_date:\\\"12-Feb\\\",episode:\\\"Ep01\\\"}){\\n    id\\n  }\\n  \\n  deleteLocations(locationIds:[29244,29242]){\\n    locationsDeleted\\n  }\\n}\",\"variables\":{\"locationName\":\"India\",\"characterName\":\"Abhijeet\",\"episodeName\":\"Red Wedding\"}}")
                .when().post("https://rahulshettyacademy.com/gq/graphql")
                .then().extract().response().asString();

        System.out.println(mutation_response);


    }
}
