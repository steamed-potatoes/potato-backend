#!/bin/bash

DOCKER_APP_NAME=potato-api

cat /GIT-TOKEN.txt | docker login ghcr.io -u USERNAME --password-stdin

EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

if [ -z "$EXIST_BLUE" ]; then
    echo "Blue Up"
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

    i=0
    while [ $i -le 30 ]
    do
        STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/ping)
        if [ $STATUS -eq 200 ]; then
            echo "Server is Running.....!"

            docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
            echo "Green Down"

            break
        else
            echo "Waiting........."
            sleep 10
            i=$(($i+1))
        fi
    done
else
    echo "Green Up"
    docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

    i=0
    while [ $i -le 30 ]
    do
        STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8082/ping)
        if [ $STATUS -eq 200 ]; then
            echo "Server is Running.....!"

            docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
            echo "Blue Down"

            break
        else
            echo "Waiting........."
            sleep 10
            i=$(($i+1))
        fi
    done
fi

