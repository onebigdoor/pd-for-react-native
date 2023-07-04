package com.pd;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactActivityDelegate;
import java.io.File;
import java.io.IOException;
import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

public class MainActivity extends ReactActivity {
  private static final String TAG = "MainActivity";
  private PdUiDispatcher dispatcher;
  private int sampleRate;

  private void initPd() throws IOException {
    sampleRate = AudioParameters.suggestSampleRate();
    PdAudio.initAudio(sampleRate, 0, 2, 8, true);

    dispatcher = new PdUiDispatcher();
    PdBase.setReceiver(dispatcher);
  }

  // android did not have same sample rate problems as iOS.  currently unused.
  private void checkSampleRate() throws IOException {
    AudioManager audioService = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    int currentSampleRate = Integer.parseInt(audioService.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE));
    if (currentSampleRate != sampleRate) {
      sampleRate = currentSampleRate;
      PdAudio.initAudio(sampleRate, 0, 2, 8, true);
      dispatcher = new PdUiDispatcher();
      PdBase.setReceiver(dispatcher);
    }
  }

  // can be used to manually load a pd patch without the use of the PureDataModule
  private void loadPdPatch() throws IOException {
    File dir = getFilesDir();
    IoUtils.extractZipResource(getResources().openRawResource(R.raw.patches), dir, true);
    File pdPatch = new File(dir, "patches/test_tone.pd");
    int patch = PdBase.openPatch(pdPatch.getAbsolutePath());
    Log.i(TAG, "Finished loading pd patch " + patch);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      initPd();
      File dir = getFilesDir();
      IoUtils.extractZipResource(getResources().openRawResource(R.raw.patches), dir, true);
      //loadPdPatch();
    } catch (IOException e) {
      Log.e(TAG, "Error initializing pd: " + e.getMessage());
      finish();
    }
  }

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "pd";
  }

  @Override
  protected void onResume() {
    super.onResume();
    PdAudio.startAudio(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    PdAudio.stopAudio();
  }

  /**
   * Returns the instance of the {@link ReactActivityDelegate}. Here we use a util class {@link
   * DefaultReactActivityDelegate} which allows you to easily enable Fabric and Concurrent React
   * (aka React 18) with two boolean flags.
   */
  @Override
  protected ReactActivityDelegate createReactActivityDelegate() {
    return new DefaultReactActivityDelegate(
        this,
        getMainComponentName(),
        // If you opted-in for the New Architecture, we enable the Fabric Renderer.
        DefaultNewArchitectureEntryPoint.getFabricEnabled());
  }
}
