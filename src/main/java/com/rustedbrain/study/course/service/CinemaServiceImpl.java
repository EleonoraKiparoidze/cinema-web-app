package com.rustedbrain.study.course.service;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.rustedbrain.study.course.model.dto.TicketInfo;
import com.rustedbrain.study.course.model.exception.ResourceException;
import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import com.rustedbrain.study.course.model.persistence.authorization.Member;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.model.persistence.cinema.Actor;
import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.CinemaHall;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.Comment;
import com.rustedbrain.study.course.model.persistence.cinema.CommentReputation;
import com.rustedbrain.study.course.model.persistence.cinema.Feature;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreeningEvent;
import com.rustedbrain.study.course.model.persistence.cinema.Genre;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.model.persistence.cinema.Row;
import com.rustedbrain.study.course.model.persistence.cinema.Seat;
import com.rustedbrain.study.course.model.persistence.cinema.Ticket;
import com.rustedbrain.study.course.service.repository.ActorRepository;
import com.rustedbrain.study.course.service.repository.CinemaHallRepository;
import com.rustedbrain.study.course.service.repository.CinemaRepository;
import com.rustedbrain.study.course.service.repository.CityRepository;
import com.rustedbrain.study.course.service.repository.CommentRepository;
import com.rustedbrain.study.course.service.repository.CommentReputationRepository;
import com.rustedbrain.study.course.service.repository.FeatureRepository;
import com.rustedbrain.study.course.service.repository.FilmScreeningEventRepository;
import com.rustedbrain.study.course.service.repository.FilmScreeningRepository;
import com.rustedbrain.study.course.service.repository.GenreRepository;
import com.rustedbrain.study.course.service.repository.MemberRepository;
import com.rustedbrain.study.course.service.repository.MovieRepository;
import com.rustedbrain.study.course.service.repository.RowRepository;
import com.rustedbrain.study.course.service.repository.SeatRepository;
import com.rustedbrain.study.course.service.repository.TicketRepository;
import com.rustedbrain.study.course.service.resources.ResourceAccessor;
import com.rustedbrain.study.course.service.util.GoogleMapApiUtil;
import com.rustedbrain.study.course.service.util.Validator;

@Service
public class CinemaServiceImpl implements CinemaService {

	private final Logger logger = Logger.getLogger(CinemaServiceImpl.class.getName());

	@Autowired
	private ResourceAccessor resourceAccessor;
	private CityRepository cityRepository;
	private CinemaRepository cinemaRepository;
	private CinemaHallRepository cinemaHallRepository;
	private FilmScreeningEventRepository filmScreeningEventRepository;
	private FilmScreeningRepository filmScreeningRepository;
	private TicketRepository ticketRepository;
	private SeatRepository seatRepository;
	private RowRepository rowRepository;
	private MemberRepository memberRepository;
	private MovieRepository movieRepository;
	private CommentRepository commentRepository;
	private CommentReputationRepository commentReputationRepository;
	private ActorRepository actorRepository;
	private GenreRepository genreRepository;
	private FeatureRepository featureRepository;

	@Autowired
	public void setCommentReputationRepository(CommentReputationRepository commentReputationRepository) {
		this.commentReputationRepository = commentReputationRepository;
	}

