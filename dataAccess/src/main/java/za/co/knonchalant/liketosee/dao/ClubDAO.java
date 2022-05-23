package za.co.knonchalant.liketosee.dao;

import za.co.knonchalant.liketosee.domain.fightclub.Club;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by evan on 2016/03/07.
 */
@Stateless
public class ClubDAO {
    @PersistenceContext
    EntityManager em;

    public Club getClub(long clubId) {
        TypedQuery<Club> query = em.createQuery("Select n from Club n where n.id = :clubId", Club.class);
        query.setParameter("clubId", clubId);
        return query.getSingleResult();
    }

    public static ClubDAO get() {
        InitialContext ic;
        try {
            ic = new InitialContext();
            return (ClubDAO) ic.lookup("java:global/fightclub-web-1.0-SNAPSHOT/FighterDAO!za.co.knonchalant.liketosee.dao.FighterDAO");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
