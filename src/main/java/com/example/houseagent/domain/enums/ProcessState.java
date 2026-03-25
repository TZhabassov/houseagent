package com.example.houseagent.domain.enums;

public enum ProcessState {
    INIT,
    COLLECT_REQUIREMENTS,
    VALIDATE_REQUIREMENTS,
    CLARIFY_REQUIREMENTS,
    GENERATE_LAYOUT,
    VALIDATE_LAYOUT,
    DONE,
    NEEDS_CLARIFICATION,
    ABORTED
}
