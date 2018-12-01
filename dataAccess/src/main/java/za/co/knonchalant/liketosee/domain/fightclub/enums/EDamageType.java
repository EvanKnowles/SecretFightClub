package za.co.knonchalant.liketosee.domain.fightclub.enums;

public enum EDamageType
{
  ATTACK("attack", "normal damage"), SPLASH_ATTACK("attack-all", "damage inflicted on everyone");

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
