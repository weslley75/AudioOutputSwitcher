#!/bin/bash

# Script para build com assinatura local
# Uso: ./build-release.sh

echo "🔐 Digite a senha da keystore:"
read -s KEYSTORE_PASSWORD

echo "🔐 Digite a senha da chave (ou Enter se for a mesma):"
read -s KEY_PASSWORD

# Se KEY_PASSWORD estiver vazio, usar a mesma senha da keystore
if [ -z "$KEY_PASSWORD" ]; then
    KEY_PASSWORD=$KEYSTORE_PASSWORD
fi

# Exportar variáveis de ambiente
export KEYSTORE_PASSWORD
export KEY_PASSWORD

echo "🏗️ Buildando APK assinado..."
./gradlew assembleRelease

if [ $? -eq 0 ]; then
    echo "✅ APK assinado criado com sucesso!"
    echo "📦 Localização: app/build/outputs/apk/release/"
    ls -la app/build/outputs/apk/release/
else
    echo "❌ Erro no build"
    exit 1
fi