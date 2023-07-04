#import "AppDelegate.h"

#import <React/RCTBundleURLProvider.h>

#import "PdAudioController.h"
#import "PdBase.h"

@interface AppDelegate () <RCTBridgeDelegate>
@property(nonatomic, retain) PdAudioController *audioController;
@property(nonatomic) double sampleRate;
- (void) checkSampleRate:(NSTimer *)timer;
@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  self.moduleName = @"pd";
  // You can add your custom initial props in the dictionary below.
  // They will be passed down to the ViewController used by React Native.
  self.initialProps = @{};
  
  // setPreferredSampleRate does not guarantee a change.  speaker only supports 48000 on many devices
  // bluetooth speakers force to 44100
  self.audioController = [[PdAudioController alloc] init];
  AVAudioSession *session = AVAudioSession.sharedInstance;
  self.sampleRate = session.sampleRate;
  [self.audioController configurePlaybackWithSampleRate:44100 numberChannels:2 inputEnabled:NO mixingEnabled:YES];
  
  //[PdBase openFile:@"test_tone.pd" path:[[NSBundle mainBundle] resourcePath]];

  [self.audioController setActive:YES];
  [self.audioController print];

  return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
#if DEBUG
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
  return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

- (void) checkSampleRate:(NSTimer *)timer {
  AVAudioSession *session = AVAudioSession.sharedInstance;
  if (session.sampleRate != self.sampleRate) {
    self.sampleRate = session.sampleRate;
    self.audioController = [[PdAudioController alloc] init];
    [self.audioController configurePlaybackWithSampleRate:44100 numberChannels:2 inputEnabled:NO mixingEnabled:YES];
    [self.audioController setActive:YES];
    [self.audioController print];
  }
}

@end
