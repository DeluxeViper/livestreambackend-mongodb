# livestreambackend-mongodb
MongoDB backend for potential livestream Android app


## Docker Setup

### (Required) MongoDB replicaset setup: 

**Note**: The replicaset for MongoDB *must* be setup due to the changestreams used

1. Recreate the container
2. Accessing MongoDB's instance: `mongo -u "rootuser" -p "rootpass"`
3. Initialize the replicaset: `rs.initiate()`
4. (Optional) You can double check the status of the replicaset by using: `rs.status()`

**Note**: If mongo-express fails, you must recreate the mongo-express container after changing this setup (not sure why)
