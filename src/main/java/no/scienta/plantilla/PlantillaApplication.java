package no.scienta.plantilla;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import no.scienta.plantilla.health.PlantillaHealth;
import no.scienta.plantilla.resources.IndexResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class PlantillaApplication extends Application<PlantillaConfiguration> {
    private static Logger log = LoggerFactory.getLogger(PlantillaApplication.class);

    public static void main(String[] args) throws Exception { // NOSONAR
        if ( args.length == 0 ) {
            log.warn("Staring without arguments assumes dev-mode, and dev mode config");
            new PlantillaApplication().run("server", "dropwizard.yml");
        } else {
            new PlantillaApplication().run(args);
        }
    }

    @Override
    public void initialize(Bootstrap<PlantillaConfiguration> bootstrap) {
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle( new AssetsBundle("/assets/favicon.ico", "/favicon.ico", null, "favicon.ico"));
        bootstrap.addBundle(new AssetsBundle("/assets/images", "/images", null, "images"));
        bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "css"));
    }

    @Override
    public void run(PlantillaConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new PlantillaExceptionMapper());

        environment.jersey().register(new IndexResource());
        environment.healthChecks().register("Plantilla", new PlantillaHealth());

        final Graphite graphite = new Graphite(new InetSocketAddress("localhost", 2003));
        final GraphiteReporter reporter = GraphiteReporter.forRegistry(environment.metrics())
                .prefixedWith("plantilla")
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build(graphite);
        reporter.start(1, TimeUnit.MINUTES);

        log.warn("Started...");
    }

}