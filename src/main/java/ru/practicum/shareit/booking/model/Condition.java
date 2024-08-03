package ru.practicum.shareit.booking.model;

public enum Condition {
    WAITING, // новое бронирование, ожидает одобрения
    APPROVED, // бронирование подтверждено владельцем
    CURRENT, // текущие бронирования
    REJECTED, // отклонена бронь владельцем вещи
    ALL, // все аренды
    PAST, // завершенные бронирования
    FUTURE // будущие бронирования
}
