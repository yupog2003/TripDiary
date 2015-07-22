LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := TripDiary
LOCAL_LDLIBS := \
	-llog \

LOCAL_SRC_FILES := \
	/run/media/yupog2003/1345b443-82c5-4d64-b39f-e68c01e04f53/程式/Android/開發/Workspace/TripDiary/tripDiary/src/main/jni/TripDiary.cpp \

LOCAL_C_INCLUDES += /run/media/yupog2003/1345b443-82c5-4d64-b39f-e68c01e04f53/程式/Android/開發/Workspace/TripDiary/tripDiary/src/main/jni
LOCAL_C_INCLUDES += /run/media/yupog2003/1345b443-82c5-4d64-b39f-e68c01e04f53/程式/Android/開發/Workspace/TripDiary/tripDiary/src/release/jni

include $(BUILD_SHARED_LIBRARY)
