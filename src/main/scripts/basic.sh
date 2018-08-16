#!/bin/bash

baseUrl="http://127.0.0.1:8080"
baseUrl="https://priority-coach-dev.herokuapp.com/"

# reference data
curl "$baseUrl/v1/sync/reference" | jq

# authenticate
authToken=`curl -X POST -H "Content-Type: application/json" -d '{"email":"stef01@example.com", "password": "stef01"}' "$baseUrl/v1/login" | jq -M -r '.accessToken' | base64`
echo "authToken=$authToken"

# user data
curl -H "Authorization: $authToken" "$baseUrl/v1/sync/user" | jq

# Create LifeUpgradeAction
curl -H "Content-Type: application/json" -H "Authorization: $authToken" -d '{"updated":{"lifeUpgradeActions":[{"id": "14df4e07-b6bf-4571-8eac-208bbb0e4433", "name": "My Test Action 2", "lifeUpgradeCategoryId": "7322472d-b2f8-4e9e-a18e-254c36042734", "custom": true}]}}' "$baseUrl/v1/sync/user"
# life_upgrade_category_id
# 7322472d-b2f8-4e9e-a18e-254c36042734

# Delete LifeUpgradeAction
curl -H "Content-Type: application/json" -H "Authorization: $authToken" -d '{"deleted": {"lifeUpgradeActions": ["14df4e07-b6bf-4571-8eac-208bbb0e4433"]}}' "$baseUrl/v1/sync/user" | jq


# accept invitation
invitationToken=`curl -X POST -H "Authorization: $authToken" -H "Content-Type: application/json" -d '{"email": "john.doe@futurist-labs1.com"}' "$baseUrl/v1/invitations" | jq -M -r '.invitationToken'`
curl -X POST -H "Authorization: $authToken" -H "Content-Type: application/json" -d "" "$baseUrl/v1/invitations/$invitationToken"

# reject invitation
invitationToken=`curl -X POST -H "Authorization: $authToken" -H "Content-Type: application/json" -d '{"email": "john.doe@futurist-labs1.com"}' "$baseUrl/v1/invitations" | jq -M -r '.invitationToken'`
curl -X DELETE -H "Authorization: $authToken" "$baseUrl/v1/invitations/$invitationToken"

# chat messages
friendId="0c75ef5d-6373-4623-b70d-0a823f315036"
curl -H "Authorization: $authToken" "$baseUrl/v1/friends/$friendId/messages" | jq
curl -H "Authorization: $authToken" -H "If-Modified-Since: 2016-02-23T23:20:36.471+02:00" "$baseUrl/v1/friends/$friendId/messages" | jq

