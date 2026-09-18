cmake_minimum_required(VERSION 3.10)

project("sdkauth")

add_library(
        sdkauth
        SHARED
        sdk_auth.cpp
)

# 去掉调试符号，减小体积、增加逆向难度
target_compile_options(sdkauth PRIVATE -O2 -fvisibility=hidden -fno-exceptions -fno-rtti)

target_link_libraries(
        sdkauth
        log
)