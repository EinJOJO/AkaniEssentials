package it.einjojo.akani.essentials.command;

public class TargetNotFoundException extends RuntimeException {
    private final String targetName;

    public TargetNotFoundException(String targetName) {
        super("Target not found: " + targetName);
        this.targetName = targetName;
    }

    public String targetName() {
        return targetName;
    }
}
