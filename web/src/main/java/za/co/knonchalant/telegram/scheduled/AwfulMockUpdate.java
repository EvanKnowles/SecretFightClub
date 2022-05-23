package za.co.knonchalant.telegram.scheduled;

import za.co.knonchalant.candogram.domain.Location;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;

public class AwfulMockUpdate implements IUpdate {
    private final long chatId;
    private final User user;
    private String text = null;
    private String title;

    public AwfulMockUpdate(long chatId) {
        this(chatId, (User) null);
    }

    public AwfulMockUpdate(long chatId, String text) {
        this.chatId = chatId;
        this.user = null;
        this.text = text;
    }

    public AwfulMockUpdate(long chatId, User user) {
        this.chatId = chatId;
        this.user = user;
    }

    public AwfulMockUpdate(long chatId, User user, String title) {
        this.chatId = chatId;
        this.user = user;
        this.title = title;
    }

    public AwfulMockUpdate(long chatId, User user, String title, String text) {
        this.chatId = chatId;
        this.user = user;
        this.title = title;
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
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
        return title;
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
