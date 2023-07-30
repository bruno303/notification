#!/bin/bash

awslocal sqs create-queue --queue-name notification-send-notification &

wait

echo "Environment configured!"