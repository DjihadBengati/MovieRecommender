package com.camillepradel.movierecommender.databases;


import com.camillepradel.movierecommender.model.Genre;
import com.camillepradel.movierecommender.model.Movie;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;

public class MongoDB {

    public MongoClient mongo;
    public DB db;
    public DBCollection table;
    public MongoDB(){
        mongo = new MongoClient( "localhost" , 27017 );
        db = mongo.getDB("MoviesLens");
    }


    public Integer getNbMovies(){
        table = db.getCollection("movies");
        DBCursor cursor = table.find();
        return cursor.count();
    }

    public ArrayList<Movie> getAllMoviesByUserId(Integer idUser){


        table = db.getCollection("users");
        ArrayList<Movie> list = new ArrayList<Movie>() ;
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", idUser);
        DBCursor cursor = table.find(whereQuery);

        while(cursor.hasNext()) {
            DBObject object = cursor.next();
            System.out.println(((ArrayList) object.get("movies")).size());
            ArrayList<BasicDBObject> movies = (ArrayList<BasicDBObject>) object.get("movies");
            System.out.println(movies);
            for (BasicDBObject movie: movies) {
                System.out.println(movie);
                list.add(getMovieById(Integer.parseInt(movie.get("movieid").toString())));
            }
        }
        return list;
    }

    public ArrayList<Movie> getAllMovies(){
        List<Genre> genres = new ArrayList<Genre>();
        ArrayList<Movie> list = new ArrayList<Movie>();
        table = db.getCollection("movies");
        DBCursor cursor = table.find();
        while(cursor.hasNext()) {
            DBObject object = cursor.next();
            list.add(new Movie(Integer.parseInt(object.get("_id").toString()),object.get("title").toString(),genres));
        }

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
        System.out.println(object.get("title").toString());

        return new Movie(Integer.parseInt(object.get("_id").toString()),object.get("title").toString(),genres);
    }

}
