package za.co.knonchalant.telegram.scheduled;

import za.co.knonchalant.candogram.domain.Location;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;

public class AwfulMockUpdate implements IUpdate {
    private long chatId;
    private User user;

    public AwfulMockUpdate(long chatId) {
        this(chatId, null);
    }

    public AwfulMockUpdate(long chatId, User user) {
        this.chatId = chatId;
        this.user = user;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public User getOtherUser() {
        return null;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String getInlineId() {
        return null;
    }

    @Override
    public long getMessageId() {
        return 0;
    }

    @Override
    public long getChatId() {
        return chatId;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public boolean isInline() {
        return false;
    }

    @Override
    public boolean skip() {
        return false;
    }
}
