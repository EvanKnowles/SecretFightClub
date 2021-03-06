package za.co.knonchalant.liketosee.domain.fightclub;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;

/**
 * Created by evan on 2016/02/23.
 */
@Entity
public class Item {
    private Integer id;

    private Long fighterId;
    private Long chatId;

    private String name;
    private double damage;
    private EDamageType damageType;

    private String attackText;

    public Item() {
    }

    public Item(String name, double damage, EDamageType damageType, String attackText, long chatId) {
        this.name = name;
        this.damage = damage;
        this.damageType = damageType;
        this.attackText = attackText;
        this.chatId = chatId;
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

    public Long getFighterId() {
        return fighterId;
    }

    public void setFighterId(Long userId) {
        this.fighterId = userId;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttackText() {
        return attackText;
    }

    public void setAttackText(String attackText) {
        this.attackText = attackText;
    }

    public EDamageType getDamageType() {
        return damageType;
    }

    public void setDamageType(EDamageType damageType) {
        this.damageType = damageType;
    }

    public String format(String attacker, String defender) {
        return attackText.replaceAll("\\{\\{a}}", attacker)
                .replaceAll("\\{\\{d}}", defender);
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}