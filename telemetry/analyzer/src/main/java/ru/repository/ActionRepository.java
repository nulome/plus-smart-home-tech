package ru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.repository.model.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
}
