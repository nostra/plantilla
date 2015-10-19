package no.scienta.plantilla.views;

import io.dropwizard.views.View;

import java.nio.charset.Charset;
import java.util.List;

/**
 *
 */
public class GeneralView extends View {

    private final List<String> lines;

    public GeneralView(String template, List<String> names) {
        super(template, Charset.forName("UTF-8"));
        lines = names;
    }

    public GeneralView(String template) {
        this(template, null);
    }

    public List<String> getLines() {
        return lines;
    }
}
