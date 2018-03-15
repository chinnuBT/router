import annotations.custom.MyController;
import annotations.custom.MyHttpMethod;
import litew8.MyMerchantServlet;
import litew8.MyUserServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MyAnnoDrivenJetty {

    /*
    1 . processAnnotations
    2 . GatherAnnotation in List<Object>
    3 . Set in initContext
    */

    public void drive(){
        processAnnotations();
    }

    private void processAnnotations() {
        List<Class> classList = new ArrayList<>();
        classList.add(MyUserServlet.class);//TODO - get a way to find the list of classes with a specific annotation. - look at org.reflections
        classList.add(MyMerchantServlet.class);

        List<CustomController> customControllers = processAnnotationsForClass(classList);

        initServer(customControllers); //initializing Servers
    }

    private void initServer(List<CustomController> customControllers) {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/"); //base path
        //context.addServlet(HelloServlet.class, "/hello"); //servlet path
        //context.addServlet("jetty.HelloServlet","/hello");
        initContext(context,customControllers);

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
        server.setHandler(handlers);

        try {
            server.start();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<CustomController> processAnnotationsForClass(List<Class> classList) {
        List<CustomController> customControllers = new ArrayList<>();
        for (Class aClass : classList) {
            CustomController customController = new CustomController();
            //validate if customServlet has correct annotation
            Annotation myAnnotaion = aClass.getAnnotation(MyController.class);
            if (myAnnotaion != null) {
                MyController myControllerAnnotation = (MyController) myAnnotaion;
                System.out.println(myAnnotaion);
                customController.setaClass(aClass); // set Class
                customController.setControllerPath(myControllerAnnotation.value());//set Path

                processMethodsForClass(aClass, customController);

            }
            System.out.println("customController -->" + customController);
            customControllers.add(customController);
        }

        return customControllers;

    }

    private void processMethodsForClass(Class aClass, CustomController customController) {

        Arrays.asList(aClass.getMethods())
                .stream()
                .filter(this::validMethod)
                .forEach(method -> {
                    MyHttpMethod myHttpMethod = method.getAnnotation(MyHttpMethod.class);
                    if(myHttpMethod!=null){
                        customController.getMethods().put(myHttpMethod.method(),method);
                    }
                });

    }

    private boolean validMethod(Method method) {
       return method.getAnnotationsByType(MyHttpMethod.class).length > 0;
    }

    private void initContext(ServletContextHandler context, List<CustomController> customControllers){

        for (CustomController customController: customControllers) {
            Class customControllerClass = customController.getaClass();
            try {
                Object  controllerObject = customControllerClass.newInstance();
                System.out.println("init --> customController --> "+ customController);
            context.addServlet(new ServletHolder(new HttpServlet() {
                @Override
                protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    Method method = customController.getMethods().get("get");
                    if(method!=null){
                        try {
                            method.invoke(controllerObject,null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }else {
                        super.doGet(req, resp);
                    }
                }

                @Override
                protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    Method method = customController.getMethods().get("post");
                    if(method!=null){
                        try {
                            method.invoke(controllerObject,null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }else {
                        super.doPost(req, resp);
                    }

                }

                @Override
                protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    Method method = customController.getMethods().get("put");
                    if(method!=null){
                        try {
                            method.invoke(controllerObject,null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }else {
                        super.doPut(req, resp);
                    }
                }

                @Override
                protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    Method method = customController.getMethods().get("delete");
                    if(method!=null){
                        try {
                            method.invoke(controllerObject,null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }else {
                        super.doDelete(req, resp);
                    }
                }
            }),customController.getControllerPath());

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
}

class CustomController{
    private Class aClass;
    private String controllerPath;
    private Map<String, Method> methods = new HashMap<>();

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public String getControllerPath() {
        return controllerPath;
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, Method> methods) {
        this.methods = methods;
    }

    @Override
    public String toString() {
        return "CustomController{" +
                "aClass=" + aClass +
                ", controllerPath='" + controllerPath + '\'' +
                ", methods=" + methods +
                '}';
    }
}
