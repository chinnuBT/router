package litew8;

import annotations.custom.*;

@MyAnnotaion(name = "myServlet")
public class MyServlet {

    @MyAnnotationMethod(method = "post")
    public static void customPost(){
        System.out.println("customPost method");
    }
}
