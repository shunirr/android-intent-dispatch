package jp.s5r.android.intentdispatch.model;

import lombok.Data;

import android.graphics.drawable.Drawable;

@Data
public class IntentModel {
  Drawable icon;
  CharSequence text;
  String packageName;
  String activityName;

  public IntentModel(Drawable icon, CharSequence text, String packageName, String activityName) {
    this.icon = icon;
    this.text = text;
    this.packageName = packageName;
    this.activityName = activityName;
  }
}
