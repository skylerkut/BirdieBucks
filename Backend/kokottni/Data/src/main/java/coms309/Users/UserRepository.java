package coms309.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long>{
    User findById(long id);
//
//    void deleteById(int id);
//
//    void deleteByName(String name);
//
//    User findByStockId(int id);
//
    User findByUsername(String username);

}
