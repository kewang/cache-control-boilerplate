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

/**
 * Main class.
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getSimpleName());

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/myapp/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in tw.kewang package
        final ResourceConfig rc = new ResourceConfig().packages("tw.kewang");

        rc.register(new LoggingFeature(LOG, Level.INFO, Verbosity.PAYLOAD_ANY, null));

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     *
     * @param args
     */
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