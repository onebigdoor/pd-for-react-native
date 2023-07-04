package com.pd;

import android.util.Log;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.puredata.core.PdBase;

public class PureDataModule extends ReactContextBaseJavaModule {
  private static final String TAG = "PureDataModule";

  // constructor
  public PureDataModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }
  // Mandatory function getName that specifies the module name
  @Override
  public String getName() {
    return "PureDataModule";
  }
  // Custom function that we are going to export to JS
  @ReactMethod
  public void sendFloat(String name, double value, Callback cb) {
    try {
      int rc = PdBase.sendFloat(name, (float) value);
      cb.invoke(null,
                String.format("Name: %s; Value: %f; rc: %d", name, value, rc));
    } catch (Exception e) {
      cb.invoke(e.toString(), null);
    }
  }

  @ReactMethod
  public void sendList(String name, ReadableArray value, Callback cb) {
    try {
      Double[] arr = value.toArrayList().toArray(new Double[0]);
      int rc = PdBase.sendList(name, arr);
      cb.invoke(null,
                String.format("Name: %s; %s; rc: %d", name, Arrays.toString(arr), rc));
    } catch (Exception e) {
      cb.invoke(e.toString(), null);
    }
  }

  @ReactMethod
  public void loadPatch(String name, Callback cb) {
    try {
      File dir = getCurrentActivity().getFilesDir();
      if (name == null || name.isEmpty()) {
        Log.e(TAG, "Error loading pd patch: patch name is required");
        return;
      }
      File pdPatch = new File(dir, "patches/" + name);
      int patch = PdBase.openPatch(pdPatch.getAbsolutePath());
      Log.i(TAG, "Finished loading pd patch " + patch);
    } catch (IOException e) {
      Log.e(TAG, "Error loading pd patch: " + e.getMessage());
      if (cb != null) {
        cb.invoke(e.toString(), null);
      }
    }
  }
}
