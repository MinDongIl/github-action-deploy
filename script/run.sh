#!/usr/bin/env bash
set -euo pipefail

: "${AWS_REGION:?}"
: "${ECR_REPOSITORY:?}"
: "${ECS_CLUSTER:?}"
: "${ECS_SERVICE:?}"
: "${CONTAINER_NAME:?}"
: "${GITHUB_SHA:?}"

ACCOUNT_ID="$(aws sts get-caller-identity --query 'Account' --output text)"
REGISTRY="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
IMAGE="${REGISTRY}/${ECR_REPOSITORY}:${GITHUB_SHA}"

docker build -t "${ECR_REPOSITORY}:${GITHUB_SHA}" .
docker tag "${ECR_REPOSITORY}:${GITHUB_SHA}" "${IMAGE}"
docker push "${IMAGE}"

jq --arg IMAGE "${IMAGE}" --arg NAME "${CONTAINER_NAME}" '
  .containerDefinitions |= (map(if .name==$NAME then .image=$IMAGE else . end))
' ecs-task-def.json > task-def.rendered.json

TASK_DEF_ARN="$(aws ecs register-task-definition \
  --cli-input-json file://task-def.rendered.json \
  --query 'taskDefinition.taskDefinitionArn' --output text)"

aws ecs update-service \
  --cluster "${ECS_CLUSTER}" \
  --service "${ECS_SERVICE}" \
  --task-definition "${TASK_DEF_ARN}" \
  --force-new-deployment >/dev/null

aws ecs wait services-stable --cluster "${ECS_CLUSTER}" --services "${ECS_SERVICE}"

echo "Deployed image: ${IMAGE}"
echo "TaskDef: ${TASK_DEF_ARN}"
