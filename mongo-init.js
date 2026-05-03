db = db.getSiblingDB("mydatabase");

db.createUser({
    user: "dbuser",
    pwd: "dbpassword",
    roles: [{
        role: "readWrite",
        db: "mydatabase"
    }]
});

db.movies.insertOne({ ok: 1})
db.movies_genres.insertOne({ ok: 1})
db.comments.insertOne({ ok: 1})
db.genres.insertOne({ ok: 1})