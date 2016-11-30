package com.camillepradel.movierecommender.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.camillepradel.movierecommender.databases.MongoDB;

import com.camillepradel.movierecommender.databases.MySQL;
import com.camillepradel.movierecommender.databases.Neo4J;
import com.camillepradel.movierecommender.model.Genre;
import com.camillepradel.movierecommender.model.Rating;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.camillepradel.movierecommender.model.Movie;

@Controller
public class MainController {
	String message = "Welcome to Spring MVC!";
	private MongoDB mongodb;
	private MySQL mysql;
    private Neo4J neo4j;
 
	@RequestMapping("/hello")
	public ModelAndView showMessage(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
		System.out.println("in controller");
 
		ModelAndView mv = new ModelAndView("helloworld");
		mv.addObject("message", message);
		mv.addObject("name", name);
		return mv;
	}

	@RequestMapping("/movies")
	public ModelAndView showMovies(
			@RequestParam(value = "user_id", required = false) Integer userId) {
		ArrayList<Movie> moviesMongoDB;
		ArrayList<Movie> moviesMySQL;
        ArrayList<Movie> moviesNeo4J;


		mysql = new MySQL();
		mongodb = new MongoDB();
        neo4j = new Neo4J();
		if(userId == null){
			moviesMongoDB = mongodb.getAllMovies();
			moviesMySQL = mysql.getAllMovies();
            moviesNeo4J = neo4j.getAllMovies();
            System.out.println("show all Movies");
		}
		else {
			moviesMongoDB = mongodb.getAllMoviesByUserId(userId);
			moviesMySQL = mysql.getMoviesByUserId(userId);
            moviesNeo4J = neo4j.getMoviesByUserId(userId);
            System.out.println("show Movies of user " + userId);
		}





		ModelAndView mv = new ModelAndView("movies");
		mv.addObject("userId", userId);
		mv.addObject("moviesMongoDB", moviesMongoDB);
		mv.addObject("moviesMySQL", moviesMySQL);
        mv.addObject("moviesNeo4J", moviesNeo4J);

        mv.addObject("moviesMongoDBSize", moviesMongoDB.size());
        mv.addObject("moviesMySQLSize", moviesMySQL.size());
        mv.addObject("moviesNeo4JSize", moviesNeo4J.size());


        // Closing connections
        neo4j.close();
		mysql.close();
		return mv;
	}

	@RequestMapping(value = "/movieratings", method = RequestMethod.GET)
	public ModelAndView showMoviesRattings(
			@RequestParam(value = "user_id", required = true) Integer userId) {
		System.out.println("GET /movieratings for user " + userId);

		// TODO: write query to retrieve all movies from DB
		List<Movie> allMovies = new LinkedList<Movie>();
		Genre genre0 = new Genre(0, "genre0");
		Genre genre1 = new Genre(1, "genre1");
		Genre genre2 = new Genre(2, "genre2");
		allMovies.add(new Movie(0, "Titre 0", Arrays.asList(new Genre[] {genre0, genre1})));
		allMovies.add(new Movie(1, "Titre 1", Arrays.asList(new Genre[] {genre0, genre2})));
		allMovies.add(new Movie(2, "Titre 2", Arrays.asList(new Genre[] {genre1})));
		allMovies.add(new Movie(3, "Titre 3", Arrays.asList(new Genre[] {genre0, genre1, genre2})));

		// TODO: write query to retrieve all ratings from the specified user
		List<Rating> ratings = new LinkedList<Rating>();
		ratings.add(new Rating(new Movie(0, "Titre 0", Arrays.asList(new Genre[] {genre0, genre1})), userId, 3));
		ratings.add(new Rating(new Movie(2, "Titre 2", Arrays.asList(new Genre[] {genre1})), userId, 4));

		ModelAndView mv = new ModelAndView("movieratings");
		mv.addObject("userId", userId);
		mv.addObject("allMovies", allMovies);
		mv.addObject("ratings", ratings);

		return mv;
	}

	@RequestMapping(value = "/movieratings", method = RequestMethod.POST)
	public String saveOrUpdateRating(@ModelAttribute("rating") Rating rating) {
		System.out.println("POST /movieratings for user " + rating.getUserId()
				+ ", movie " + rating.getMovie().getId()
				+ ", score " + rating.getScore());

		// TODO: add query which
		//         - add rating between specified user and movie if it doesn't exist
		//         - update it if it does exist

		return "redirect:/movieratings?user_id=" + rating.getUserId();
	}

	@RequestMapping(value = "/recommendations", method = RequestMethod.GET)
	public ModelAndView ProcessRecommendations(
			@RequestParam(value = "user_id", required = true) Integer userId,
			@RequestParam(value = "processing_mode", required = false, defaultValue = "0") Integer processingMode){
		System.out.println("GET /movieratings for user " + userId);

		// TODO: process recommendations for specified user exploiting other users ratings
		//       use different methods depending on processingMode parameter
		Genre genre0 = new Genre(0, "genre0");
		Genre genre1 = new Genre(1, "genre1");
		Genre genre2 = new Genre(2, "genre2");
		List<Rating> recommendations = new LinkedList<Rating>();
		String titlePrefix;
		if (processingMode.equals(0))
			titlePrefix = "0_";
		else if (processingMode.equals(1))
			titlePrefix = "1_";
		else if (processingMode.equals(2))
			titlePrefix = "2_";
		else
			titlePrefix = "default_";
		recommendations.add(new Rating(new Movie(0, titlePrefix + "Titre 0", Arrays.asList(new Genre[] {genre0, genre1})), userId, 5));
		recommendations.add(new Rating(new Movie(1, titlePrefix + "Titre 1", Arrays.asList(new Genre[] {genre0, genre2})), userId, 5));
		recommendations.add(new Rating(new Movie(2, titlePrefix + "Titre 2", Arrays.asList(new Genre[] {genre1})), userId, 4));
		recommendations.add(new Rating(new Movie(3, titlePrefix + "Titre 3", Arrays.asList(new Genre[] {genre0, genre1, genre2})), userId, 3));

		ModelAndView mv = new ModelAndView("recommendations");
		mv.addObject("recommendations", recommendations);

		return mv;
	}
}
