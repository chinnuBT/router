package litew8;

import annotations.custom.MyController;
import annotations.custom.MyHttpMethod;

@MyController("/user")
public class MyUserServlet {

    @MyHttpMethod(method = "post")
    public void addUser(){
        System.out.println("POST-ing User");
    }

    @MyHttpMethod(method = "get")
    public void getUser(){
        System.out.println("GET-ting User");
    }

    @MyHttpMethod(method = "put")
    public void updateUser(){
        System.out.println("PUT-ting User ");
    }

    @MyHttpMethod(method = "delete")
    public void deleteUser(){
        System.out.println("DELETE-ing added");
    }
}
