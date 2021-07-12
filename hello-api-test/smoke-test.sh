#!/bin/bash

# semplice smoke test 
# see https://circleci.com/blog/smoke-tests-in-cicd-pipelines/

SMOKE_URL=${1:-"http://localhost:8080/actuator/health"}
TIME_OUT=${2:-180}

TIME_OUT_COUNT=0
TIME_INC=10

while true
do
  echo "Checking status on $SMOKE_URL... $TIME_OUT_COUNT seconds elapsed"
  STATUS=$(curl -s -o /dev/null -w '%{http_code}' $SMOKE_URL)
  if [ $STATUS -eq 200 ]; then
    echo ''
    echo 'Smoke Tests Successfully Completed.'
    break
  elif [[ $TIME_OUT_COUNT -ge $TIME_OUT ]]; then
    echo "Process has Timed out! Elapsed Timeout Count.. $TIME_OUT_COUNT"
    exit 1
  else
    TIME_OUT_COUNT=$((TIME_OUT_COUNT+$TIME_INC))
    sleep $TIME_INC
  fi
done
