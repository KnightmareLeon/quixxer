package io.github.knightmareleon.shared.utils;

import java.util.List;

public class Result<T> {

    private final T value;
    private final List<String> errorMessages;

    private Result(T value, List<String> errorMessages) {
        this.value = value;
        this.errorMessages = errorMessages;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(null, List.of(message));
    }

    public static <T> Result<T> error(List<String> messages) {
        return new Result<>(null, messages);
    }

    public boolean isSuccess() {
        return this.errorMessages == null || this.errorMessages.isEmpty();
    }

    public T getValue() {
        return this.value;
    }

    public List<String> getErrorMessages() {
        return this.errorMessages;
    }
}
