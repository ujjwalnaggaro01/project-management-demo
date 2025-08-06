@echo off
REM Get short Git commit hash
FOR /F "delims=" %%i IN ('git rev-parse --short HEAD') DO SET GIT_TAG=%%i

REM Build Docker image
docker build -t project-service:%GIT_TAG% .

REM Tag Docker image for Docker Hub
docker tag project-service:%GIT_TAG% ujjwalraghuvanshi1212/project-service:%GIT_TAG%

REM Push to Docker Hub
docker push ujjwalraghuvanshi1212/project-service:%GIT_TAG%

echo.
echo Script execution complete. Press any key to exit...
pause >nul