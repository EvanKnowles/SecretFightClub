package za.co.knonchalant.telegram.handlers;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;

import java.util.Arrays;
import java.util.List;

public class AbuseHandler extends BaseMessageHandler {

    public static final String COMMAND = "abuse";

    private static final List<String> INSULTS = Arrays.asList(
   "This is the END for you, you gutter crawling cur %s!",
           "I've spoken with apes more polite than you %s!",
           " %s, soon you'll be wearing my sword like a shish kebab!",
           "People fall at my feet when they see me coming %s!",
           "%s, I'm not going to take your insolence sitting down!",
           "I once owned a dog that was smarter than you %s.",
           "%s, nobody's ever drawn blood from me and nobody ever will.",
           "Have you stopped wearing diapers yet %s?",
           "There are no words for how disgusting you are %s.",
           "You make me want to puke %s.",
           "My handkerchief will wipe up your blood %s!",
           "I got this scar on my face during a mighty struggle %s!",
           "I've heard you are a contemptible sneak %s.",
           "You're no match for my brains %s, you poor fool.",
           "You have the manners of a beggar %s.",
           "Now I know what filth and stupidity really are - %s.",
           "Every word you say to me is stupid %s.",
           "I've got a long, sharp lesson for you to learn today %s.",
           "I will milk every drop of blood from %s's body!",
           "I've got the courage and skill of a master swordsman %s.",
           "%s, my tongue is sharper than any sword",
           "%s, my name is feared in every dirty corner of this island!",
           "%s, my wisest enemies run away at the first sight of me!",
           "Only once have I met such a coward %s!",
           "No one will ever catch ME fighting as badly as you do %s.",
           "My last fight ended with my hands covered with blood %s.",
           "I hope you have a boat ready for a quick escape %s.",
           "%s, my sword is famous all over the Caribbean!",
           "You, %s,  are a pain in the backside, sir!",
           "I usually see people like you passed-out on tavern floor %s.",
           "There are no clever moves that can help you now %s.",
           "Every enemy I've met I've annihilated %s!",
           "%s, you're as repulsive as a monkey in a negligee.",
           "Killing %s would be justifiable homicide!",
           "%s, you're the ugliest monster ever created!",
           "I'll skewer you like a sow at a buffet %s!",
           "Would you like to be buried, or cremated %s?",
           "Coming face to face with me must leave you petrified %s!",
           "%s, when your father first saw you, he must have been mortified!",
           "You can't match my witty repartee %s!",
           "%s, I have never seen such clumsy swordplay!",
           "%s! En garde! Touché!",
           "I'll leave you devasted, mutilated, and perforated %s!",
           "Heaven preserve me! %s, you look like something that's died!",
           "I'll hound you night and day %s!");

    public AbuseHandler(String botName, IBotAPI bot) {
        super(botName, COMMAND, bot);
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        String text = update.getText();
        String keyword = getKeywords(text, COMMAND);

        String insult = String.format(INSULTS.get((int) Math.floor(Math.random() * INSULTS.size())), keyword);

        sendMessage(update, insult);

        return null;
    }

    @Override
    public String getDescription() {
        return "Hurl abuse at X";
    }
}
