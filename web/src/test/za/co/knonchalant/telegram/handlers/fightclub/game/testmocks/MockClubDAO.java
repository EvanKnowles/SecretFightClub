package za.co.knonchalant.telegram.handlers.fightclub.game.testmocks;

import za.co.knonchalant.liketosee.dao.ClubDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Club;

import java.util.HashSet;
import java.util.Set;

public class MockClubDAO extends ClubDAO {

    Set<Club> clubs = new HashSet<>();

    @Override
    public Club getClub(long clubId) {
        for (Club club : clubs) {
            if (club.getId().equals(clubId)) {
                return club;
            }
        }

        return null;
    }

    @Override
    public Club findClub(String joinCode) {
        for (Club club : clubs) {
            if (club.getJoinCode().equals(joinCode)) {
                return club;
            }
        }

        return null;
    }

    @Override
    public void persistClub(Club club) {
        clubs.add(club);
    }
}
