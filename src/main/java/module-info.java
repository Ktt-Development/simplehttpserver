module simplehttpserver {

    requires jdk.httpserver;
    requires java.net.http; // test requirement
    exports com.kttdevelopment.simplehttpserver.handler;
    exports com.kttdevelopment.simplehttpserver.var;
    exports com.kttdevelopment.simplehttpserver;

}