
cmake_minimum_required(VERSION 3.4.1)

include_directories(src/main/cpp/include)

aux_source_directory(src/main/cpp SRC_FILE)
add_library(readbody2 SHARED ${SRC_FILE})

find_library(
              log-lib
              log )

target_link_libraries( readbody2
                       ${log-lib} android)