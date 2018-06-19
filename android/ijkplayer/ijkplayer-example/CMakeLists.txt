
cmake_minimum_required(VERSION 3.4.1)

set(libs "${CMAKE_SOURCE_DIR}/src/main/jniLibs")

include_directories(${CMAKE_SOURCE_DIR}/src/main/jni/include)


#add_library(libopencv_java3 SHARED IMPORTED )
#set_target_properties(libopencv_java3 PROPERTIES
    #IMPORTED_LOCATION "${libs}/armeabi-v7a/libopencv_java3.so")

#add_library(readsense_head_tracking STATIC IMPORTED)

#set_target_properties(readsense_head_tracking PROPERTIES IMPORTED_LOCATION "${libs}/armeabi-v7a/libreadsense_head_tracking.a")

#add_library( # Sets the name of the library.
             ##SHARED

             # Provides a relative path to your source file(s).
             #src/main/jni/ReadHead.cpp )

add_library(control_gate SHARED src/main/jni/ControlGate.cpp)


find_library(
              log-lib
              log )
target_link_libraries(control_gate log android)

