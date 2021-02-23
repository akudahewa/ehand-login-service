package lk.dialog.loginservice.repository;

import lk.dialog.loginservice.model.Role;
import lk.dialog.loginservice.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role>  findByName(RoleName roleName);
}
