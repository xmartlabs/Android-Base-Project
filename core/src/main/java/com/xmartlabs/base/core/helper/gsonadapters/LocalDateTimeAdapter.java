package com.xmartlabs.base.core.helper.gsonadapters;

import android.support.annotation.Nullable;

import com.annimon.stream.Exceptional;
import com.annimon.stream.Optional;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.xmartlabs.base.core.helper.StringUtils;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;

import lombok.RequiredArgsConstructor;
import timber.log.Timber;

/** {@link Gson} type adapter that serializes {@link LocalDate} objects to any specified format. */
@RequiredArgsConstructor
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
  final DateTimeFormatter dateFormat;

  @Nullable
  @Override
  public synchronized JsonElement serialize(@Nullable LocalDateTime dateTime, @Nullable Type type,
                                            @Nullable JsonSerializationContext jsonSerializationContext) {
    return Optional.ofNullable(dateTime)
        .flatMap(localDateTime ->
            Exceptional.of(() -> new JsonPrimitive(dateFormat.format(localDateTime.atZone(ZoneOffset.UTC).toInstant())))
                .ifException(e -> Timber.e(e, "Date cannot be serialized, date='%s'", localDateTime.toString()))
                .getOptional())
        .orElse(null);
  }

  @Nullable
  public synchronized JsonElement serialize(@Nullable LocalDateTime dateTime) {
    return serialize(dateTime, null, null);
  }

  @Nullable
  @Override
  public synchronized LocalDateTime deserialize(@Nullable JsonElement jsonElement, @Nullable Type type,
                                                @Nullable JsonDeserializationContext jsonDeserializationContext) {
    return Optional.ofNullable(jsonElement)
        .map(JsonElement::toString)
        .filter(str -> !StringUtils.isNullOrEmpty(str))
        .flatMap(str ->
            Exceptional.of(() -> Instant.ofEpochMilli(Long.valueOf(str)).atZone(ZoneOffset.UTC).toLocalDateTime())
                .ifException(e -> Timber.e(e, "Date cannot be parsed, date='%s'", str))
                .getOptional())
        .orElse(null);
  }

  @Nullable
  public synchronized LocalDateTime deserialize(@Nullable JsonElement jsonElement) {
    return deserialize(jsonElement, null, null);
  }
}
