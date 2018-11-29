package za.co.knonchalant.telegram.scheduled.info;

import java.io.Serializable;

public class RestartGameInfo implements Serializable {
    private long chatId;

    public RestartGameInfo(long chatId) {
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }
}
