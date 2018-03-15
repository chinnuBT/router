import annotations.custom.MyAnnotaion;
import annotations.custom.MyAnnotationMethod;
import litew8.MyServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import jetty.HelloServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import sun.reflect.Reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

//TODO - replace "get" with HTTP_METHODS.GET enum
public class Main {

    public static void main(String[] args){

        //initServer();

        //processAnnotations();

        MyAnnoDrivenJetty myAnnoDrivenJetty = new MyAnnoDrivenJetty();
        myAnnoDrivenJetty.drive();


    }

    private static void processAnnotations() {
        //simpleAnnoTest();
        simpleTest();

    }

    private static void simpleAnnoTest() {
        Class myClass = MyServlet.class;
        Method method = null;
        try {
            method = myClass.getMethod("customPost", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        MyAnnotationMethod[] arr = method.getAnnotationsByType(MyAnnotationMethod.class);
        System.out.println(arr.length);
        try {
            method.invoke(myClass.newInstance(), null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
//        method.invoke(null, null); // can be used when method is static
    }

    private static void simpleTest(){
        Class myServletClass = MyServlet.class;
        Annotation annotation = myServletClass.getAnnotation(MyAnnotaion.class);
        System.out.println(annotation.annotationType() + " -- " + (annotation instanceof MyAnnotaion));
        MyAnnotaion myAnnotaion = (MyAnnotaion)annotation;
        System.out.println(myAnnotaion.name());

        try {
            MyServlet myServlet = (MyServlet)myServletClass.newInstance();

            Arrays.asList(myServletClass.getMethods())
                    .stream()
                    .filter(method -> {
                        //System.out.println(method.getName() + "--" + method.getAnnotationsByType(MyAnnotationMethod.class).length);
                        return method.getAnnotationsByType(MyAnnotationMethod.class).length > 0;
                    })
                    .forEach(method -> {
                        try {
                            method.invoke(myServlet,null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private static void initServer() throws Exception{
        System.out.println("Hello World!");

        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/"); //base path
        //context.addServlet(HelloServlet.class, "/hello"); //servlet path
        //context.addServlet("jetty.HelloServlet","/hello");
        initContext(context);

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
        server.setHandler(handlers);


        server.start();
        server.join();
    }

    private static void initContext(ServletContextHandler contextHandler){
        String className = "jetty.HelloServlet";
        String servletPath = "/hello";
        contextHandler.addServlet(className,servletPath);

        contextHandler.addServlet(new ServletHolder(),"");
    }

}
