package no.scienta.plantilla.health;

import com.codahale.metrics.health.HealthCheck;

/**
 *
 */
public class PlantillaHealth extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
