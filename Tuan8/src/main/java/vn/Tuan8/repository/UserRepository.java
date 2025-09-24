package vn.Tuan8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.Tuan8.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
