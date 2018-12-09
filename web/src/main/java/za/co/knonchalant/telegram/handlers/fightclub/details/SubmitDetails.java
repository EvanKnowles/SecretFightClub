package za.co.knonchalant.telegram.handlers.fightclub.details;

import za.co.knonchalant.candogram.domain.BaseDetail;

public class SubmitDetails extends BaseDetail {
    int itemId;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
