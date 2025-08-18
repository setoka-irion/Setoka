#!/bin/bash
set -e
APP_DIR=/home/ubuntu/irion
APP_JAR="$APP_DIR/IRI-ON.jar"
echo "[설치] $APP_JAR 권한 설정 중"
if [ ! -f "$APP_JAR" ]; then
 echo "[오류] $APP_JAR 파일이 존재하지 않습니다"
 exit 1
fi
chmod 644 "$APP_JAR"
echo "[설치 완료] $APP_JAR 실행 권한 설정 완료"
