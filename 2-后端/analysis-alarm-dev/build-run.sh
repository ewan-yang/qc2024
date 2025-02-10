#!/bin/bash
IMAGE_ID=$(docker images -q analysis-alarm:latest)
CONTAINER_ID=$(docker ps -q -f name=analysis-alarm)
if [ "$CONTAINER_ID" != "" ]
then
    docker stop $CONTAINER_ID
    docker rm -f $CONTAINER_ID
fi
echo "container:analysis-alarm stop ..."
if [ "IMAGE_ID" != "" ]
then
            docker rmi $IMAGE_ID
fi
echo "image:analysis-alarm remove ... wait service restart ..."
docker build -t analysis-alarm:latest .
NEW_CONTAINER_ID=$(docker run -d -p 18092:18092 -p 28092:28092 --name analysis-alarm --restart=always analysis-alarm:latest  --appendonly yes)
echo "container:analysis-alarm restart,containerId=$NEW_CONTAINER_ID"
