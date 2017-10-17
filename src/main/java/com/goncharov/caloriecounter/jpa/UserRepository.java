package com.goncharov.caloriecounter.jpa;

import com.goncharov.caloriecounter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Anton Goncharov
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);

}
