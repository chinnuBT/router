package litew8;

import annotations.custom.MyController;
import annotations.custom.MyHttpMethod;

@MyController("/merchant")
public class MyMerchantServlet {

    @MyHttpMethod(method = "post")
    public void addMerchant(){
        System.out.println("POST-ing merchant");
    }

    @MyHttpMethod(method = "get")
    public void getMerchant(){
        System.out.println("GET-ting merchant");
    }

    @MyHttpMethod(method = "put")
    public void updateMerchant(){
        System.out.println("PUT-ting merchant ");
    }

    @MyHttpMethod(method = "delete")
    public void deleteMerchant(){
        System.out.println("DELETE-ing merchant");
    }

}
