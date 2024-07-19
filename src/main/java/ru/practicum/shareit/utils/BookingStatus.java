package ru.practicum.shareit.utils;

public enum BookingStatus {
    WAITING, //новое бронирование, ожидает одобрения
    APPROVED, //бронирование подтверждено владельцем
    REJECTED, //бронирование отклонено владельцем
    CANCELLED,//бронирование отменено создателем
}