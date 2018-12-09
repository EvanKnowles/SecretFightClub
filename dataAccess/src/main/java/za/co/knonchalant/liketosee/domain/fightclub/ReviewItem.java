package za.co.knonchalant.liketosee.domain.fightclub;

import org.hibernate.annotations.GenericGenerator;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EApprovalStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by evan on 2016/02/23.
 */
@Entity
public class ReviewItem {
    private Integer id;

    private Integer itemId;
    private String itemName;

    private EApprovalStatus status = EApprovalStatus.PENDING;
    private String reviewedByName;
    private long reviewedById;
    private String reviewComment;
    private Date reviewDate;

    public ReviewItem(Integer itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public ReviewItem() {
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(EApprovalStatus status) {
        this.status = status;
    }

    public String getReviewedByName() {
        return reviewedByName;
    }

    public void setReviewedByName(String reviewedBy) {
        this.reviewedByName = reviewedBy;
    }

    public long getReviewedById() {
        return reviewedById;
    }

    public void setReviewedById(long reviewedByName) {
        this.reviewedById = reviewedByName;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}