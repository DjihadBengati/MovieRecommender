package com.camillepradel.movierecommender.controller;

import java.util.ArrayList;

import com.camillepradel.movierecommender.databases.MongoDB;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.camillepradel.movierecommender.model.Movie;

@Controller
public class MainController {
	String message = "Welcome to Spring MVC!";
 
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
		ArrayList<Movie> moviesList;

		MongoDB mongodb = new MongoDB();
		if(userId == null){
			moviesList = mongodb.getAllMovies();
			System.out.println(moviesList.size());
		}
		else {
			moviesList = mongodb.getAllMoviesByUserId(userId);
		}
		System.out.println("show Movies of user " + userId);

		// TODO: write query to retrieve all movies from DB or all movies rated by user with id userId,
		// depending on whether or not a value was given for userId

		ModelAndView mv = new ModelAndView("movies");
		mv.addObject("userId", userId);
		mv.addObject("movies", moviesList);
		return mv;
	}
}