	@Autowired
	public void setCommentRepository(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	@Autowired
	public void setMovieRepository(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	@Autowired
	public void setResourceAccessor(ResourceAccessor resourceAccessor) {
		this.resourceAccessor = resourceAccessor;
	}

	@Autowired
	public void setFilmScreeningRepository(FilmScreeningRepository filmScreeningRepository) {
		this.filmScreeningRepository = filmScreeningRepository;
	}

	@Autowired
	public void setMemberRepository(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Autowired
	public void setSeatRepository(SeatRepository seatRepository) {
		this.seatRepository = seatRepository;
	}

	@Autowired
	public void setRowRepository(RowRepository rowRepository) {
		this.rowRepository = rowRepository;
	}

	@Autowired
	public void setTicketRepository(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	@Autowired
	public void setCityRepository(CityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}

	@Autowired
	public void setCinemaHallRepository(CinemaHallRepository cinemaHallRepository) {
		this.cinemaHallRepository = cinemaHallRepository;
	}

	@Autowired
	public void setCinemaRepository(CinemaRepository cinemaRepository) {
		this.cinemaRepository = cinemaRepository;
	}

	@Autowired
	public void setFilmScreeningEventRepository(FilmScreeningEventRepository filmScreeningEventRepository) {
		this.filmScreeningEventRepository = filmScreeningEventRepository;
	}

	@Autowired
	public void setActorRepository(ActorRepository actorRepository) {
		this.actorRepository = actorRepository;
	}

	@Autowired
	public void setGenreRepository(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	@Autowired
	public void setFeatureRepository(FeatureRepository featureRepository) {
		this.featureRepository = featureRepository;
	}

	@Override
	public List<City> getCities() {
		return cityRepository.findAll();
	}

	@Override
	public Page<City> getCitiesPage(int page, int pageSize) {
		return cityRepository.findAll(PageRequest.of(page, pageSize));
	}

	@Override
	public Page<Cinema> getCinemasPage(int page, int pageSize) {
		return getCinemasPage(page, pageSize, new Sort(Sort.Direction.DESC, "name"));
	}

	@Override
	public Page<Cinema> getCinemasPage(int page, int pageSize, Sort sort) {
		return cinemaRepository.findAll(PageRequest.of(page, pageSize, sort));
	}

	@Override
	public Cinema getCinema(long id) {
		return cinemaRepository.getOne(id);
	}

	@Override
	public void deleteCity(String cityName) {
		if ( cityName == null || cityName.isEmpty() ) {
			throw new IllegalArgumentException("City name cannot be empty");
		} else {
			Optional<City> optionalCity = Optional.ofNullable(cityRepository.findByName(cityName));
			if ( optionalCity.isPresent() ) {
				cityRepository.delete(optionalCity.get());
			} else {
				throw new IllegalArgumentException("City with specified name not found");
			}
		}
	}

	@Override
	public Cinema getCinema(Long cinemaId) {
		return cinemaRepository.getOne(cinemaId);
	}

	@Override
	public FilmScreeningEvent getFilmScreeningEvent(Long filmScreeningEventId) {
		return filmScreeningEventRepository.getOne(filmScreeningEventId);
	}

	private Location getUserLocation(String ipAddress) throws IOException {
		try {
			LookupService lookupService = new LookupService(resourceAccessor.getIPGeoDatabaseFile(),
					LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
			Optional<Location> optionalLocation = Optional.ofNullable(lookupService.getLocation(ipAddress));
			if ( optionalLocation.isPresent() ) {
				return optionalLocation.get();
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error occurred while getting location by user's IP address", e);
			throw e;
		}
		throw new NoSuchElementException("Location for entered IP address not found");
	}

	@Override
	public Optional<City> getCityByName(String cityName) {
		return Optional.ofNullable(cityRepository.findByName(cityName));
	}

	@Override
	public Optional<Cinema> getNearestCinema(InetAddress address) throws IOException {
		Location userLocation = getUserLocation(address.getHostAddress());
		String userStreetLocation =
				GoogleMapApiUtil.getAddressByCoordinates(userLocation.longitude, userLocation.latitude);

		Optional<City> optionalCity = getCityByName(userLocation.city);
		if ( optionalCity.isPresent() ) {
			return calculateNearestCinemaToAddress(optionalCity.get().getCinemas(), userStreetLocation);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<City> getCityByInetAddress(InetAddress address) throws IOException {
		return getCityByName(getUserLocation(address.getHostAddress()).city);
	}

	@Override
	public List<FilmScreening> getTodayCinemaFilmScreenings(Cinema cinema) {
		List<FilmScreening> filmScreenings = new ArrayList<>();
		for (FilmScreening filmScreening : cinema.getFilmScreenings()) {
			if ( filmScreening.isAvailableToBuy() && filmScreening.getStartDate().before(Date.from(Instant.now()))
					&& filmScreening.getEndDate().after(Date.from(Instant.now())) ) {
				FilmScreening todayFilmScreening = new FilmScreening();
				todayFilmScreening.setMovie(filmScreening.getMovie());
				todayFilmScreening.setFilmScreeningEvents(filmScreening.getFilmScreeningEvents().stream().filter(
						filmScreeningEvent -> filmScreeningEvent.getTime().toLocalTime().isAfter(LocalTime.now()))
						.collect(Collectors.toList()));
				filmScreenings.add(todayFilmScreening);
			}
		}
		return filmScreenings;
	}

	@Override
	public Optional<Cinema> getNearestCinema(InetAddress address, City city) throws IOException {
		Location userLocation = getUserLocation(address.getHostAddress());
		String userStreetLocation =
				GoogleMapApiUtil.getAddressByCoordinates(userLocation.longitude, userLocation.latitude);
		return calculateNearestCinemaToAddress(city.getCinemas(), userStreetLocation);
	}

	@Override
	public void deleteCinema(Long id) {
		cinemaRepository.deleteById(id);
	}

	@Override
	public void deleteCity(Long id) {
		cityRepository.deleteById(id);
	}

	@Override
	public Map<String, String> getHelpTittleTextMap()
			throws ParserConfigurationException, ResourceException, SAXException, IOException {
		return resourceAccessor.getHelpTittleTextMap();
	}

	@Override
	public Map<Integer, List<Integer>> getCinemaHallSeatCoordinateMap(CinemaHall cinemaHall)
			throws ParserConfigurationException, ResourceException, SAXException, IOException {
		return resourceAccessor.getCinemaHallSeatCoordinateMap(cinemaHall);
	}

	@Override
	public List<Cinema> getCityCinemas(Long id) {
		return cinemaRepository.getCinemasByCityId(id);
	}

	@Override
	public List<Seat> getSeats(List<Long> seatIds) {
		return seatRepository.findAllById(seatIds);
	}

	@Override
	public List<TicketInfo> reserveTickets(String name, String surname, String login,
			FilmScreeningEvent filmScreeningEvent, List<Seat> seats) {
		Optional<Member> optionalMember = Optional.ofNullable(memberRepository.findByLogin(login));
		if ( optionalMember.isPresent() ) {
			List<TicketInfo> tickets = new ArrayList<>();
			for (Seat seat : seats) {
				Ticket ticket = new Ticket();
				Date date = new Date();
				ticket.setRegistrationDate(date);
				ticket.setLastAccessDate(date);
				ticket.setSeat(seat);
				ticket.setMember(optionalMember.get());
				ticket.setClientName(name);
				ticket.setClientSurname(surname);
				ticket.setEvent(filmScreeningEvent);
				ticket.setReserved(true);
				ticketRepository.save(ticket);
				tickets.add(new TicketInfo(ticket));
			}
			return tickets;
		} else {
			throw new IllegalArgumentException("Only members can buy tickets");
		}
	}

	@Override
	public List<TicketInfo> reserveTickets(String name, String surname, FilmScreeningEvent filmScreeningEvent,
			List<Seat> seats) {
		List<TicketInfo> tickets = new ArrayList<>();
		for (Seat seat : seats) {
			Ticket ticket = new Ticket();
			Date date = new Date();
			ticket.setRegistrationDate(date);
			ticket.setLastAccessDate(date);
			ticket.setSeat(seat);
			ticket.setClientName(name);
			ticket.setClientSurname(surname);
			ticket.setEvent(filmScreeningEvent);
			ticket.setReserved(true);
			ticketRepository.save(ticket);
			tickets.add(new TicketInfo(ticket));
		}
		return tickets;
	}

	@Override
	public List<Actor> getActors() {
		return actorRepository.findAll();
	}

	@Override
	public List<Genre> getGenres() {
		return genreRepository.findAll();
	}

	@Override
	public Member getMemberByLogin(String userLogin) {
		return memberRepository.findByLogin(userLogin);
	}

	@Override
	public Set<FilmScreening> getDayFilmScreenings(long cinemaId, LocalDate day) {
		Set<FilmScreening> filmScreenings =
				new HashSet<>(filmScreeningRepository.getFilmScreening(cinemaId, java.sql.Date.valueOf(day)));

		for (FilmScreening filmScreening : filmScreenings) {
			filmScreening.getFilmScreeningEvents()
					.removeIf(filmScreeningEvent -> !filmScreeningEvent.getDate().equals(java.sql.Date.valueOf(day)));
		}

		return filmScreenings;
	}

	@Override
	public List<TicketInfo> getTicketsInfo(List<Long> ticketIds) {
		return ticketRepository.findAllById(ticketIds).stream().map(TicketInfo::new).collect(Collectors.toList());
	}

	@Override
	public void renameCinema(long id, String value) {
		cinemaRepository.renameCinema(id, value);
	}

	@Override
	public void registerMember(String login, String password, String name, String surname, long cityId,
			LocalDate birthday, String mail) {
		if ( !Validator.LOGIN_VALIDATOR.isValid(login) ) {
			throw new IllegalArgumentException(
					"Login is not valid. You can use only characters and digits in your login.");
		} else if ( !Validator.MAIL_VALIDATOR.isValid(mail) ) {
			throw new IllegalArgumentException("Mail is not valid. Please check inputted mail.");
		}
		Member member = new Member(login, password, mail);
		member.setName(name);
		member.setSurname(surname);
		member.setCity(cityRepository.getOne(cityId));
		member.setLastAccessDate(new Date());
		member.setRegistrationDate(new Date());
		member.setBirthday(Date.from(birthday.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		memberRepository.save(member);
	}

	@Override
	public Optional<Movie> getMovie(long id) {
		return movieRepository.findById(id);
	}

	@Override
	public void createMessage(Movie movie, User authenticUser, String message) {
		Comment comment = new Comment(authenticUser, movie, message);
		comment.setLastAccessDate(new Date());
		comment.setRegistrationDate(new Date());
		commentRepository.save(comment);
	}

	@Override
	public void deleteComment(long id) {
		commentRepository.deleteById(id);
	}

	@Override
	public void addMinusCommentReputation(long commentId, User user) {
		CommentReputation commentReputation = createMinusCommentReputation(commentId, user);
		commentReputationRepository.save(commentReputation);
	}

	@Override
	public void invertCommentReputation(CommentReputation commentReputation) {
		commentReputationRepository.setPlus(commentReputation.getId(), !commentReputation.isPlus());
	}

	@Override
	public void addPlusCommentReputation(long commentId, User user) {
		CommentReputation commentReputation = createMinusCommentReputation(commentId, user);
		commentReputation.setPlus(true);
		commentReputationRepository.save(commentReputation);
	}

	@Override
	public void renameCity(City selectedCity, String newCityName) {
		cityRepository.renameCity(selectedCity.getId(), newCityName);
	}

	@Override
	public void createCity(String name) {
		City city = new City(name);
		cityRepository.save(city);
	}

	@Override
	public void editCinema(Cinema selectedCinema, String newCinemaName, City newCity, String newCinemaLocation,
			Manager manager) {
		cinemaRepository.editCinema(selectedCinema.getId(), newCinemaName, newCity, newCinemaLocation, manager);
	}

	@Override
	public void createCinema(City city, String name, String street, Manager manager) {
		if ( name == null || name.isEmpty() ) {
			throw new IllegalArgumentException("Cinema name cannot be empty");
		} else if ( street == null || street.isEmpty() ) {
			throw new IllegalArgumentException("Cinema street cannot be empty");
		} else {
			cinemaRepository.save(new Cinema(city, name, street, manager));
		}
	}

	@Override
	public void editCinemaHall(CinemaHall selectedCinemaHall, String newCinemaHallName, Cinema newCinema) {
		cinemaHallRepository.editCinemaHall(selectedCinemaHall.getId(), newCinemaHallName, newCinema);
	}

	@Override
	public void deleteCinemaHall(long id) {
		cinemaHallRepository.deleteById(id);
	}

	private CommentReputation createMinusCommentReputation(long commentId, User user) {
		CommentReputation commentReputation = new CommentReputation();
		commentReputation.setLastAccessDate(new Date());
		commentReputation.setRegistrationDate(new Date());
		commentReputation.setUser(user);
		commentReputation.setComment(commentRepository.getOne(commentId));
		commentReputationRepository.save(commentReputation);
		return commentReputation;
	}

	private Optional<Cinema> calculateNearestCinemaToAddress(Set<Cinema> cinemas, String userStreetLocation)
			throws IOException {
		TreeMap<Double, Cinema> cinemaDistanceMap = new TreeMap<>();
		for (Cinema cinema : cinemas) {
			cinemaDistanceMap.put(
					GoogleMapApiUtil.getDistanceBetweenAddresses(userStreetLocation, cinema.getLocation()), cinema);
		}
		Optional<Map.Entry<Double, Cinema>> optionalDistanceCinema = Optional.ofNullable(cinemaDistanceMap.lastEntry());
		return optionalDistanceCinema.map(Map.Entry::getValue);
	}

	@Override
	public long createCinemaHall(String cinemaHallName, Cinema cinema) {
		CinemaHall cinemaHall = new CinemaHall(cinemaHallName);
		cinemaHall.setCinema(cinema);
		cinemaHallRepository.save(cinemaHall);
		return cinemaHall.getId();
	}

	@Override
	public void setCinemaHallSeatMap(long cinameHallId, Map<Integer, List<Integer>> cinemaHallSeatCoordinateMultiMap)
			throws ParserConfigurationException, ResourceException, SAXException, IOException {
		resourceAccessor.setCinemaHallSeatMap(cinameHallId, cinemaHallSeatCoordinateMultiMap);
	}

	@Override
	public Optional<CinemaHall> getCinemaHall(long id) {
		return cinemaHallRepository.findById(id);
	}

	@Override
	public void saveCinemaHallSeatsFromXML(long cinemaHallId, Map<Integer, List<Integer>> cinemaHallSeatCoordinateMap) {
		CinemaHall cinemaHall = cinemaHallRepository.getOne(cinemaHallId);
		rowRepository.deleteAll(cinemaHall.getRows());
		cinemaHall.setRows(mapSeatCoordinateMapToRowsSet(cinemaHall, cinemaHallSeatCoordinateMap));
		cinemaHallRepository.save(cinemaHall);
		logger.log(Level.INFO, cinemaHallRepository.getOne(cinemaHallId).getRows().toString());
	}

	private List<Row> mapSeatCoordinateMapToRowsSet(CinemaHall cinemaHall,
			Map<Integer, List<Integer>> cinemaHallSeatCoordinateMap) {
		List<Row> rows = new ArrayList<>();
		for (Map.Entry<Integer, List<Integer>> entry : cinemaHallSeatCoordinateMap.entrySet()) {
			Row row = new Row();
			row.setCinemaHall(cinemaHall);
			row.setNumber(entry.getKey() + 1);
			row.setRegistrationDate(new Date());
			row.setLastAccessDate(new Date());

			Set<Seat> seats = new HashSet<>();
			for (Integer seatNum : entry.getValue()) {
				Seat seat = new Seat(seatNum + 1, 1, 12.50);
				seat.setRow(row);
				seat.setLastAccessDate(new Date());
				seat.setRegistrationDate(new Date());
				seats.add(seat);
			}
			row.setSeats(seats);
			rows.add(row);
		}
		return rows;
	}

	@Override
	public void deleteMovie(long movieId) {
		movieRepository.deleteById(movieId);
	}

	@Override
	public void editMovie(Movie selectedMovie, String newOriginalMovieName, String newLocalizeMovieName,
			String newCountry, LocalDateTime newMovieReleaseDate) {
		movieRepository.editMovie(selectedMovie.getId(), newOriginalMovieName, newLocalizeMovieName, newCountry,
				Date.from(newMovieReleaseDate.atZone(ZoneId.systemDefault()).toInstant()));
	}

	@Override
	public void editMovie(Movie editedMovie) {
		Movie movie = movieRepository.getOne(editedMovie.getId());
		movie.setActors(editedMovie.getActors());
		movie.setGenres(editedMovie.getGenres());
		movie.setCountry(editedMovie.getCountry());
		movie.setDescription(editedMovie.getDescription());
		movie.setLocalizedName(editedMovie.getLocalizedName());
		movie.setMinAge(editedMovie.getMinAge());
		movie.setOriginalName(editedMovie.getOriginalName());
		movie.setPosterPath(editedMovie.getPosterPath());
		movie.setProducer(editedMovie.getProducer());
		movie.setReleaseDate(editedMovie.getReleaseDate());
		movie.setTimeMinutes(editedMovie.getTimeMinutes());
		movie.setTrailerURL(editedMovie.getTrailerURL());
		movieRepository.save(movie);
	}

	@Override
	public long createMovie(String localizedName, String originalName, LocalDateTime releaseDate) {
		Movie movie = new Movie();
		movie.setLocalizedName(localizedName);
		movie.setOriginalName(originalName);
		movie.setReleaseDate(Date.from(releaseDate.atZone(ZoneId.systemDefault()).toInstant()));
		movie.setPosterPath("");
		movie.setDescription("");
		movie.setTrailerURL("");
		movieRepository.save(movie);
		return movie.getId();
	}

	@Override
	public List<Movie> getMovies() {
		return movieRepository.findAll();
	}

	@Override
	public void createFilmScreening(Movie movie, Cinema cinema, java.sql.Date startDate, java.sql.Date endDate) {
		FilmScreening filmScreening = new FilmScreening();
		filmScreening.setAvailableToBuy(true);
		filmScreening.setRegistrationDate(new Date());
		filmScreening.setLastAccessDate(new Date());
		filmScreening.setCinema(cinema);
		filmScreening.setMovie(movie);
		filmScreening.setStartDate(startDate);
		filmScreening.setEndDate(endDate);
		filmScreening.setCinema(cinema);
		filmScreeningRepository.save(filmScreening);
	}

	@Override
	public void editFilmScreening(long id, Movie movie, Cinema cinema, Date startDate, Date endDate) {
		filmScreeningRepository.editFilmScreening(id, movie, cinema, new java.sql.Date(startDate.getTime()),
				new java.sql.Date(endDate.getTime()));
	}

	@Override
	public void deleteFilmScreening(long id) {
		filmScreeningRepository.deleteById(id);
	}

	@Override
	public void deleteFilmScreeningEvent(long id) {
		filmScreeningEventRepository.deleteById(id);
	}

	@Override
	public void createFilmScreeningEvent(FilmScreening filmScreening, CinemaHall cinemaHall, java.sql.Date date,
			Time time) {
		FilmScreeningEvent filmScreeningEvent = new FilmScreeningEvent();
		filmScreeningEvent.setRegistrationDate(new Date());
		filmScreeningEvent.setLastAccessDate(new Date());
		filmScreeningEvent.setFilmScreening(filmScreening);
		filmScreeningEvent.setTime(time);
		filmScreeningEvent.setCinemaHall(cinemaHall);
		filmScreeningEvent.setDate(date);
		filmScreeningEventRepository.save(filmScreeningEvent);
	}

	@Override
	public void editComment(long commentId, String newValue) {
		commentRepository.editComment(commentId, newValue);
	}

	@Override
	public void createFeature(String name, String description) {
		Feature feature = new Feature(name, description);
		feature.setLastAccessDate(new Date());
		feature.setRegistrationDate(new Date());
		featureRepository.save(feature);
	}

	@Override
	public void editCinemaFeatures(Cinema selectedCinema, Set<Feature> features) {
		Cinema cinema = cinemaRepository.getOne(selectedCinema.getId());
		cinema.setFeatures(features);
		cinemaRepository.save(cinema);
	}

	@Override
	public List<Feature> getFeatures() {
		return featureRepository.findAll();
	}

	@Override
	public void createGenre(String name) {
		Genre genre = new Genre(name);
		genre.setLastAccessDate(new Date());
		genre.setRegistrationDate(new Date());
		genreRepository.save(genre);
	}

	@Override
	public void createActor(String name, String surname) {
		Actor actor = new Actor(name, surname);
		actor.setLastAccessDate(new Date());
		actor.setRegistrationDate(new Date());
		actorRepository.save(actor);
	}

	@Override
	public void editFeature(long id, String name, String description) {
		Feature feature = featureRepository.getOne(id);
		feature.setName(name);
		feature.setFeatureDescription(description);
		featureRepository.save(feature);
	}

	@Override
	public void editGenre(long id, String name) {
		Genre genre = genreRepository.getOne(id);
		genre.setName(name);
		genreRepository.save(genre);
	}

	@Override
	public void editActor(long id, String name, String surname) {
		Actor actor = actorRepository.getOne(id);
		actor.setName(name);
		actor.setSurname(surname);
		actorRepository.save(actor);
	}
}
