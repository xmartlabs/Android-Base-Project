package com.xmartlabs.template.helper.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.annimon.stream.Stream;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.xmartlabs.template.R;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class UiHelper {
  /**
   * Displays an alert if the Google Play Services are not enabled
   * @param activity the activity to use to check the availability of the Google Play Services
   * @return true if the Google Play Services are available
   */
  public static boolean checkGooglePlayServicesAndShowAlertIfNeeded(@NonNull Activity activity) {
    int googlePlayServicesStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);

    if (googlePlayServicesStatus == ConnectionResult.SUCCESS) {
      return true;
    }

    if (GoogleApiAvailability.getInstance().isUserResolvableError(googlePlayServicesStatus)) {
      GoogleApiAvailability.getInstance().getErrorDialog(activity, googlePlayServicesStatus, 0).show();
    }

    return false;
  }

  /**
   * Retrieves the object in the {@code adapter} at the {@code spinner} position
   * @param spinner the spinner to retrieve the position from
   * @param adapter the adapter to retrieve the object from
   * @param <T> the type of the object held by the adapter
   * @return an instance of type T at the spinner position
   */
  @NonNull
  @SuppressWarnings("unused")
  public static <T> T getSpinnerValue(@NonNull Spinner spinner, @NonNull ArrayAdapter<T> adapter) {
    int selectedPosition = spinner.getSelectedItemPosition();
    return adapter.getItem(selectedPosition);
  }

  /**
   * Retrieves the URL of the drawable with {@code drawableResId} id in the given {@code context}
   * @param context the context from which to retrieve the resource
   * @param drawableResId the id of the drawable resource
   * @return the URL of the drawable
   */
  @NonNull
  public static String getUrlForDrawable(Context context, @DrawableRes int drawableResId) {
    return String.format(
        Locale.US,
        "%s://%s/%s/%s",
        ContentResolver.SCHEME_ANDROID_RESOURCE,
        context.getResources().getResourcePackageName(drawableResId),
        context.getResources().getResourceTypeName(drawableResId),
        context.getResources().getResourceEntryName(drawableResId)
    );
  }

  /**
   * Validates that the field is not empty and, if it's an email field, validates that the email given is valid.
   * A field is said to be an email field if it's input type is TYPE_TEXT_VARIATION_EMAIL_ADDRESS
   * @param editText the {@link EditText} instance to validate
   * @return true if the field has content and, in case of an email field, that the content matches a valid email
   */
  public static boolean isValidField(EditText editText) {
    return !editText.getText().toString().isEmpty()
        && (!isEmailField(editText) || isValidEmail(editText.getText()));
  }

  /**
   * Checks whether the given <code>EditText</code> is an email field or not
   * @param editText the {@link EditText} to check
   * @return true if the {@link EditText} input type is TYPE_TEXT_VARIATION_EMAIL_ADDRESS
   */
  public static boolean isEmailField(EditText editText) {
    return (editText.getInputType() & InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) ==
        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
  }

  /**
   * Checks whether the given {@link CharSequence} is a valid email address
   * @param target the {@link CharSequence} to check
   * @return true if the {@code target} is a valid email
   */
  public static boolean isValidEmail(CharSequence target) {
    return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
  }

  /**
   * Retrieves the {@link TextInputLayout} of the given {@code editText}
   * @param editText the {@link EditText} instance from which the {@link TextInputLayout} will be retrieved
   * @return the {@link TextInputLayout} of the {@code editText}
   */
  @Nullable
  public static TextInputLayout getTextInputLayout(EditText editText) {
    if (editText.getParent() instanceof TextInputLayout) {
      return (TextInputLayout) editText.getParent();
    } else if (editText.getParent().getParent() instanceof TextInputLayout) {
      return (TextInputLayout) editText.getParent().getParent();
    }
    return null;
  }

  /**
   * Validates that the given {@code fields} and put the appropriate error messages if needed
   * The method {@link #validateField(EditText, boolean)} is applied to each of the {@code fields}
   * @param fields the fields to validate
   * @param hideErrorLayout whether or not to enable the error on the fields
   */
  public static void validateFieldsAndPutErrorMessages(List<EditText> fields, boolean hideErrorLayout) {
    Stream.of(fields)
        .forEach(field -> UiHelper.validateField(field, hideErrorLayout));
  }

  /**
   * Validates that {@code editText} is valid and sets the error in the {@link TextInputLayout}, if any
   * @param editText the {@link EditText} to validate
   * @param hideErrorLayout whether or not to enable the error on the given {@link EditText}
   * @return true if {@code editText} is valid using {@link #isValidField(EditText)}
   */
  public static boolean validateField(EditText editText, boolean hideErrorLayout) {
    boolean isValid = UiHelper.isValidField(editText);
    TextInputLayout textInputLayout = UiHelper.getTextInputLayout(editText);
    if (textInputLayout != null) {
      if (isValid) {
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(!hideErrorLayout);
      } else {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(UiHelper.isEmailField(editText)
            ? editText.getResources().getString(R.string.the_email_not_valid)
            : editText.getResources().getString(R.string.field_required)
        );
      }
    }
    return isValid;
  }
}
