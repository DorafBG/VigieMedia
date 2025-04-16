package Vigie;
import Evenement.Evenement;
import Evenement.IEvenement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Alerte {
    private final String message;
    private final String type;
    private final LocalDateTime date;
    private final IEvenement evenementSource;
    private final String sourceModule;

    public Alerte(String message, String type, IEvenement evenementSource, String sourceModule) {
        this.message = message;
        this.type = type;
        this.date = LocalDateTime.now();
        this.evenementSource = evenementSource;
        this.sourceModule = sourceModule;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public IEvenement getEvenementSource() {
        return evenementSource;
    }

    public String getSourceModule() {
        return sourceModule;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'à' HH'h'mm", Locale.FRENCH);
        String dateFormatee = date.format(formatter);
        return "Date: " + dateFormatee + "\n" +
                "Type: " + type + "\n" +
                "Source: " + sourceModule + "\n" +
                "Message: " + message;
    }
}