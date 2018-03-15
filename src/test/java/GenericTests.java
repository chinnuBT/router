import annotations.custom.MyHttpMethod;
import litew8.MyUserServlet;
import org.junit.Test;
import static org.junit.Assert.*;

public class GenericTests {

    @Test
    public void testMethodAnnosArray(){
        //System.out.println("Hello Test");
        try {
            MyHttpMethod[] arr =  MyUserServlet.class
                    .getMethod("addUser") //toString - will return 0 length but nonnull Array
                    .getAnnotationsByType(MyHttpMethod.class);

            assertNotNull(arr);
            assertTrue(arr.length>0);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSpecificAnnotationOnMethod(){
        try {
            MyHttpMethod myHttpMethod = MyUserServlet.class
                    .getMethod("addUser") //toString - will return null
                    .getAnnotation(MyHttpMethod.class);
            assertNotNull(myHttpMethod);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
