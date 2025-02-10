#!/bin/bash
IMAGE_ID=$(docker images -q relay-task:latest)
CONTAINER_ID=$(docker ps -q -f name=relay-task)
if [ "$CONTAINER_ID" != "" ]
then
    docker stop $CONTAINER_ID
    docker rm -f $CONTAINER_ID
fi
echo "container:relay-task stop ..."
if [ "IMAGE_ID" != "" ]
then
            docker rmi $IMAGE_ID
fi
echo "image:relay-task remove ... wait service restart ..."
docker build -t relay-task:latest .
NEW_CONTAINER_ID=$(docker run -d -p 18091:18091 -p 28091:28091 --name relay-task --restart=always relay-task:latest)
echo "container:relay-task restart,containerId=$NEW_CONTAINER_ID"
