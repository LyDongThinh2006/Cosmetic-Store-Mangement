package com.thinh.cosmetic.domain.enums;

public enum ReturnStatus {
    REQUESTED,
    APPROVED,
    REJECTED,
    PROCESSING,
    COMPLETED;

    public boolean canTransitionTo(ReturnStatus next) {
        if (next == null) {
            return false;
        }
        if (this == next) {
            return true;
        }
        return switch (this) {
            case REQUESTED -> next == APPROVED || next == REJECTED;
            case APPROVED -> next == PROCESSING || next == COMPLETED || next == REJECTED;
            case PROCESSING -> next == COMPLETED || next == REJECTED;
            case COMPLETED, REJECTED -> false;
        };
    }
}

