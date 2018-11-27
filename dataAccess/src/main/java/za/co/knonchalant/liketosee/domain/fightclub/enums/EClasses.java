package za.co.knonchalant.liketosee.domain.fightclub.enums;

public enum EClasses {

    OYSTER("Oyster"), MATH("Math"), GYM("Gym"), CODE("Codez0ring"), BOURGEOIS("Bourgeois");

    private final String name;

    EClasses(String name) {
        this.name = name;
    }

    public static EClasses fromName(String name) {
        for (EClasses value : EClasses.values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
