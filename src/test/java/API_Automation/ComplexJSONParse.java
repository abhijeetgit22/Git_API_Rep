package API_Automation;

import Files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJSONParse {

    public static void main(String[] args){
        JsonPath js = new JsonPath(Payload.coursePrice());

        //Print No of courses returned by API
        int count = js.getInt("courses.size()");
        System.out.println(count);

        //Print Purchase Amount
        int totalAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println(totalAmount);

        //Print Title of the first course
        String firstCourseTitle = js.get("courses[0].title");  //.get() is same as .getString() as for both output is String
        System.out.println(firstCourseTitle);

        //Print All course titles and their respective Prices

        for (int i=0; i<count; i++)
        {
            String title= js.get("courses["+i+"].title");
            int price= js.getInt("courses["+i+"].price");

            System.out.println(title);
            System.out.println(price);
        }

        //Print no of copies sold by RPA Course
        for (int i=0;i<count;i++){
            String title= js.get("courses["+i+"].title");
            if (title.equalsIgnoreCase("RPA")){
                int copies = js.getInt("courses["+i+"].copies");
                System.out.println(copies);
                break;
            }
        }

        //Verify if Sum of all Course prices matches with Purchase Amount

        int actualTotalAmount=0;
        for (int i=0; i<count; i++)
        {

            int price= js.getInt("courses["+i+"].price");
            int copies = js.getInt("courses["+i+"].copies");

            int courseAmount = price* copies;
            actualTotalAmount+=courseAmount;

        }
        System.out.println("Actual Total Amount is - "+actualTotalAmount);
        if(totalAmount==actualTotalAmount)
            System.out.println("Sum of all Course prices matches with Purchase Amount");
        else
            System.out.println("Sum of all Course prices does not matches with Purchase Amount");

    }


}
