package util;

public class idUserGenerator {
    private static int id = 0;

    public static int getId() {
        return ++id;
    }
}