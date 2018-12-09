package za.co.knonchalant.liketosee.dao;

import za.co.knonchalant.liketosee.domain.fightclub.Admin;
import za.co.knonchalant.liketosee.domain.fightclub.ReviewItem;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class AdminDAO {
    @PersistenceContext
    EntityManager em;

    public void persistSubmission(ReviewItem submission) {
        em.persist(submission);
    }


    public ReviewItem getReviewItemFor(int itemId) {
        TypedQuery<ReviewItem> query = em.createQuery("Select n from ReviewItem n where n.itemId=:id", ReviewItem.class);
        query.setParameter("id", itemId);
        List<ReviewItem> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    public boolean isAdmin(long userId) {
        return !getAdminsFor(userId).isEmpty();
    }

    private List<Admin> getAdminsFor(long userId) {
        TypedQuery<Admin> query = em.createQuery("select n from Admin n where n.userId = :userId", Admin.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public static AdminDAO get() {
        InitialContext ic;
        try {
            ic = new InitialContext();
            return (AdminDAO) ic.lookup("java:global/fightclub-web-1.0-SNAPSHOT/AdminDAO!za.co.knonchalant.liketosee.dao.AdminDAO");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<ReviewItem> getPendingReviews() {
        TypedQuery<ReviewItem> query = em.createQuery("Select n from ReviewItem n where n.reviewDate is null", ReviewItem.class);
        return query.getResultList();
    }
}
