package com.example.houseagent.domain.model;

import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.Severity;

import java.util.List;

public record ConstraintIssue(
        IssueType type,
        String message,
        List<String> affectedRooms,
        Severity severity
) {
    public String fingerprint() {
        return type + ":" + message + ":" + affectedRooms;
    }
}
