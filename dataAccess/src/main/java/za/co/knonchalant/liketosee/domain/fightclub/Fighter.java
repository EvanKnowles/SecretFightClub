package za.co.knonchalant.liketosee.domain.fightclub;

import org.hibernate.annotations.GenericGenerator;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EClasses;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Created by evan on 2016/02/23.
 */
@Entity
public class Fighter {
    private long id;

    private String name;
    private long userId;
    private long chatId;
    private long wins;
    private EClasses type;
    private double health;
    private boolean inGame;

    public Fighter() {
    }

    public Fighter(String name, long userId, long chatId, EClasses type) {
        this.name = name;
        this.userId = userId;
        this.chatId = chatId;
        this.type = type;
        this.health = 100.0;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public EClasses getType() {
        return type;
    }

    public void setType(EClasses type) {
        this.type = type;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void revive() {
        setHealth(100);
    }

    public void damage(double damage) {
        health -= damage;
        if (health > 150) {
            health = 150;
        }
    }

    @Transient
    public boolean isDead() {
        return health <= 0;
    }

    public long getWins() {
        return wins;
    }

    public void setWins(long wins) {
        this.wins = wins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void win() {
        wins++;
        setHealth(100);
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void kill() {
        this.health = 0;
    }
}
