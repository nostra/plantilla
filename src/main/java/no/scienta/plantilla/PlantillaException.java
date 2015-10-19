package no.scienta.plantilla;

/**
 *
 */
public class PlantillaException extends RuntimeException {

    public PlantillaException(String reason) {
        super(reason);
    }
    public PlantillaException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
