package tw.kewang;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.logging.LoggingFeature.Verbosity;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getSimpleName());
    private static final String BASE_URI = "http://localhost:8080/api/";

    private static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("tw.kewang");

        rc.register(new LoggingFeature(LOG, Level.INFO, Verbosity.PAYLOAD_ANY, null));

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        HttpServer server = startServer();

        System.out.println(String.format("Jersey app started with WADL available at %sapplication.wadl\nPress 'stop' to stop it...", BASE_URI));

        String ret = "";

        Scanner scanner = new Scanner(System.in);

        while (!ret.equalsIgnoreCase("stop")) {
            ret = scanner.next();
        }

        server.stop();
    }
}