#!/bin/sh
APP_NAME=JadyerSDK
APP_WARS=JadyerSDK-demo/target
APP_PATH=/app/tomcat-8.0.21
GIT_URL=https://github.com/jadyer/JadyerSDK.git

appPID=0
getAppPID(){
    pidInfo=`ps aux|grep java|grep $APP_PATH|grep -v grep`
    if [ -n "$pidInfo" ]; then
        appPID=`echo $pidInfo | awk '{print $2}'`
    else
        appPID=0
    fi
}

downloadAndCompileSourceCode(){
    cd $APP_PATH/temp
    git clone $GIT_URL
    cd $APP_NAME
    mvn clean package -DskipTests
}

shutdown(){
    getAppPID
    echo "[ÐþÓñ] ======================================================================================================================================================"
    if [ $appPID -ne 0 ]; then
        echo -n "[ÐþÓñ] Stopping $APP_PATH(PID=$appPID)..."
        kill -9 $appPID
        if [ $? -eq 0 ]; then
            echo "[Success]"
            echo "[ÐþÓñ] ======================================================================================================================================================"
        else
            echo "[Failed]"
            echo "[ÐþÓñ] ======================================================================================================================================================"
        fi
        getAppPID
        if [ $appPID -ne 0 ]; then
            shutdown
        fi
    else
        echo "[ÐþÓñ] $APP_PATH is not running"
        echo "[ÐþÓñ] ======================================================================================================================================================"
    fi
}

deploy(){
    cd $APP_PATH/webapps/
    rm -rf $APP_NAME
    rm -rf $APP_NAME.war
    cp $APP_PATH/temp/$APP_NAME/$APP_WARS/*.war $APP_NAME.war
    cd $APP_PATH/logs/
    rm -rf *
    cd $APP_PATH/temp
    rm -rf $APP_NAME
}

startup(){
    cd $APP_PATH/bin
    ./startup.sh
    tail -100f ../logs/catalina.out
}

downloadAndCompileSourceCode
shutdown
deploy
startup