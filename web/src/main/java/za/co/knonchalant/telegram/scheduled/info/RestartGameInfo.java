package za.co.knonchalant.telegram.scheduled.info;

import java.io.Serializable;

public class RestartGameInfo implements Serializable {
    private long clubId;

    public RestartGameInfo(long clubId) {
        this.clubId = clubId;
    }

    public long getClubId() {
        return clubId;
    }
}
