package cat.itacademy.barcelonactiva.abdellaoui.fethi.s05.t02.jocdedausMDB.model.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import cat.itacademy.barcelonactiva.abdellaoui.fethi.s05.t02.jocdedausMDB.model.mappers.PlayerMapper;
import cat.itacademy.barcelonactiva.abdellaoui.fethi.s05.t02.jocdedausMDB.model.mappers.GameMapper;
import cat.itacademy.barcelonactiva.abdellaoui.fethi.s05.t02.jocdedausMDB.model.domain.Game;
import cat.itacademy.barcelonactiva.abdellaoui.fethi.s05.t02.jocdedausMDB.model.domain.Player;
import cat.itacademy.barcelonactiva.abdellaoui.fethi.s05.t02.jocdedausMDB.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.abdellaoui.fethi.s05.t02.jocdedausMDB.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.abdellaoui.fethi.s05.t02.jocdedausMDB.model.repositories.GameRepository;
import cat.itacademy.barcelonactiva.abdellaoui.fethi.s05.t02.jocdedausMDB.model.repositories.PlayerRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlayerService {
	
	private PlayerRepository playerRepository;
	private GameRepository gameRepository;
	
	static Random random() {
		return new Random();
	}
	
	public List<PlayerDTO> getPlayers(){ 
		return playerRepository.findAll().stream().map(PlayerMapper::toDTO).toList();
	}
	
	public PlayerDTO addPlayer(PlayerDTO playerDTO){
		Player player = PlayerMapper.toPlayer(playerDTO);
		return PlayerMapper.toDTO(playerRepository.save(player));		
	}

	public PlayerDTO updatePlayer(String nom, PlayerDTO playerDTO) {
		Player player = PlayerMapper.toPlayer(playerDTO);
		player.setUsername(nom);
		return PlayerMapper.toDTO(playerRepository.save(player));
	}
	
	public List<GameDTO> getPlayerGames(String id){ 
		Optional<Player> player = playerRepository.findById(id);
		if (!player.isEmpty()) {
		return gameRepository.findByPlayer(player.get()).stream().map(GameMapper::toDTO).toList();
		}
		return null;
	}
	
	public GameDTO playGame(String id){

		Optional<Player> player = playerRepository.findById(id);
		
		if (!player.isEmpty()){

			Game game = new Game();
			game.setDice1((byte) (random().nextInt(6) + 1));
			game.setDice2((byte) (random().nextInt(6) + 1));			
			game.setPlayer(player.get());
			return GameMapper.toDTO(gameRepository.save(game));
		}
		return null;
	}

	public List<String> getUsernames() {

		List<String> usernames = playerRepository.findAll().stream().map(p -> p.getUsername()).toList();
		return usernames;
	}

	public PlayerDTO getPlayerDTO(String id) {

		Optional<Player> p = playerRepository.findById(id);
		if (p.isPresent()) {
			return PlayerMapper.toDTO(p.get());
		}
		return null;
	}

	public PlayerDTO deletePlayerGames(String id) {
		Optional<Player> player = playerRepository.findById(id);
		if (player.isPresent()) {
			List<Game> games = gameRepository.findByPlayer(player.get());
			gameRepository.deleteAll(games);
		}
		return PlayerMapper.toDTO(player.get());
		
	}
	

}
