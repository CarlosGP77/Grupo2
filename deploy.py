#!/usr/bin/env python3
"""
Script de despliegue remoto para Grupo2 App
Conecta por SSH y ejecuta el despliegue de Docker
"""

import paramiko
import sys
import time
import os

class SSHDeployer:
    def __init__(self, host, user, password, project_path="/home/dario/grupo2"):
        self.host = host
        self.user = user
        self.password = password
        self.project_path = project_path
        self.client = None

    def connect(self):
        """Conecta al servidor SSH"""
        try:
            print(f"\n╔════════════════════════════════════════════════════════╗")
            print(f"║     CONEXIÓN SSH - {self.host}")
            print(f"╚════════════════════════════════════════════════════════╝\n")

            self.client = paramiko.SSHClient()
            self.client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

            print(f"[1/10] Conectando a {self.host}...")
            self.client.connect(self.host, username=self.user, password=self.password, timeout=10)
            print(f"✓ Conectado como {self.user}\n")
            return True
        except Exception as e:
            print(f"✗ Error de conexión: {e}\n")
            return False

    def execute_command(self, command, description=""):
        """Ejecuta un comando remoto"""
        try:
            if description:
                print(f"{description}")

            stdin, stdout, stderr = self.client.exec_command(command)
            output = stdout.read().decode('utf-8')
            error = stderr.read().decode('utf-8')

            if output:
                print(output)
            if error and "warning" not in error.lower():
                print(f"⚠️  {error}")

            return True
        except Exception as e:
            print(f"✗ Error: {e}")
            return False

    def deploy(self):
        """Ejecuta el despliegue completo"""
        if not self.connect():
            return False

        try:
            # PASO 1
            print("[1/10] Estado actual:")
            print("=" * 58)
            self.execute_command("cd " + self.project_path + " && pwd")

            # PASO 2
            print("\n[2/10] Apagando Docker containers...")
            print("=" * 58)
            self.execute_command("cd " + self.project_path + " && docker-compose down 2>/dev/null || echo 'No había containers activos'")

            # PASO 3
            print("\n[3/10] Verificando Docker...")
            print("=" * 58)
            self.execute_command("docker --version && docker-compose --version")

            # PASO 4
            print("\n[4/10] Actualizando código desde Git...")
            print("=" * 58)
            self.execute_command("cd " + self.project_path + " && git fetch origin && git pull origin main")

            # PASO 5
            print("\n[5/10] Últimos cambios:")
            print("=" * 58)
            self.execute_command("cd " + self.project_path + " && git log -1 --oneline")

            # PASO 6
            print("\n[6/10] Verificando orden de arranque en docker-compose.yml...")
            print("=" * 58)
            self.execute_command("cd " + self.project_path + " && grep -A 10 'depends_on:' docker-compose.yml || echo 'Sin dependencias explícitas'")

            # PASO 7
            print("\n[7/10] Iniciando Docker (construyendo imagen)...")
            print("=" * 58)
            self.execute_command("cd " + self.project_path + " && docker-compose up -d --build")

            # PASO 8
            print("\n[8/10] Esperando inicialización (40 segundos)...")
            print("=" * 58)
            for i in range(40, 0, -10):
                print(f"  ⏳ {i}s restantes...")
                time.sleep(10)
            print("  ✓ Inicialización completada")

            # PASO 9
            print("\n[9/10] Estado de servicios:")
            print("=" * 58)
            self.execute_command("cd " + self.project_path + " && docker-compose ps")

            # PASO 10
            print("\n[10/10] Primeros logs de la aplicación:")
            print("=" * 58)
            self.execute_command("cd " + self.project_path + " && docker-compose logs --tail=20 grupo2-app")

            print("\n╔════════════════════════════════════════════════════════╗")
            print("║           ✅ DESPLIEGUE COMPLETADO                    ║")
            print("╚════════════════════════════════════════════════════════╝")
            print("\n📊 SERVICIOS DISPONIBLES:")
            print("  • Aplicación:     http://192.168.35.132:8080")
            print("  • Verificador:    http://192.168.35.132:8080/verificador.html")
            print("  • API REST:       http://192.168.35.132:8080/api/verificador")
            print("  • Nextcloud:      http://192.168.35.132:8888")
            print("\n💡 Para ver logs en tiempo real:")
            print("   docker-compose logs -f grupo2-app\n")

            return True

        except Exception as e:
            print(f"✗ Error durante despliegue: {e}")
            return False
        finally:
            if self.client:
                self.client.close()

def main():
    # Configuración
    host = "192.168.35.132"
    user = "dario"
    password = "1234"
    project_path = "/home/dario/grupo2"

    # Crear deployer
    deployer = SSHDeployer(host, user, password, project_path)

    # Ejecutar
    success = deployer.deploy()

    sys.exit(0 if success else 1)

if __name__ == "__main__":
    main()
