#!/bin/bash
set -e

APP_DIR=/home/ubuntu/irion
APP_JAR="$APP_DIR/IRI-ON.jar"
LOG_FILE="$APP_DIR/app.log"
echo "[시작] Spring Boot 애플리케이션 실행 준비"

if [ ! -f "$APP_JAR" ]; then
 echo "[오류] $APP_JAR 파일이 존재하지 않습니다. 실행 불가"
 exit 1
fi

touch "$LOG_FILE"
chown ubuntu:ubuntu "$LOG_FILE"
chmod 644 "$LOG_FILE"
echo "$LOG_FILE 로그 파일 생성 및 권한 변경 완료"

echo "$APP_JAR 실행"
nohup java -jar "$APP_JAR" > "$LOG_FILE" 2>&1 &
echo "[시작 완료] 애플리케이션 실행 완료, 로그 파일: $LOG_FILE"
