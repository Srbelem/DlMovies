package com.devsuperior.dsmovie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmovie.dto.MovieDto;
import com.devsuperior.dsmovie.dto.ScoreDto;
import com.devsuperior.dsmovie.entitis.Movie;
import com.devsuperior.dsmovie.entitis.Score;
import com.devsuperior.dsmovie.entitis.User;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.repositories.UserRepository;

@Service
public class ScoreService {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private ScoreRepository scoreRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public MovieDto saveScore(ScoreDto dto) {
		
		User user = userRepository.findByEmail(dto.getEmail());
		if(user == null) {
			user = new User();
			user.setEmail(dto.getEmail());
			user = userRepository.saveAndFlush(user);
		}
		
		Movie movie = movieRepository.findById(dto.getMovieId()).get();
			Score score = new Score();
			score.setMovie(movie);
			score.setUser(user);
			score.setValue(dto.getScore());
			
			score = scoreRepository.saveAndFlush(score);
			
			double sum = 0.0;
			for(Score s : movie.getScores()) {
				sum = sum + s.getValue();
			}
			
			double svg = sum / movie.getScores().size();
			movie.setScore(svg);
			movie.setCount(movie.getScores().size());
			
			movie = movieRepository.save(movie);
			return new MovieDto(movie);
	}
}
