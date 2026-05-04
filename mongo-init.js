db = db.getSiblingDB("mydatabase");

db.createUser({
    user: "dbuser",
    pwd: "dbpassword",
    roles: [{
        role: "readWrite",
        db: "mydatabase"
    }]
});

