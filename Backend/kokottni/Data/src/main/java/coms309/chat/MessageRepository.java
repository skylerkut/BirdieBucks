package coms309.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{

    List<Message> findByTarget(String target);

}
