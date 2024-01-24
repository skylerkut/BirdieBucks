package coms309.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendGroupRepository extends JpaRepository<FriendGroup, Integer> {

    FriendGroup findBygroupName(String groupName);





}