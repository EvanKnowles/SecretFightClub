package za.co.knonchalant.liketosee.util;

public abstract class StringPrettifier {

  private static final String[] VOWELS = {"a", "e", "i", "o", "u"};

  private StringPrettifier() {
  }

  public static String prettify(String name)
  {
    String nameLower = name.toLowerCase();
    if (nameLower.startsWith("the ") || nameLower.startsWith("an ") || nameLower.startsWith("a ")) {
      // if the name includes a prefix of 'a' or 'an' or 'the', then use it as-is
      return name;
    }
    for (String vowel : VOWELS)
    {
      if (nameLower.startsWith(vowel)) {
        return "an " + name;
      }
    }
    return "a " + name;
  }
}
