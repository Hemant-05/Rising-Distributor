package com.rising.Distributor.repository;
import com.rising.Distributor.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByNumber(String number);
}
