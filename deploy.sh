REPOSITORY=/home/ubuntu/app/
cd $REPOSITORY

APP_NAME=inflearn-clone-back
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z "$CURRENT_PID" ]
then
  echo "> No processes found to terminate !" >> /home/ubuntu/deploy.log
else
  echo "> sudo kill -15 $CURRENT_PID" >> /home/ubuntu/deploy.log
  kill -15 "$CURRENT_PID" >> /home/ubuntu/deploy.log 2>&1
  sleep 5
fi

echo "> $JAR_PATH deployed !"
nohup java -jar -Duser.timezone=KST -Dspring.profiles.active=prod $JAR_PATH >> /dev/null &

echo "[$(date)] server deployed !" >> /home/ubuntu/deploy.log