package com.xmartlabs.template.helper.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Optional;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.xmartlabs.template.helper.function.Consumer;

import org.threeten.bp.Clock;
import org.threeten.bp.LocalDate;

@SuppressWarnings("unused")
public class DatePickerDialogHelper {
  /**
   * Creates a {@link DatePickerDialog} instance.
   *
   * @param listener to be triggered when the user selects a date
   * @param clock to get the current date
   * @return the {@link DatePickerDialog} created instance
   */
  @SuppressWarnings("unused")
  @NonNull
  public static DatePickerDialog createDialog(@Nullable Consumer<LocalDate> listener, @NonNull Clock clock) {
    return createDialog(null, listener, clock);
  }

  /**
   * Creates a {@link DatePickerDialog} instance with the <code>localDate</code> selected.
   *
   * @param localDate the selected start localDate
   * @param listener to be triggered when the user selects a localDate
   * @param clock to get the current date
   * @return the {@link DatePickerDialog} created instance
   */
  @NonNull
  public static DatePickerDialog createDialog(@Nullable LocalDate localDate, @Nullable Consumer<LocalDate> listener,
                                              @NonNull Clock clock) {
    localDate = Optional.ofNullable(localDate)
        .orElse(LocalDate.now(clock));

    DatePickerDialog.OnDateSetListener dialogCallBack = (view, year, monthOfYear, dayOfMonth) -> {
      LocalDate date = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
      Optional.ofNullable(listener)
          .ifPresent(theListener -> theListener.accept(date));
    };

    DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
        dialogCallBack,
        localDate.getYear(),
        localDate.getMonthValue() - 1,
        localDate.getDayOfMonth()
    );
    datePickerDialog.dismissOnPause(true);
    return datePickerDialog;
  }
}
