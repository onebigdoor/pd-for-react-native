#import "RCTPureDataModule.h"
#import "PdBase.h"

@implementation RCTPureDataModule

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(sendFloat
                  : (NSString *)name value
                  : (float)value cb
                  : (RCTResponseSenderBlock)cb) {
  [PdBase sendFloat:value toReceiver:name];
  NSLog(@"%f", value);
}

RCT_EXPORT_METHOD(sendList
                  : (NSString *)name value
                  : (NSArray *)value cb
                  : (RCTResponseSenderBlock)cb) {
  [PdBase sendList:value toReceiver:name];
}

RCT_EXPORT_METHOD(loadPatch
                  : (NSString *)name cb
                  : (RCTResponseSenderBlock)cb) {
  [PdBase openFile:name path:[[NSBundle mainBundle] resourcePath]];
  NSLog(@"%@", name);
}

@end
