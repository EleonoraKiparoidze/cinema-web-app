package com.rustedbrain.study.course;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.xml.sax.SAXException;

import com.rustedbrain.study.course.model.exception.ResourceException;
import com.rustedbrain.study.course.model.persistence.authorization.Administrator;
import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import com.rustedbrain.study.course.model.persistence.authorization.Member;
import com.rustedbrain.study.course.model.persistence.authorization.Moderator;
import com.rustedbrain.study.course.model.persistence.authorization.Paymaster;
import com.rustedbrain.study.course.model.persistence.cinema.Actor;
import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.Feature;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import com.rustedbrain.study.course.model.persistence.cinema.Genre;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.model.persistence.cinema.Row;
import com.rustedbrain.study.course.model.persistence.cinema.Seat;
import com.rustedbrain.study.course.service.repository.ActorRepository;
import com.rustedbrain.study.course.service.repository.AdministratorRepository;
import com.rustedbrain.study.course.service.repository.CinemaHallRepository;
import com.rustedbrain.study.course.service.repository.CinemaRepository;
import com.rustedbrain.study.course.service.repository.CityRepository;
import com.rustedbrain.study.course.service.repository.FeatureRepository;
import com.rustedbrain.study.course.service.repository.FilmScreeningEventRepository;
import com.rustedbrain.study.course.service.repository.FilmScreeningRepository;
import com.rustedbrain.study.course.service.repository.GenreRepository;
import com.rustedbrain.study.course.service.repository.ManagerRepository;
import com.rustedbrain.study.course.service.repository.MemberRepository;
import com.rustedbrain.study.course.service.repository.ModeratorRepository;
import com.rustedbrain.study.course.service.repository.MovieRepository;
import com.rustedbrain.study.course.service.repository.PaymasterRepository;
import com.rustedbrain.study.course.service.repository.RowRepository;
import com.rustedbrain.study.course.service.repository.SeatRepository;
import com.rustedbrain.study.course.service.resources.ResourceAccessor;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(ManagerRepository managerRepository, ModeratorRepository moderatorRepository,
			PaymasterRepository paymasterRepository, SeatRepository seatRepository, RowRepository rowRepository,
			CinemaHallRepository cinemaHallRepository, FilmScreeningEventRepository filmScreeningEventRepository,
			GenreRepository genreRepository, ActorRepository actorRepository,
			FilmScreeningRepository filmScreeningRepository, CinemaRepository cinemaRepository,
			CityRepository cityRepository, AdministratorRepository administratorRepository,
			MemberRepository memberRepository, MovieRepository movieRepository, FeatureRepository featureRepository) {
		return (args) -> {
			// save a couple of customers
			administratorRepository
					.save(new Administrator("BloodarAdmin", "bloodarkness1996", "bloodaradmin@gmail.com"));
			administratorRepository.save(new Administrator("EleonoraAdmin", "eliakipa1996", "eliakipaadmin@gmail.com"));

			memberRepository.save(new Member("BloodarMember", "bloodarkness1996", "bloodarmember@gmail.com"));
			memberRepository.save(new Member("EleonoraMember", "eliakipa1996", "eliakipamember@gmail.com"));

			paymasterRepository
					.save(new Paymaster("BloodarPaymaster", "bloodarkness1996", "bloodarpaymaster@gmail.com"));
			paymasterRepository.save(new Paymaster("EleonoraPaymaster", "eliakipa1996", "eliakipapaymaster@gmail.com"));

			moderatorRepository
					.save(new Moderator("BloodarModerator", "bloodarkness1996", "bloodarmoderator@gmail.com"));
			moderatorRepository.save(new Moderator("EleonoraModerator", "eliakipa1996", "eliakipamoderator@gmail.com"));

			Manager manager1 = new Manager("BloodarManager", "bloodarkness1996", "bloodarmanager@gmail.com");
			managerRepository.save(manager1);
			managerRepository.save(new Manager("EleonoraManager", "eliakipa1996", "eliakipamanager@gmail.com"));

			Set<Feature> features =
					getFeatureSet("IMAX", "Accessible for disabled people", "M cafe", "TWINS", "Original voice");
			featureRepository.saveAll(features);

			City city1 = new City("New York");
			City city2 = new City("New Jersey");
			City city3 = new City("Washington");
			cityRepository.save(city1);
			cityRepository.save(city2);
			cityRepository.save(city3);

			Cinema cinema1 = new Cinema("Lincoln Plaza Cinema", city1);
			cinema1.setLocation("1886 Broadway");
			Cinema cinema2 = new Cinema("Village East Cinema", city1);
			cinema2.setLocation("189 2nd Ave");
			Cinema cinema3 = new Cinema("Hillsborough Cinemas", city2);
			cinema3.setLocation("111 Raider Blvd, Hillsborough Township");
			Cinema cinema4 = new Cinema("Landmark E Street Cinema", city3);
			cinema4.setLocation("555 11th St NW");
			Cinema cinema5 = new Cinema("Angelika Pop-Up at Union Market", city3);
			cinema5.setLocation("550 Penn St NE");
			Cinema cinema6 = new Cinema("AMC Mazza Gallerie", city3);
			cinema6.setLocation("5300 Wisconsin Ave NW");
			cinema6.setManager(manager1);
			cinema6.setFeatures(features);

			cinemaRepository.save(cinema1);
			cinemaRepository.save(cinema2);
			cinemaRepository.save(cinema3);
			cinemaRepository.save(cinema4);
			cinemaRepository.save(cinema5);
			cinemaRepository.save(cinema6);

			Set<CinemaHall> cinemaHalls1 = getTestCinemaHallSet(3, cinema5);
			Set<CinemaHall> cinemaHalls2 = getTestCinemaHallSet(5, cinema6);

			cinemaHallRepository.saveAll(cinemaHalls1);
			cinemaHallRepository.saveAll(cinemaHalls2);

			for (CinemaHall cinemaHall : cinemaHalls2) {
				cinemaHallRepository.save(cinemaHall);
				List<Row> row = cinemaHall.getRows();
				for (Row row1 : row) {
					rowRepository.save(row1);
					for (Seat seat : row1.getSeats()) {
						seatRepository.save(seat);
					}
				}
			}

			Set<Actor> hellBoyActorSet = getActorSet("Ron Perlman", "Selma Blair", "Jeffrey Tambor", "Karel Roden",
					"Rupert Evans", "John Hurt");
			// actorRepository.saveAll(hellBoyActorSet);

			Set<Actor> theGreatGatsbyActorSet = getActorSet("Leonardo DiCaprio", "Tobey Maguire", "Carey Mulligan",
					"Joel Edgerton", "Isla Fisher", "Jason Clarke", "Amitabh Bachchan");
			// actorRepository.saveAll(theGreatGatsbyActorSet);

			Set<Actor> theTerminatorActorSet =
					getActorSet("Arnold Schwarzenegger", "Michael Biehn", "Linda Hamilton", "Paul Winfield");
			// actorRepository.saveAll(theTerminatorActorSet);

			Set<Genre> genreSet = getGenreSet("adventure", "comedy", "drama", "horror", "historical");
			genreRepository.saveAll(genreSet);

			Movie movie1 = createMovie("Великий Гэтсби", "The Great Gatsby", 13,
					"Ник Кэррауэй, выпускник Йельского университета, находится в санатории, где лечится от алкоголизма. Он рассказывает о человеке по имени Гэтсби, с которым его свела судьба в Нью-Йорке. Нику трудно говорить об этом, и врач предлагает ему описать эту историю на бумаге.\n"
							+ "Свой рассказ Ник начинает воспоминанием о том, как в 1922 году переехал со Среднего Запада в Нью-Йорк и арендовал дом в Уэст Эгге на Лонг-Айленде. Ник посещает роскошное поместье Тома и Дэйзи Бьюкененов. Дэйзи была троюродной сестрой Ника, а её муж, Том, некогда играл в поло в Йеле, а ныне наслаждается богатством. Дейзи знакомит Ника со своей подругой, известной гольфисткой Джордан Бейкер. За ужином внезапно начинает звонить телефон: это звонит любовница Тома, о которой всем уже давно известно.\n"
							+ "Для встреч с этой любовницей, Миртл Уилсон, женой ничего не подозревающего автомеханика Джорджа из шахтёрского района штата Нью-Йорк, Том арендует квартиру в городе. Том приглашает туда Ника, где тот также знакомится с Кэтрин, сестрой Миртл, и с четой МакКи, друзьями Миртл. Ночь заканчивается всеобщей попойкой и разбитым носом Миртл, раздражавшей Тома упоминанием имени Дэйзи.",
					new Date(), new Date(), new GregorianCalendar(2013, 1, 1).getTime(), theGreatGatsbyActorSet,
					genreSet, 130, "C:\\Users\\User\\Downloads\\greatgatsby.jpg", "http://www.youtube.com/v/rARN6agiW7o"

			);
			movieRepository.save(movie1);

			Movie movie2 = createMovie("Терминатор", "The Terminator", 18,
					"Созданный в недалеком будущем военный компьютер Скайнет развязал ядерную войну, а затем поработил остатки человечества. Однако у людей появился великий лидер — Джон Коннор, организовавший Сопротивление и сумевший выиграть Войну. Поверженный Скайнет отправляет в прошлое робота-терминатора с целью убить мать Джона Коннора — Сару Коннор. Сара работает обычной официанткой и не подозревает о своей великой миссии. На её защиту люди посылают человека — сержанта Сопротивления Кайла Риз. Силы противников не равны. Ризу противостоит чрезвычайно мощная боевая машина, неуязвимая для огнестрельного оружия. Терминатор внешне неотличим от человека: он специально предназначен для внедрения в человеческое общество и уничтожения людей. Ризу приходится не только бороться с роботом, но и убеждать шокированную Сару, а также полицию, которая принимает его за сумасшедшего террориста. В тяжёлых испытаниях недоверие, возникшее между Сарой и Ризом, уступает место симпатии и сменяется пылкой страстью. В результате на свет появляется Джон. В финале Риз погибает, но Саре удаётся уничтожить терминатора. При этом остатки его процессора и часть металлической руки попадают к сотрудникам компании «Кибердайн Системс» — будущей создательнице Скайнет. Таким образом, попытка коррекции будущего приводит к противоположному результату — всё встаёт на свои места.",
					new Date(), new Date(), new GregorianCalendar(1984, 1, 1).getTime(), theTerminatorActorSet,
					genreSet, 130, "C:\\Users\\User\\Downloads\\terminator.jpg",
					"http://www.youtube.com/v/k64P4l2Wmeg");
			movieRepository.save(movie2);

			Movie movie3 = createMovie("Hellboy", "Hellboy ", 13,
					"In 1944, with the help of Russian mystic Grigori Rasputin, the Nazis build a dimensional portal off the coast of Scotland and intend to free the Ogdru Jahad—monstrous entities imprisoned in deep space—to aid them in defeating the Allies. Rasputin opens the portal with the aid of his disciples, Ilsa von Haupstein and Obersturmbannführer Karl Ruprecht Kroenen, member of the Thule Society and Adolf Hitler's top assassin. An Allied team is sent to destroy the portal, guided by a young scientist named Trevor Bruttenholm, who is well-versed in the occult. The German team is killed and the portal is destroyed—in the process absorbing Rasputin—while Haupstein and Kroenen escape. The Allied team discovers that an infant demon with a right hand of stone came through the portal; they dub him \"Hellboy\" and Bruttenholm adopts him.",
					new Date(), new Date(), new GregorianCalendar(2004, 4, 30).getTime(), hellBoyActorSet, genreSet,
					122, "C:\\Users\\User\\Downloads\\hellboy.jpg", "http://www.youtube.com/v/kA9vtXbbhVs");
			movieRepository.save(movie3);

			FilmScreening filmScreening = createFilmScreening(cinemaRepository.getOne(cinema6.getId()),
					movieRepository.getOne(movie1.getId()));
			FilmScreening filmScreening2 = createFilmScreening(cinemaRepository.getOne(cinema6.getId()),
					movieRepository.getOne(movie2.getId()));
			FilmScreening filmScreening3 = createFilmScreening(cinemaRepository.getOne(cinema6.getId()),
					movieRepository.getOne(movie3.getId()));

			filmScreening.setFilmScreeningEvents(createFilmScreeningEvents(filmScreening,
					(CinemaHall) cinemaHalls2.toArray()[0], LocalTime.of(14, 30, 00), LocalTime.of(16, 30, 00),
					LocalTime.of(18, 30, 00), LocalTime.of(20, 30, 00), LocalTime.of(22, 30, 00)));
			filmScreening2.setFilmScreeningEvents(createFilmScreeningEvents(filmScreening2,
					(CinemaHall) cinemaHalls2.toArray()[1], LocalTime.of(17, 30, 00)));
			filmScreening2.setFilmScreeningEvents(createFilmScreeningEvents(filmScreening2,
					(CinemaHall) cinemaHalls2.toArray()[1], LocalTime.of(17, 30, 00)));

			filmScreeningRepository.save(filmScreening);
			filmScreeningRepository.save(filmScreening2);
			filmScreeningRepository.save(filmScreening3);

			log.info(filmScreeningRepository.findAll().toString());
			log.info(filmScreeningEventRepository.findAll().toString());
			log.info(cinemaRepository.getOne(cinema6.getId()).getFilmScreenings().toString());
		};
	}

	private Movie createMovie(String localizedName, String originalName, int minAge, String description,
			Date registrationDate, Date lastAccesDate, Date releaseDate, Set<Actor> actors, Set<Genre> genres,
			int timeMinutes, String posterPath, String trailerURL) {
		Movie movie = new Movie();
		movie.setLocalizedName(localizedName);
		movie.setOriginalName(originalName);
		movie.setMinAge(minAge);
		movie.setDescription(description);
		movie.setRegistrationDate(registrationDate);
		movie.setLastAccessDate(lastAccesDate);
		movie.setReleaseDate(releaseDate);
		movie.setActors(actors);
		movie.setGenres(genres);
		movie.setPosterPath(posterPath);
		movie.setTimeMinutes(timeMinutes);
		movie.setTrailerURL(trailerURL);
		return movie;
	}

	private FilmScreening createFilmScreening(Cinema cinema, Movie movie) {
		FilmScreening filmScreening = new FilmScreening();
		filmScreening.setStartDate(new Date());
		filmScreening
				.setEndDate(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(new Random().nextInt(5) + 2)));
		filmScreening.setRegistrationDate(new Date());
		filmScreening.setLastAccessDate(new Date());
		filmScreening.setMovie(movie);
		filmScreening.setCinema(cinema);
		return filmScreening;
	}

	private List<FilmScreeningEvent> createFilmScreeningEvents(FilmScreening filmScreening, CinemaHall cinemaHall,
			LocalTime... times) {
		List<FilmScreeningEvent> filmScreeningEvents = new ArrayList<>();

		for (LocalTime time : times) {
			final int days_increment = 1;
			LocalDate currDate =
					LocalDate.from(filmScreening.getStartDate().toInstant().atZone(ZoneId.systemDefault()));
			LocalDate endDate = LocalDate.from(filmScreening.getEndDate().toInstant().atZone(ZoneId.systemDefault()));
			while ( !currDate.equals(endDate.plusDays(days_increment)) ) {
				FilmScreeningEvent filmScreeningEvent = new FilmScreeningEvent();
				filmScreeningEvent.setRegistrationDate(new Date());
				filmScreeningEvent.setLastAccessDate(new Date());
				filmScreeningEvent.setFilmScreening(filmScreening);
				filmScreeningEvent.setTime(Time.valueOf(time));
				filmScreeningEvent.setCinemaHall(cinemaHall);
				filmScreeningEvent.setDate(java.sql.Date.valueOf(currDate));
				filmScreeningEvents.add(filmScreeningEvent);
				currDate = currDate.plusDays(days_increment);
			}
		}

		return filmScreeningEvents;
	}

	private Set<CinemaHall> getTestCinemaHallSet(int cinemaHallsCount, Cinema cinema)
			throws ParserConfigurationException, IOException, SAXException, ResourceException {
		Set<CinemaHall> cinemaHalls = new HashSet<>();
		for (int i = 1; i <= cinemaHallsCount; i++) {
			CinemaHall cinemaHall = new CinemaHall();
			cinemaHall.setCinema(cinema);
			cinemaHall.setLastAccessDate(new Date());
			cinemaHall.setRegistrationDate(new Date());
			cinemaHall.setName(String.valueOf(i));

			Map<Integer, List<Integer>> cinemaHallSeatCoordinateMap =
					new ResourceAccessor().getCinemaHallSeatCoordinateMap(cinemaHall);

			List<Row> rows = new ArrayList<>();
			cinemaHallSeatCoordinateMap.entrySet().stream().forEach(enty -> {
				Row row = new Row();
				row.setCinemaHall(cinemaHall);
				row.setNumber(enty.getKey() + 1);
				row.setRegistrationDate(new Date());
				row.setLastAccessDate(new Date());

				Set<Seat> seats = new HashSet<>();
				enty.getValue().forEach(value -> {
					Seat seat = new Seat(value + 1, 1, 12.50);
					seat.setRow(row);
					seat.setLastAccessDate(new Date());
					seat.setRegistrationDate(new Date());
					seats.add(seat);
				});
				row.setSeats(seats);
				rows.add(row);
			});

			cinemaHall.setRows(rows);
			cinemaHalls.add(cinemaHall);
		}
		return cinemaHalls;
	}

	private Set<Genre> getGenreSet(String... genres) {
		Set<Genre> genreSet = new HashSet<>(genres.length);
		for (String genre : genres) {
			Genre newGenre = new Genre();
			newGenre.setRegistrationDate(new Date());
			newGenre.setLastAccessDate(new Date());
			newGenre.setName(genre);
			genreSet.add(newGenre);
		}
		return genreSet;
	}

	private Set<Feature> getFeatureSet(String... features) {
		Set<Feature> featureSet = new HashSet<>(features.length);
		for (String feature : features) {
			Feature newFeature = new Feature();
			newFeature.setRegistrationDate(new Date());
			newFeature.setLastAccessDate(new Date());
			newFeature.setName(feature);
			featureSet.add(newFeature);
		}
		return featureSet;
	}

	private Set<Actor> getActorSet(String... nameSurnames) {
		Set<Actor> actorSet = new HashSet<>(nameSurnames.length);
		for (String nameSurname : nameSurnames) {
			Actor actor = new Actor();
			actor.setRegistrationDate(new Date());
			actor.setLastAccessDate(new Date());
			String[] nameSurnameArray = nameSurname.split(" ");
			if ( nameSurnameArray.length < 2 ) {
				continue;
			}
			actor.setName(nameSurnameArray[0]);
			actor.setSurname(nameSurnameArray[1]);
			actorSet.add(actor);
		}
		return actorSet;
	}
}
