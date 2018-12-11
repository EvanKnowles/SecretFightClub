package za.co.knonchalant.liketosee.domain.fightclub.enums;

public enum EDamageType
{
  ATTACK("attack", "normal damage"),
  ATTACK_ALL("attack-all", "damage inflicted on everyone"),
  SPLASH_ATTACK("attack-splash", "damage inflicted on everyone else"),
  SILENCE("silence", "silences the player who holds it");

  private final String name;
  private final String description;

  EDamageType(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public static EDamageType fromName(String name) {
    for (EDamageType value : EDamageType.values()) {
      if (value.getName().equalsIgnoreCase(name)) {
        return value;
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
