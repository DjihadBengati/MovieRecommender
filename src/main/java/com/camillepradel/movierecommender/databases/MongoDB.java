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

    public ArrayList<Movie> getAllMoviesByUserId(Integer idUser){


        table = db.getCollection("users");
        ArrayList<Movie> list = new ArrayList<Movie>() ;
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", idUser);
        DBCursor cursor = table.find(whereQuery);
        DBObject object = cursor.next();
        System.out.println(object.get("movies"));
        System.out.println(object.get("movies").getClass());
        BasicDBList ratings = (BasicDBList) object.get("movies");
        for (int i=0;i<ratings.size();i++) {
            list.add(getMovieById(Integer.parseInt(((BasicDBObject)ratings.get(i)).get("movieid").toString())));
        }

        /*
        while(cursor.hasNext()) {
            DBObject object = cursor.next();
            for (Object movie: (ArrayList) object.get("movies")) {
                list.add(getMovieById(movie.get("movieid")));
            }
        }*/

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
        DBCursor cursor = table.find();
        DBObject object = cursor.next();
        return new Movie(Integer.parseInt(object.get("_id").toString()),object.get("title").toString(),genres);

    }

}

