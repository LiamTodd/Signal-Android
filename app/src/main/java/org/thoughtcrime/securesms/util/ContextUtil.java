package org.thoughtcrime.securesms.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.Objects;

public final class ContextUtil {
  private ContextUtil() {}

  public static @NonNull Drawable requireDrawable(@NonNull Context context, @DrawableRes int drawable) {
    return Objects.requireNonNull(AppCompatResources.getDrawable(context, drawable));
  }

}
