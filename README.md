# DangerousSituationRecognition

Danger Recognition is a fall detection Android application based on a TEMI robot. It utilizes TEMI robot SDK and Tensorflow Lite Framework to realize functions. 

![Fig2](https://user-images.githubusercontent.com/57375920/117052949-572db180-ace6-11eb-9969-22946a00e036.jpeg)


## Significant parameters
*Dangerous body center height threshold: com/example/dangeroussituationrecognition/DetectorActivity.java:225

*Times of fall confirmation rounds:  com.example.dangeroussituationrecognition.DetectorActivity#detectPeriod

*Tensorflow Lite model weight file:  com.example.dangeroussituationrecognition.DetectorActivity#TF_OD_API_MODEL_FILE

*Tensorflow lite model labels file:  com.example.dangeroussituationrecognition.DetectorActivity#TF_OD_API_LABELS_FILE
