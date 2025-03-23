package utils;

import java.util.Random;

public class Usernames {
    // Pool an beschreibenden Worten (Adjektiven) mit Großbuchstaben
    private static final String[] modifiers = {
            "Wühlige", "Schnelle", "Flauschige", "Witzige", "Mutige",
            "Tapsige", "Neugierige", "Elegante", "Schlaue", "Drollige", "Quirlige"
    };

    // Pool an Substantiven
    private static final String[] subjects = {
            "Wühlmaus", "Häschen", "Löwe", "Tiger", "Fuchs",
            "Bär", "Eule", "Dachs", "Frosch", "Igel", "Biber"
    };

    private static final Random rng = new Random();

    public static String generate() {
        String modifier = modifiers[rng.nextInt(modifiers.length)];
        String subject = subjects[rng.nextInt(subjects.length)];
        return modifier + " " + subject;
    }
}