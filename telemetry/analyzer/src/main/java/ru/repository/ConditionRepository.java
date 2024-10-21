package ru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.repository.model.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
