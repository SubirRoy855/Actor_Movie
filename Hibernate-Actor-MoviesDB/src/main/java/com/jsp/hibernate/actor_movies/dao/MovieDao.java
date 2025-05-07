package com.jsp.hibernate.actor_movies.dao;


	import java.util.ArrayList;
	import java.util.List;
	import java.util.Scanner;
	import javax.persistence.EntityManager;
	import javax.persistence.EntityManagerFactory;
	import javax.persistence.EntityTransaction;
	import javax.persistence.Persistence;
	import javax.persistence.Query;
	import javax.persistence.criteria.CriteriaBuilder;
	import javax.persistence.criteria.CriteriaQuery;
	import javax.persistence.criteria.CriteriaUpdate;
	import javax.persistence.criteria.Root;

import com.jsp.hibernate.actor_movies.entity.Actor;
import com.jsp.hibernate.actor_movies.entity.Movie;


	public class MovieDao {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
		Scanner scan = new Scanner(System.in);

		public void addMovie() {
			EntityManager em = emf.createEntityManager();
			EntityTransaction et = em.getTransaction();
			et.begin();
			Movie movie = new Movie();
			System.out.println("Enter the Title : ");
			movie.setTitle(scan.next());
			System.out.println("Enter the Genre ");
			movie.setGenre(scan.next());
			System.out.println("Enter the Director : ");
			movie.setDirector(scan.next());
			System.out.println("Enter the Collection : ");
			movie.setCollection(scan.nextLong());

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Actor> cq = cb.createQuery(Actor.class);
			Root<Actor> root = cq.from(Actor.class);
			Query query = em.createQuery(cq);
			List<Actor> actors = query.getResultList();
			for (Actor actor : actors) {
				printActor(actor);
			}
			System.out.println("Enter the ActorId of this movie : ");
			String ids = scan.next();
			String[] stringId = ids.split(",");
			List<Actor> addActors = new ArrayList<Actor>();
			for (String id : stringId) {
				int actorId = Integer.parseInt(id);
				Actor actor = em.find(Actor.class, actorId);
				addActors.add(actor);
			}
			movie.setActors(addActors);;
			em.persist(movie);
			et.commit();
			em.close();
		}

		public static void printActor(Actor a) {
			System.out.println("\n[ Actor Details \n ------------------------------ \n ActorId : " + a.getActorId()
					+ "\n ActorName : " + a.getActorName() + "\n age :" + a.getAge() + "\n Industry : " + a.getIndustry()
					+ "\n Nationatility : " + a.getNationality() + " ]");
		}

		public static void printMovieWithActors(Movie m) {
			System.out.println("\n[ Movie Details \n ------------------------------ \n movieId : " + m.getMovieId()
					+ "\n movieName : " + m.getTitle() + "\n Genre :" + m.getGenre() + "\n Director : " + m.getDirector()
					+ "\n Collection : " + m.getCollection() + " ]"
					+ " \n ----------------------------------- \n actors of this movie are : \n ---------------------------------\n");
			List<Actor> actors = m.getActors();
			for (Actor actor : actors) {
				printActor(actor);
			}
		}

		public static void printMovie(Movie m) {
			System.out.println("\n[ Movie Details \n ------------------------------ \n movieId : " + m.getMovieId()
					+ "\n movieName : " + m.getTitle() + "\n Genre :" + m.getGenre() + "\n Director : " + m.getDirector()
					+ "\n Collection : " + m.getCollection() + " ]");

		}

		public void findMovieById() {
			EntityManager em = emf.createEntityManager();
			System.out.println("Enter the Id of the Movie : ");
			Movie movie = em.find(Movie.class, scan.nextInt());
			printMovieWithActors(movie);
			em.close();

		}

		public void findMovieByTitle() {
			EntityManager em = emf.createEntityManager();
			Query query = em.createQuery("from Movie where title=?1");
			System.out.println("Enter the Title : ");
			query.setParameter(1, scan.next());
			List<Movie> movies = query.getResultList();
			for (Movie movie : movies) {
				printMovieWithActors(movie);
			}
			em.close();
		}

		public void findMovieByDirector() {
			EntityManager em = emf.createEntityManager();
			Query query = em.createNativeQuery("Select * from movie where director=?1", Movie.class);
			System.out.println("Enter the Director Name : ");
			query.setParameter(1, scan.next());
			List<Movie> movies = query.getResultList();
			for (Movie movie : movies) {
				printMovieWithActors(movie);
			}
			em.close();

		}

		public void findMovieByGenre() {
			EntityManager em = emf.createEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
			Root<Movie> root = cq.from(Movie.class);
			System.out.println("Enter the genre");
			cq.where(cb.equal(root.get("genre"), scan.next()));
			Query query = em.createQuery(cq);
			List<Movie> movies = query.getResultList();
			for (Movie movie : movies) {
				printMovieWithActors(movie);
			}
			em.close();
		}

		public void findMovieWithCollectionGreaterThan() {
			EntityManager em = emf.createEntityManager();
			Query query = em.createQuery("from Movie where Collection>?1");
			System.out.println("Enter the collection : ");
			query.setParameter(1, scan.nextLong());
			List<Movie> movies = query.getResultList();
			for (Movie movie : movies) {
				printMovieWithActors(movie);
			}
			em.close();
		}

		public void findMoviesByActorId() {
			EntityManager em = emf.createEntityManager();
			Query query = em.createNativeQuery(
					"SELECT m.* FROM movie m inner join movie_actor ma on m.movieId = ma.movie_movieId JOIN actor a ON ma.actor_actorId=a.actorId WHERE a.actorId = ?1",
					Movie.class);
			System.out.println("Enter the actor Id");
			query.setParameter(1, scan.nextInt());
			List<Movie> movies = query.getResultList();
			for (Movie movie : movies) {
				printMovie(movie);
			}
			em.close();
		}

		public void updateMovieCollectionByTitle() {
			EntityManager em = emf.createEntityManager();
			EntityTransaction et = em.getTransaction();
			et.begin();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaUpdate<Movie> cu = cb.createCriteriaUpdate(Movie.class);
			Root<Movie> root = cu.from(Movie.class);
			System.out.println("Enter the collection");
			cu.set(root.get("Collection"), scan.nextLong());
			System.out.println("Enter the Title : ");
			cu.where(cb.equal(root.get("title"), scan.next()));
			Query query = em.createQuery(cu);
			int rowUpdated = query.executeUpdate();
			System.out.println(rowUpdated + "Data updated Sucessfully.");
			et.commit();
			em.close();
		}

		public void deleteMovieById() {
			EntityManager em = emf.createEntityManager();
			EntityTransaction et = em.getTransaction();
			et.begin();
			Query query = em.createQuery("delete from Movie where movieId=?1");
			System.out.println("Enter the MovieId :");
			query.setParameter(1, scan.nextInt());
			int rowDeleted = query.executeUpdate();
			System.out.println(rowDeleted + " Data deleted sucessfully.");
			et.commit();
			em.close();

		}

	}



