package com.camillepradel.movierecommender.databases;


import com.camillepradel.movierecommender.model.Genre;
import com.camillepradel.movierecommender.model.Movie;
import com.camillepradel.movierecommender.model.Rating;
import com.mongodb.*;
import org.bson.Document;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MongoDB {

    public MongoClient mongo;
    public DB db;
    public DBCollection table;
    public MongoDB(){
        mongo = new MongoClient( "localhost" , 27017 );
        db = mongo.getDB("MoviesLens");
    }

    public ArrayList<Movie> getAllMoviesByUserId(Integer idUser){
        table = db.getCollection("users");
        ArrayList<Movie> list = new ArrayList<Movie>() ;
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", idUser);
        DBCursor cursor = table.find(whereQuery);

        while(cursor.hasNext()) {
            DBObject object = cursor.next();
            ArrayList<BasicDBObject> movies = (ArrayList<BasicDBObject>) object.get("movies");
            for (BasicDBObject movie: movies) {
                list.add(getMovieById(Integer.parseInt(movie.get("movieid").toString())));
            }
        }
        return list;
    }

    public ArrayList<Movie> getAllMovies(){
        long startTime = System.nanoTime();
        List<Genre> genres = new ArrayList<Genre>();
        ArrayList<Movie> list = new ArrayList<Movie>();
        table = db.getCollection("movies");
        DBCursor cursor = table.find();
        while(cursor.hasNext()) {
            DBObject object = cursor.next();
            list.add(new Movie(Integer.parseInt(object.get("_id").toString()),object.get("title").toString(),genres));
        }
        long endTime = System.nanoTime();
        //System.out.println("getAllMovies MongoDB : "+((double)(endTime-startTime)/1000000000.0));
        return list;
    }

    public Movie getMovieById(int idMovie){
        List<Genre> genres = new ArrayList<Genre>();
        table = db.getCollection("movies");
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", idMovie);
        table = db.getCollection("movies");
        DBCursor cursor = table.find(whereQuery);
        DBObject object = cursor.next();
        return new Movie(Integer.parseInt(object.get("_id").toString()),object.get("title").toString(),genres);
    }


    public ArrayList<Rating> getRatingByIdUser(int idUser){
        ArrayList<Rating> list = new ArrayList<Rating>();
        table = db.getCollection("users");
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", idUser);
        DBCursor cursor = table.find(whereQuery);
        while(cursor.hasNext()) {
            DBObject object = cursor.next();
            ArrayList<BasicDBObject> movies = (ArrayList<BasicDBObject>) object.get("movies");
            for (BasicDBObject movie: movies) {
                list.add(new Rating(getMovieById(Integer.parseInt(movie.get("movieid").toString())),
                        idUser,Integer.parseInt(movie.get("rating").toString())));
            }
        }
        return list;
    }

    public Boolean isRated( int userId,long movieId){
        Boolean israted = false;
        table = db.getCollection("users");
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id",userId);
        DBCursor cursor = table.find(whereQuery);
        while (cursor.hasNext()){
            DBObject object = cursor.next();
            ArrayList<BasicDBObject> movies = (ArrayList<BasicDBObject>) object.get("movies");
            for (BasicDBObject movie: movies) {
                if (Integer.parseInt(movie.get("movieid").toString())==movieId){
                    israted=true;
                }
            }
        }
        return israted;
    }

    public void updateMovieRating(Rating rating) {
        ArrayList<Rating> listRating= new  ArrayList<Rating>();
        MongoCursor<Document> cursor;

        MongoDatabase data = mongo.getDatabase("MoviesLens");
        MongoCollection<Document> users = data.getCollection("users");

        BasicDBObject updateQuery = new BasicDBObject();
        BasicDBObject newDocument = new BasicDBObject();
        BasicDBObject deleteDocument = new BasicDBObject();

        deleteDocument.append("$pull", new BasicDBObject()
                .append("movies", new BasicDBObject()
                        .append("movieid", rating.getMovieId())
                )
        );

        newDocument.append("$push", new BasicDBObject()
                .append("movies", new BasicDBObject()
                        .append("movieid", rating.getMovieId())
                        .append("rating", rating.getScore())
                        .append("timestamp", (int) (new Date().getTime()/1000))
                )
        );

        BasicDBObject searchQuery = new BasicDBObject()
                .append("_id", rating.getUserId());

        FindIterable<Document> movie = users.find(searchQuery);


        users.updateOne(searchQuery,deleteDocument);
        users.updateOne(searchQuery, newDocument);

    }

    public ArrayList<Rating> recommandations(int userId){

        MongoDatabase data = mongo.getDatabase("MoviesLens");
        MongoCollection<Document> users = data.getCollection("users");

        BasicDBObject project;
        BasicDBObject match;
        BasicDBObject sort;
        BasicDBObject limit;
        BasicDBObject unwind;
        BasicDBObject group;

        project = new BasicDBObject("$project",
                new BasicDBObject("moviesid", "$movies.movieid")
        );

        match = new BasicDBObject("$match",
                new BasicDBObject("_id", new BasicDBObject("$eq", userId))
        );

        AggregateIterable<Document> output = users.aggregate(Arrays.asList(project, match));

        final Document moviesid = output.first();

        BasicDBList intersectionList = new BasicDBList();
        intersectionList.add("$movies.movieid" );
        intersectionList.add( moviesid.get("moviesid") );

        project = new BasicDBObject("$project",
                new BasicDBObject("nbMoviesIntersect",
                        new BasicDBObject("$size",
                                new BasicDBObject("$setIntersection",
                                        intersectionList
                                )
                        )
                )
        );

        match = new BasicDBObject("$match",
                new BasicDBObject("_id", new BasicDBObject("$ne", userId))
        );

        sort = new BasicDBObject("$sort",
                new BasicDBObject("nbMoviesIntersect", -1)
        );

        limit = new BasicDBObject("$limit", 5);

        output = users.aggregate(Arrays.asList(project, match, sort, limit));

        Document simUser = output.first();

        unwind = new BasicDBObject("$unwind", "$movies");

        match = new BasicDBObject("$match",
                new BasicDBObject()
                        .append("_id", simUser.getInteger("_id"))
                        .append("movies.movieid", new BasicDBObject("$nin", moviesid.get("moviesid")))
        );

        group = new BasicDBObject("$group",  new BasicDBObject()
                .append("movies", new BasicDBObject("$push", "$movies"))
        );

        sort = new BasicDBObject("$sort", new BasicDBObject("movies.rating",  -1));

        output = users.aggregate(Arrays.asList(unwind, match, sort, group));

        Document listMoviesDiff = output.first();


        ArrayList<Rating> listRating= new  ArrayList<Rating>();
        MongoCollection<Document> movies = data.getCollection("movies");
        MongoCursor<Document> cursor;

        ArrayList<Document> user_movies = (ArrayList) listMoviesDiff.get("movies");
        BasicDBObject inQuery = new BasicDBObject();
        HashMap<Integer,Integer> list = new HashMap<Integer,Integer>();
        for(Document movie : user_movies )
            list.put(movie.getInteger("movieid"), movie.getInteger("rating"));

        inQuery.put("_id", new BasicDBObject("$in", list.keySet()));
        cursor = movies.find(inQuery).iterator();

        HashMap<Integer,Rating> hashMoviesRating = new HashMap<Integer,Rating>();
        while (cursor.hasNext()) {
            Document movie = cursor.next();
            String[] genres;
            genres = movie.getString("genres").split("\\|");
            ArrayList<Genre> listGenres = new ArrayList<Genre>();
            for(String genre : genres){
                listGenres.add(new Genre(1,genre));
            }
            hashMoviesRating.put(movie.getInteger("_id"),
                    new Rating(new Movie(movie.getInteger("_id"), movie.getString("title"), listGenres),
                            userId, list.get(movie.getInteger("_id"))));

        }

        for(Document movie : user_movies )
            listRating.add(hashMoviesRating.get(movie.getInteger("movieid")));

        return listRating;
    }

}