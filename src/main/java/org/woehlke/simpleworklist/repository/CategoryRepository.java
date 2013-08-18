package org.woehlke.simpleworklist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIsNullAndUserAccount(UserAccount userAccount);

    List<Category> findByUserAccount(UserAccount userAccount);
}
