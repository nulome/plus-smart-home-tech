package ru.test.collect.model.scenario;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScenarioCondition {
    private int sensorId;
    private ScenarioType type;
    private OperationType operation;
    private int value;
}
