package za.co.knonchalant.liketosee.dao;

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
import java.util.stream.Collectors;

/**
 * Created by evan on 2016/03/07.
 */
@Stateless
public class FighterDAO {
    @PersistenceContext
    EntityManager em;

    public void persistItem(Item item) {
        em.persist(item);
    }

    public void persistFighter(Fighter fighter) {
        em.persist(fighter);
    }

    public List<Item> getItemsCarriedBy(Long fighterId) {
        TypedQuery<Item> query = em.createQuery("Select n from Item n where n.fighterId = :user", Item.class);
        query.setParameter("user", fighterId);
        return query.getResultList();
    }

    public List<Item> findAllItems() {
        TypedQuery<Item> query = em.createQuery("Select n from Item n", Item.class);
        return query.getResultList();
    }

    public static FighterDAO get() {
        InitialContext ic;
        try {
            ic = new InitialContext();
            return (FighterDAO) ic.lookup("java:global/SecretFightClub/FighterDAO!za.co.knonchalant.liketosee.dao.FighterDAO");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Item> getAllUncarriedItems() {
        TypedQuery<Item> query = em.createQuery("Select n from Item n where n.fighterId is null", Item.class);
        return query.getResultList();
    }

    public void give(Item item, Fighter fighter) {
        em.detach(item);
        item.setId(null);
        item.setFighterId(fighter.getId());
        persistItem(item);
    }

    public Item findItem(int itemID) {
        TypedQuery<Item> query = em.createQuery("Select n from Item n where n.id = :id", Item.class);
        query.setParameter("id", itemID);
        List<Item> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    public void remove(Item item) {
        em.remove(item);
    }

    public List<Fighter> findFightersInClub(long clubId) {
        TypedQuery<Fighter> query = em.createQuery("Select f from Fighter f where f.clubId = :clubId ", Fighter.class);
        query.setParameter("clubId", clubId);
        return query.getResultList();
    }

    public List<Fighter> findAliveFightersInClub(long clubId) {
        TypedQuery<Fighter> query = em.createQuery("Select f from Fighter f where f.clubId = :clubId and f.health > 0", Fighter.class);
        query.setParameter("clubId", clubId);
        return query.getResultList();
    }

    public Fighter getFighter(long fighterId) {
        TypedQuery<Fighter> query = em.createQuery("Select n from Fighter n where n.id = :fighterId", Fighter.class);
        query.setParameter("fighterId", fighterId);

        List<Fighter> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }

        return resultList.get(0);
    }

    public Fighter getFighterByUserId(long userId) {
        TypedQuery<Fighter> query = em.createQuery("Select n from Fighter n where n.userId = :id", Fighter.class);
        query.setParameter("id", userId);

        List<Fighter> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }

        return resultList.get(0);
    }

    public List<Item> getAllUncarriedItemsFrom(long chatId) {
        TypedQuery<Item> query = em.createQuery("Select n from Item n where n.fighterId is null and n.chatId = :chatId", Item.class);
        query.setParameter("chatId", chatId);
        return query.getResultList();
    }

    public List<Item> getAllUncarriedItemsFor(long chatId) {
        TypedQuery<Item> query = em.createQuery("Select n from Item n where n.fighterId is null", Item.class);
        // temporarily hacking out club vs global items
//        query.setParameter("chatId", chatId);
        return query.getResultList();
    }

    public Item getItem(Integer itemId) {
        TypedQuery<Item> query = em.createQuery("Select n from Item n where n.id = :itemId", Item.class);
        query.setParameter("itemId", itemId);

        List<Item> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }

        return resultList.get(0);
    }

    public boolean removeItem(int id) {
        Query query = em.createQuery("delete from Item n where n.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate() == 1;
    }

    public List<Fighter> findAliveFightersInRoom(long chatId) {
        TypedQuery<Fighter> query = em.createQuery("select n from Fighter n where n.userId = :id", Fighter.class);
        query.setParameter("id", chatId);
        List<Fighter> resultList = query.getResultList();

        Fighter fighter = resultList.get(0);
        return fighter.getClub().getFighters().stream().filter(f -> !f.isDead()).collect(Collectors.toList());
    }
}
