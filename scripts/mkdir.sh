#!/bin/bash
set -e # 오류 발생 시 중단

APP_DIR=/home/ubuntu/irion

echo "[디렉토리 준비] 디렉토리 생성 및 기존 파일 삭제"

mkdir -p "$APP_DIR"
rm -f "$APP_DIR/IRI-ON.jar" # 기존 파일 삭제
chown -R ubuntu:ubuntu "$APP_DIR"
chmod -R 755 "$APP_DIR"

echo "[디렉토리 준비 완료] 소유권 및 권한 설정 완료"