#include <jni.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <fstream>
#include <iostream>
#include <vector>
#include <cstdlib>
#include <cmath>
#include <sstream>
#include <ctime>
#include <android/log.h>

#define pi 3.14159265358979323846
#define altitudeDifferThreshold 20
#define earthRadius 6378.1*1000
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,"trip",__VA_ARGS__)

using namespace std;

extern "C" {

jclass stringClass;
jclass cacheClass;
jclass myLatLng2Class;
jclass arrayListClass;
jclass floatClass;
jclass doubleClass;
jclass gpxAnalyzer2Class;
jclass recordClass;
jclass allRecordActivityClass;

jfieldID mLatitude;
jfieldID mLongitude;
jfieldID mAltitude;
jfieldID mTime;

jfieldID mLatitudes;
jfieldID mLongitudes;
jfieldID mAltitudes;
jfieldID mTimes;
jfieldID mStartTime;
jfieldID mEndTime;
jfieldID mTotalTime;
jfieldID mDistance;   // meters
jfieldID mAvgSpeed;   // km/hr
jfieldID mMaxSpeed;		 // km/hr
jfieldID mClimb;      // meters
jfieldID mMaxAltitude;      // meters
jfieldID mMinAltitude;      // meters

jfieldID rTotalTime;
jfieldID rTotalDistance;
jfieldID rTotalClimb;
jfieldID rMaxAltitude;
jfieldID rMinAltitude;
jfieldID rMaxLatitude;
jfieldID rMinLatitude;
jfieldID rTracks;

jmethodID struct_MyLatLng2;
jmethodID struct_ArrayList;
jmethodID struct_Float;
jmethodID struct_Double;
jmethodID arrayList_add;
jmethodID progress_changed;
jmethodID allRecordActivity_progressChanged;
jmethodID allRecordActivity_onTrackFinished;
bool stop;

static bool startsWith(const string& haystack, const string& needle) {
	return needle.length() <= haystack.length() && equal(needle.begin(), needle.end(), haystack.begin());
}
static bool contains(const string& haystack, const string& needle) {
	return haystack.find(needle) != string::npos;
}
static vector<string> split(string str, string sep) {
	char* cstr = const_cast<char*>(str.c_str());
	char* current;
	vector < string > arr;
	current = strtok(cstr, sep.c_str());
	while (current != NULL) {
		arr.push_back(current);
		current = strtok(NULL, sep.c_str());
	}
	return arr;
}
static double distFrom(double lat1, double lng1, double lat2, double lng2) {
	double dLat, dLng, dist;
	dLat = (lat2 - lat1) * pi / 360;
	dLng = (lng2 - lng1) * pi / 360;
	dist = sin(dLat) * sin(dLat) + cos(pi * lat1 / 180) * cos(pi * lat2 / 180) * sin(dLng) * sin(dLng);
	dist = 2 * atan2(sqrt(dist), sqrt(1 - dist));
	dist = earthRadius * dist;
	return dist;
}
static __inline__ double myatof(const char *nptr) {
	return (strtod(nptr, NULL));
}
static double now_ms(void) {
	struct timespec res;
	clock_gettime(CLOCK_REALTIME, &res);
	return 1000.0 * res.tv_sec + (double) res.tv_nsec / 1e6;
}
static time_t getSecondsFromString(string s) {
	string timeStr = s.substr(s.find(">") + 1, s.rfind("Z") - s.find(">") - 1);
	tm t;
	strptime(timeStr.c_str(), "%Y-%m-%dT%H:%M:%S", &t);
	time_t seconds = mktime(&t);
	return seconds;
}
class MyLatLng2 {
public:
	double lat;
	double lng;
	string time;
	float altitude;
};
jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	JNIEnv* env;
	if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
		return -1;
	}
	stringClass = env->FindClass("java/lang/String");
	stringClass = (jclass) env->NewGlobalRef(stringClass);
	cacheClass = env->FindClass("com/yupog2003/tripdiary/data/TrackCache");
	cacheClass = (jclass) env->NewGlobalRef(cacheClass);
	myLatLng2Class = env->FindClass("com/yupog2003/tripdiary/data/MyLatLng2");
	myLatLng2Class = (jclass) env->NewGlobalRef(myLatLng2Class);
	arrayListClass = env->FindClass("java/util/ArrayList");
	arrayListClass = (jclass) env->NewGlobalRef(arrayListClass);
	floatClass = env->FindClass("java/lang/Float");
	floatClass = (jclass) env->NewGlobalRef(floatClass);
	doubleClass = env->FindClass("java/lang/Double");
	doubleClass = (jclass) env->NewGlobalRef(doubleClass);
	gpxAnalyzer2Class = env->FindClass("com/yupog2003/tripdiary/data/GpxAnalyzer2");
	gpxAnalyzer2Class = (jclass) env->NewGlobalRef(gpxAnalyzer2Class);
	recordClass = env->FindClass("com/yupog2003/tripdiary/data/Record");
	recordClass = (jclass) env->NewGlobalRef(recordClass);
	allRecordActivityClass = env->FindClass("com/yupog2003/tripdiary/AllRecordActivity");
	allRecordActivityClass = (jclass) env->NewGlobalRef(allRecordActivityClass);

	mLatitude = env->GetFieldID(myLatLng2Class, "latitude", "D");
	mLongitude = env->GetFieldID(myLatLng2Class, "longitude", "D");
	mAltitude = env->GetFieldID(myLatLng2Class, "altitude", "F");
	mTime = env->GetFieldID(myLatLng2Class, "time", "Ljava/lang/String;");

	mLatitudes = env->GetFieldID(cacheClass, "latitudes", "[D");
	mLongitudes = env->GetFieldID(cacheClass, "longitudes", "[D");
	mAltitudes = env->GetFieldID(cacheClass, "altitudes", "[F");
	mTimes = env->GetFieldID(cacheClass, "times", "[Ljava/lang/String;");
	mStartTime = env->GetFieldID(cacheClass, "startTime", "Ljava/lang/String;");
	mEndTime = env->GetFieldID(cacheClass, "endTime", "Ljava/lang/String;");
	mTotalTime = env->GetFieldID(cacheClass, "totalTime", "Ljava/lang/String;");
	mDistance = env->GetFieldID(cacheClass, "distance", "F");   // meters
	mAvgSpeed = env->GetFieldID(cacheClass, "avgSpeed", "F");   // km/hr
	mMaxSpeed = env->GetFieldID(cacheClass, "maxSpeed", "F");		 // km/hr
	mClimb = env->GetFieldID(cacheClass, "climb", "F");      // meters
	mMaxAltitude = env->GetFieldID(cacheClass, "maxAltitude", "F");      // meters
	mMinAltitude = env->GetFieldID(cacheClass, "minAltitude", "F");      // meters

	rTotalTime = env->GetFieldID(recordClass, "totalTime", "Ljava/lang/String;");
	rTotalDistance = env->GetFieldID(recordClass, "totalDistance", "F");
	rTotalClimb = env->GetFieldID(recordClass, "totalClimb", "F");
	rMaxAltitude = env->GetFieldID(recordClass, "maxAltitude", "F");
	rMinAltitude = env->GetFieldID(recordClass, "minAltitude", "F");
	rMaxLatitude = env->GetFieldID(recordClass, "maxLatitude", "D");
	rMinLatitude = env->GetFieldID(recordClass, "minLatitude", "D");
	rTracks = env->GetFieldID(recordClass, "tracks", "[[Lcom/yupog2003/tripdiary/data/MyLatLng2;");

	struct_MyLatLng2 = env->GetMethodID(myLatLng2Class, "<init>", "()V");
	struct_ArrayList = env->GetMethodID(arrayListClass, "<init>", "()V");
	struct_Float = env->GetMethodID(floatClass, "<init>", "(F)V");
	struct_Double = env->GetMethodID(doubleClass, "<init>", "(D)V");
	arrayList_add = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
	progress_changed = env->GetMethodID(gpxAnalyzer2Class, "onProgressChanged", "(J)V");
	allRecordActivity_progressChanged = env->GetMethodID(allRecordActivityClass, "progressChanged", "(I)V");
	allRecordActivity_onTrackFinished = env->GetMethodID(allRecordActivityClass, "onTrackFinished", "([D[D)V");

	return JNI_VERSION_1_6;
}
JNIEXPORT jboolean Java_com_yupog2003_tripdiary_data_GpxAnalyzer2_parse(JNIEnv* env, jobject thiz, jstring gpxPath, jobject cache, jobject speeds, jint timezoneO) {
	stop = false;
	const char *gpxPathChar = (char*) (env->GetStringUTFChars(gpxPath, 0));
	char cachePath[100];
	strcpy(cachePath, gpxPathChar);
	strcat(cachePath, ".cache");
	time_t timezoneOffset = timezoneO; //(second)
	ifstream fis(gpxPathChar, ifstream::in);
	string s;
	vector<MyLatLng2> track;
	vector<time_t> times;
	MyLatLng2 latlng;
	MyLatLng2 preLatLng;
	bool first = true;
	float distance = 0;
	float totalAltitude = 0;
	float maxAltitude = FLT_MIN;
	float minAltitude = FLT_MAX;
	float preAltitude = 0;
	unsigned int count = 0;
	string timeStr;
	while (getline(fis, s)) {
		if (stop) {
			return false;
		}
		count++;
		if (count == 5000) {
			env->CallVoidMethod(thiz, progress_changed, (jlong) fis.tellg());
			count = 0;
		}
		if (contains(s, "<trkpt")) {
			char c[512];
			if (s.length() > 512) {
				return false;
			}
			strcpy(c, s.c_str());
			if (s.find("lat") > s.find("lon")) {
				strtok(c, "\"");
				latlng.lng = myatof(strtok(NULL, "\""));
				strtok(NULL, "\"");
				latlng.lat = myatof(strtok(NULL, "\""));
			} else {
				strtok(c, "\"");
				latlng.lat = myatof(strtok(NULL, "\""));
				strtok(NULL, "\"");
				latlng.lng = myatof(strtok(NULL, "\""));
			}
		} else if (contains(s, "<ele>")) {
			float altitude = myatof(s.substr(s.find(">") + 1, s.rfind("<") - s.find(">") - 1).c_str());
			latlng.altitude = altitude;
			if (altitude > maxAltitude)
				maxAltitude = altitude;
			if (altitude < minAltitude)
				minAltitude = altitude;
		} else if (contains(s, "<time>")) {
			timeStr = s.substr(s.find(">") + 1, s.rfind("Z") - s.find(">") - 1);
			tm t;
			strptime(timeStr.c_str(), "%Y-%m-%dT%H:%M:%S", &t);
			t.tm_isdst = 0;
			t.tm_sec += timezoneOffset;
			time_t seconds = mktime(&t);
			char resultTime[80];
			strftime(resultTime, 80, "%Y-%m-%dT%H:%M:%S", &t);
			latlng.time = (string) resultTime;
			times.push_back(seconds);
		} else if (contains(s, "</trkpt>")) {
			if (!first) {
				float altitudeDiffer = latlng.altitude - preAltitude;
				if (abs(altitudeDiffer) > altitudeDifferThreshold) {
					if (altitudeDiffer > 0)
						totalAltitude += altitudeDiffer;
					preAltitude = latlng.altitude;
				}
				distance += distFrom(preLatLng.lat, preLatLng.lng, latlng.lat, latlng.lng);
			} else {
				first = false;
				preAltitude = latlng.altitude;
			}
			preLatLng = latlng;
			track.push_back(latlng);
		}
	}
	fis.close();
	int trackSize = track.size();
	int timesSize = times.size();
	if (trackSize == 0 || timesSize == 0 || stop)
		return false;

	float maxSpeed = 0;
	for (int i = 0; i + 20 < trackSize && i + 20 < timesSize; i += 20) {
		if (stop)
			return false;
		float dist = distFrom(track[i].lat, track[i].lng, track[i + 20].lat, track[i + 20].lng);
		float seconds = times[i + 20] - times[i];
		float speed = dist / seconds * 18 / 5;
		/*if (speeds != NULL) {
			jobject speedFloat = env->NewObject(floatClass, struct_Float, (jfloat) speed);
			env->CallBooleanMethod(speeds, arrayList_add, speedFloat);
			env->DeleteLocalRef(speedFloat);
		}*/
		if (maxSpeed < speed)
			maxSpeed = speed;
	}
	if (stop)
		return false;
	time_t totalSeconds = times[times.size() - 1] - times[0];
	stringstream sss;
	int day = totalSeconds / 86400;
	int hour = totalSeconds % 86400 / 3600;
	int min = totalSeconds % 3600 / 60;
	int sec = totalSeconds % 60;
	if (day != 0) {
		sss << day << "T";
	}
	sss << hour << ":" << min << ":" << sec;
	string totalTime = sss.str();
	float avgSpeed = distance / totalSeconds * 18 / 5;
	jstring jstartTime = env->NewStringUTF(track[0].time.c_str());
	jstring jendTime = env->NewStringUTF(track[track.size() - 1].time.c_str());
	jstring jtotalTime = env->NewStringUTF(totalTime.c_str());

	int blockSize = trackSize / 100;
	if (blockSize == 0)
		blockSize = trackSize;
	jdoubleArray jLatitudesArray = env->NewDoubleArray(trackSize);
	jdoubleArray jLongitudesArray = env->NewDoubleArray(trackSize);
	jfloatArray jAltitudesArray = env->NewFloatArray(trackSize);
	jdouble latitudes[blockSize];
	jdouble longitudes[blockSize];
	jfloat altitudes[blockSize];
	int blockIndex;
	for (int i = 0; i < trackSize; i++) {
		blockIndex = i % blockSize;
		latitudes[blockIndex] = (jdouble) track[i].lat;
		longitudes[blockIndex] = (jdouble) track[i].lng;
		altitudes[blockIndex] = (jfloat) track[i].altitude;
		if (blockIndex == blockSize - 1 || i == trackSize - 1) {
			env->SetDoubleArrayRegion(jLatitudesArray, i - blockIndex, blockIndex + 1, latitudes);
			env->SetDoubleArrayRegion(jLongitudesArray, i - blockIndex, blockIndex + 1, longitudes);
			env->SetFloatArrayRegion(jAltitudesArray, i - blockIndex, blockIndex + 1, altitudes);
		}
	}

	jobjectArray timesArray = env->NewObjectArray(trackSize, stringClass, env->NewStringUTF(""));
	for (int i = 0; i < trackSize; i++) {
		jstring time = (jstring) env->NewStringUTF(track[i].time.c_str());
		env->SetObjectArrayElement(timesArray, i, time);
		env->DeleteLocalRef(time);
	}
	env->SetObjectField(cache, mLatitudes, jLatitudesArray);
	env->SetObjectField(cache, mLongitudes, jLongitudesArray);
	env->SetObjectField(cache, mAltitudes, jAltitudesArray);
	env->SetObjectField(cache, mTimes, timesArray);
	env->SetObjectField(cache, mStartTime, jstartTime);
	env->SetObjectField(cache, mEndTime, jendTime);
	env->SetObjectField(cache, mTotalTime, jtotalTime);
	env->SetFloatField(cache, mDistance, (jfloat) distance);
	env->SetFloatField(cache, mAvgSpeed, (jfloat) avgSpeed);
	env->SetFloatField(cache, mMaxSpeed, (jfloat) maxSpeed);
	env->SetFloatField(cache, mClimb, (jfloat) totalAltitude);
	env->SetFloatField(cache, mMaxAltitude, (jfloat) maxAltitude);
	env->SetFloatField(cache, mMinAltitude, (jfloat) minAltitude);
	env->DeleteLocalRef(jstartTime);
	env->DeleteLocalRef(jendTime);
	env->DeleteLocalRef(jtotalTime);
	env->DeleteLocalRef(timesArray);
	env->DeleteLocalRef(jLatitudesArray);
	env->DeleteLocalRef(jLongitudesArray);
	env->DeleteLocalRef(jAltitudesArray);

	ofstream fos(cachePath, ofstream::out);
	fos.precision(12);
	fos << track[0].time << '\n';
	fos << track[trackSize - 1].time << '\n';
	fos << totalTime << '\n';
	fos << distance << '\n';
	fos << avgSpeed << '\n';
	fos << maxSpeed << '\n';
	fos << totalAltitude << '\n';
	fos << maxAltitude << '\n';
	fos << minAltitude << '\n';
	for (int i = 0; i < trackSize; i++) {
		fos << track[i].lat << '\n';
		fos << track[i].lng << '\n';
		fos << track[i].altitude << '\n';
		fos << track[i].time << '\n';
	}
	fos << flush;
	fos.close();
	return true;
}
JNIEXPORT bool Java_com_yupog2003_tripdiary_data_GpxAnalyzer2_getCache(JNIEnv* env, jobject thiz, jstring cachePath, jobject cache) {
	const char *cachePathChar = (char*) (env->GetStringUTFChars(cachePath, 0));
	ifstream fis(cachePathChar);
	string s;
	getline(fis, s);
	jstring jstartTime = env->NewStringUTF(s.c_str());
	getline(fis, s);
	jstring jendTime = env->NewStringUTF(s.c_str());
	getline(fis, s);
	jstring jtotalTime = env->NewStringUTF(s.c_str());
	env->SetObjectField(cache, mStartTime, jstartTime);
	env->SetObjectField(cache, mEndTime, jendTime);
	env->SetObjectField(cache, mTotalTime, jtotalTime);
	getline(fis, s);
	env->SetFloatField(cache, mDistance, (jfloat) myatof(s.c_str()));
	getline(fis, s);
	env->SetFloatField(cache, mAvgSpeed, (jfloat) myatof(s.c_str()));
	getline(fis, s);
	env->SetFloatField(cache, mMaxSpeed, (jfloat) myatof(s.c_str()));
	getline(fis, s);
	env->SetFloatField(cache, mClimb, (jfloat) myatof(s.c_str()));
	getline(fis, s);
	env->SetFloatField(cache, mMaxAltitude, (jfloat) myatof(s.c_str()));
	getline(fis, s);
	env->SetFloatField(cache, mMinAltitude, (jfloat) myatof(s.c_str()));
	env->DeleteLocalRef(jstartTime);
	env->DeleteLocalRef(jendTime);
	env->DeleteLocalRef(jtotalTime);
	jdoubleArray latitudes = (jdoubleArray) env->GetObjectField(cache, mLatitudes);
	jdoubleArray longitudes = (jdoubleArray) env->GetObjectField(cache, mLongitudes);
	jfloatArray altitudes = (jfloatArray) env->GetObjectField(cache, mAltitudes);
	int size = env->GetArrayLength(latitudes);
	int blockSize = size / 100;
	if (blockSize == 0)
		blockSize = size;
	jobjectArray times = env->NewObjectArray(size, stringClass, env->NewStringUTF(""));
	jdouble latitudesArray[blockSize];
	jdouble longitudesArray[blockSize];
	jfloat altitudesArray[blockSize];
	int index = 0;
	int blockIndex = 0;
	int count = 0;
	while (getline(fis, s)) {
		if (stop)
			return false;
		count++;
		if (count == 1250) {
			env->CallVoidMethod(thiz, progress_changed, (jlong) fis.tellg());
			count = 0;
		}
		blockIndex = index % blockSize;
		latitudesArray[blockIndex] = (jdouble) myatof(s.c_str());
		getline(fis, s);
		longitudesArray[blockIndex] = (jdouble) myatof(s.c_str());
		getline(fis, s);
		altitudesArray[blockIndex] = (jfloat) myatof(s.c_str());
		getline(fis, s);
		jstring time = env->NewStringUTF(s.c_str());
		env->SetObjectArrayElement(times, index, time);
		env->DeleteLocalRef(time);
		if (blockIndex == blockSize - 1 || index == size - 1) {
			env->SetDoubleArrayRegion(latitudes, index - blockIndex, blockIndex + 1, latitudesArray);
			env->SetDoubleArrayRegion(longitudes, index - blockIndex, blockIndex + 1, longitudesArray);
			env->SetFloatArrayRegion(altitudes, index - blockIndex, blockIndex + 1, altitudesArray);
		}
		index++;
	}
	fis.close();
	env->SetObjectField(cache, mTimes, times);
	env->DeleteLocalRef(times);
	env->DeleteLocalRef(latitudes);
	env->DeleteLocalRef(longitudes);
	env->DeleteLocalRef(altitudes);
	return true;
}
JNIEXPORT bool Java_com_yupog2003_tripdiary_AllRecordActivity_parse(JNIEnv* env, jobject thiz, jstring rootPath, jobjectArray trippaths, jintArray timezones, jobject record) {
	stop = false;
	int num_trips = env->GetArrayLength(trippaths);
	float totalDistance = 0;
	float totalClimb = 0;
	float maxAltitude = FLT_MIN;
	float minAltitude = FLT_MAX;
	double maxLatitude = FLT_MIN;
	double minLatitude = FLT_MAX;
	time_t totalSeconds = 0;
	string s;
	MyLatLng2 latlng;
	string timeStr;
	for (int i = 0; i < num_trips; i++) {
		if (stop)
			return false;
		env->CallVoidMethod(thiz, allRecordActivity_progressChanged, (jint) i);
		MyLatLng2 preLatLng;
		bool first = true;
		jstring tripp = (jstring) env->GetObjectArrayElement(trippaths, i);
		string tripPath = (string) env->GetStringUTFChars(tripp, 0);
		string tripName = tripPath.substr(tripPath.rfind("/") + 1);
		string gpxPath = tripPath + "/" + tripName + ".gpx";
		ifstream fis(gpxPath.c_str(), ifstream::in);
		vector<MyLatLng2> track;
		vector < string > timeStrs;
		while (getline(fis, s)) {
			if (stop) {
				return false;
			}
			if (contains(s, "<trkpt")) {
				char c[512];
				if (s.length() > 512) {
					return false;
				}
				strcpy(c, s.c_str());
				if (s.find("lat") > s.find("lon")) {
					strtok(c, "\"");
					latlng.lng = myatof(strtok(NULL, "\""));
					strtok(NULL, "\"");
					latlng.lat = myatof(strtok(NULL, "\""));
				} else {
					strtok(c, "\"");
					latlng.lat = myatof(strtok(NULL, "\""));
					strtok(NULL, "\"");
					latlng.lng = myatof(strtok(NULL, "\""));
				}
				if (latlng.lat > maxLatitude) {
					maxLatitude = latlng.lat;
				}
				if (latlng.lat < minLatitude) {
					minLatitude = latlng.lat;
				}
			} else if (contains(s, "<ele>")) {
				float altitude = myatof(s.substr(s.find(">") + 1, s.rfind("<") - s.find(">") - 1).c_str());
				latlng.altitude = altitude;
				if (altitude > maxAltitude)
					maxAltitude = altitude;
				if (altitude < minAltitude)
					minAltitude = altitude;
			} else if (contains(s, "<time>")) {
				timeStrs.push_back(s);
			} else if (contains(s, "</trkpt>")) {
				if (!first) {
					float altitudeDiffer = latlng.altitude - preLatLng.altitude;
					if (abs(altitudeDiffer) > altitudeDifferThreshold) {
						if (altitudeDiffer > 0)
							totalClimb += altitudeDiffer;
					}
					totalDistance += distFrom(preLatLng.lat, preLatLng.lng, latlng.lat, latlng.lng);
				} else {
					first = false;
				}
				preLatLng = latlng;
				track.push_back(latlng);
			}
		}
		fis.close();
		jsize trackSize = track.size();
		if (timeStrs.size() > 0)
			totalSeconds += getSecondsFromString(timeStrs[timeStrs.size() - 1]) - getSecondsFromString(timeStrs[0]);
		jdoubleArray latArray = env->NewDoubleArray(trackSize);
		jdoubleArray lngArray = env->NewDoubleArray(trackSize);
		int blockSize = trackSize / 100;
		if (blockSize == 0)
			blockSize = trackSize;
		int blockIndex;
		jdouble latitudes[blockSize];
		jdouble longitudes[blockSize];
		for (int i = 0; i < trackSize; i++) {
			blockIndex = i % blockSize;
			latitudes[blockIndex] = (jdouble) track[i].lat;
			longitudes[blockIndex] = (jdouble) track[i].lng;
			if (blockIndex == blockSize - 1 || i == trackSize - 1) {
				env->SetDoubleArrayRegion(latArray, i - blockIndex, blockIndex + 1, latitudes);
				env->SetDoubleArrayRegion(lngArray, i - blockIndex, blockIndex + 1, longitudes);
			}
		}
		env->CallVoidMethod(thiz, allRecordActivity_onTrackFinished, latArray, lngArray);
		env->DeleteLocalRef(latArray);
		env->DeleteLocalRef(lngArray);
	}
	stringstream sss;
	int day = totalSeconds / 86400;
	int hour = totalSeconds % 86400 / 3600;
	int min = totalSeconds % 3600 / 60;
	int sec = totalSeconds % 60;
	if (day != 0) {
		sss << day << "T";
	}
	sss << hour << ":" << min << ":" << sec;
	string totalTime = sss.str();
	jstring jTotalTime = env->NewStringUTF(totalTime.c_str());
	env->SetObjectField(record, rTotalTime, jTotalTime);
	env->SetFloatField(record, rTotalDistance, (jfloat) totalDistance);
	env->SetFloatField(record, rTotalClimb, (jfloat) totalClimb);
	env->SetFloatField(record, rMaxAltitude, (jfloat) maxAltitude);
	env->SetFloatField(record, rMinAltitude, (jfloat) minAltitude);
	env->SetDoubleField(record, rMaxLatitude, (jdouble) maxLatitude);
	env->SetDoubleField(record, rMinLatitude, (jdouble) minLatitude);
	return true;
}
JNIEXPORT void Java_com_yupog2003_tripdiary_data_GpxAnalyzer2_stop(JNIEnv* env, jobject thiz) {
	stop = true;
}
JNIEXPORT void Java_com_yupog2003_tripdiary_AllRecordActivity_stop(JNIEnv* env, jobject thiz) {
	stop = true;
}

}
