package HYU.FishShip.Core.Repository;

import HYU.FishShip.Core.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);
    boolean existsByLoginId(String loginId);
    User findByLoginId(String loginId);
    User findByEmail(String email);
    User findByPhone(String phone);
}
