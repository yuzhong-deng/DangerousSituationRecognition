# DangerousSituationRecognition

Danger Recognition is a fall detection Android application based on a TEMI robot. It utilizes TEMI robot SDK and Tensorflow Lite Framework to realize functions. 

![Fig2](https://user-images.githubusercontent.com/57375920/117052949-572db180-ace6-11eb-9969-22946a00e036.jpeg)





## Significant parameters
* Dangerous body center height threshold: com/example/dangeroussituationrecognition/DetectorActivity.java:225

* Times of fall confirmation rounds:  com.example.dangeroussituationrecognition.DetectorActivity#detectPeriod

* Tensorflow Lite model weight file:  com.example.dangeroussituationrecognition.DetectorActivity#TF_OD_API_MODEL_FILE

* Tensorflow lite model labels file:  com.example.dangeroussituationrecognition.DetectorActivity#TF_OD_API_LABELS_FILE

## Installation on TEMI

1. Install Android Studio
2. Open TEMI robot and its ADB port
3. Connect your device with TEMI robot by ADB
4. Clone this repo to local
5. Open this project in Android Studio
6. Run it on the TEMI robot

## Customize Tensorflow Lite model
[colab notebook](https://colab.research.google.com/drive/1EOT2KiZ56NN3_VhuiwhFBJLDhrGoxC4X?usp=sharing)
1. Label datasets 
2. Generate darknet custom data
3. Train the datasets by the YOLOv4 
4. Convert the weights to TensorFlow .pb
5. Convert the TensorFlow weights to TensorFlow Lite
6. Application on Android (Requires modification in DetectorActivity file, A: [tensorflow lite standard weight](https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android) B:[YOLOv4 tiny](https://github.com/hunglc007/tensorflow-yolov4-tflite))
