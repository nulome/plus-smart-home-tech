package ru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.repository.model.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
