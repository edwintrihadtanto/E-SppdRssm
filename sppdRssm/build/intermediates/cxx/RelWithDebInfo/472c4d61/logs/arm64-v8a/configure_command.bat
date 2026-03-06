@echo off
"E:\\SDK\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HG:\\Kumpulan Project Android\\E-SppdRssm\\sppdRssm\\src\\main\\cpp" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=24" ^
  "-DANDROID_PLATFORM=android-24" ^
  "-DANDROID_ABI=arm64-v8a" ^
  "-DCMAKE_ANDROID_ARCH_ABI=arm64-v8a" ^
  "-DANDROID_NDK=E:\\SDK\\ndk\\29.0.14206865" ^
  "-DCMAKE_ANDROID_NDK=E:\\SDK\\ndk\\29.0.14206865" ^
  "-DCMAKE_TOOLCHAIN_FILE=E:\\SDK\\ndk\\29.0.14206865\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=E:\\SDK\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=G:\\Kumpulan Project Android\\E-SppdRssm\\sppdRssm\\build\\intermediates\\cxx\\RelWithDebInfo\\472c4d61\\obj\\arm64-v8a" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=G:\\Kumpulan Project Android\\E-SppdRssm\\sppdRssm\\build\\intermediates\\cxx\\RelWithDebInfo\\472c4d61\\obj\\arm64-v8a" ^
  "-DCMAKE_BUILD_TYPE=RelWithDebInfo" ^
  "-BG:\\Kumpulan Project Android\\E-SppdRssm\\sppdRssm\\.cxx\\RelWithDebInfo\\472c4d61\\arm64-v8a" ^
  -GNinja
