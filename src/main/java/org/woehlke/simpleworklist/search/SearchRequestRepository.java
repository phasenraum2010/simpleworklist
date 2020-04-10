package org.woehlke.simpleworklist.search;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRequestRepository extends JpaRepository<SearchRequest, Long> {
}