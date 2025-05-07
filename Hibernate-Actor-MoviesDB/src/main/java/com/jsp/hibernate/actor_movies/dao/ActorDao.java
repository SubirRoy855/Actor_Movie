package com.jsp.hibernate.actor_movies.dao;

import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import com.jsp.hibernate.actor_movies.entity.Actor;

public class ActorDao {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
	Scanner sc = new Scanner(System.in);
//In-build-methods
	public void addActor() {
		Actor actor = new Actor();
		System.out.println("Enter the Actor Name:");
		actor.setActorName(sc.next());
		System.out.println("Enter the Actor Age:");
		actor.setAge(sc.nextInt());
		System.out.println("Enter the Industry Name:");
		actor.setIndustry(sc.next());
		System.out.println("Enter the Nationality: ");
		actor.setNationality(sc.next());
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();

		em.persist(actor);

		et.commit();
//		em.close();

	}
	//In-build-methods
	public void findActorById() {
		EntityManager em = emf.createEntityManager();

		System.out.println("Enter the Id: ");
		Actor ac = em.find(Actor.class, sc.nextInt());
		if (ac != null) {
			System.out.print(ac.getActorId());
			System.out.print(ac.getActorName());
			System.out.print(ac.getAge());
			System.out.print(ac.getIndustry());
			System.out.print(ac.getNationality());
		} else
			System.out.println("Actor not found");

	}
//HQL
	public void findActorByName() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();

		Query query = em.createQuery("FROM Actor WHERE actorName=?1");
		System.out.println("Enter the actor Name:");
		query.setParameter(1, sc.next());

		List<Actor> list = query.getResultList();
		for (Actor a : list) {
			System.out.println(a);
		}
		et.commit();
		em.close();
	}
//SQL
	public void findActorByIndustry() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		Query query = em.createNamedQuery("select * from actor where industry=?1");
		System.out.println("Enter the Industry: ");
		query.setParameter(1, sc.next());

		List<Actor> list = query.getResultList();

		for (Actor ac : list) {
			System.out.println(ac);
		}
	}
//	CriteriaBuilder
	public void findActorBetweenAge() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Actor> query = cb.createQuery(Actor.class);
		Root<Actor> root = query.from(Actor.class);

		query.select(root);
		System.out.println("Enter the 1st Age: ");
		int a = sc.nextInt();

		System.out.println("Enter the 2nd Age: ");
		int b = sc.nextInt();
		query.where(cb.between(root.get("age"), a, b));

		Query qu = em.createQuery(query);

		List<Actor> actors = qu.getResultList();

		for (Actor actor : actors)
			System.out.println(actor);

		et.commit();
		em.close();

	}
	//HQL
	public void findActorsWithAgeGreaterThan() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();

		Query query = em.createQuery("FROM Actor WHERE age>?1");
		System.out.println("Enter the actor Age:");
		query.setParameter(1, sc.nextInt());

		List<Actor> list = query.getResultList();
		for (Actor a : list) {
			System.out.println(a);
		}
		et.commit();
		em.close();
		
	}
//	CriteriaBuilder
	public void updateActorAgeById() {
		EntityManager em=emf.createEntityManager();
		EntityTransaction et=em.getTransaction();
		et.begin();
		//Step:1
		CriteriaBuilder cb=em.getCriteriaBuilder();
		//Steo:2
		CriteriaUpdate<Actor> cu=cb.createCriteriaUpdate(Actor.class);
		
		Root<Actor> root=cu.from(Actor.class);
		System.out.println("Enter the Set Age: ");
		cu.set(root.get("age"), sc.nextInt());
		System.out.println("Where id: ");
		cu.where(cb.equal(root.get("actorId"), sc.nextInt()));
		//step:3
		Query query=em.createQuery(cu);
		
		int result=query.executeUpdate();
		System.out.println(result+" data update");
		et.commit();
		
		
	}
	//HQL
	public void updateActorNationalityByIndustry() {
		EntityManager em=emf.createEntityManager();
		EntityTransaction et=em.getTransaction();
		et.begin();
		Query query=em.createQuery("update Actor set nationality=?1 where industry=?2");
		System.out.println("Enter the nationality: ");
		query.setParameter(1, sc.next());
		System.out.println("Enter the industry: ");
		query.setParameter(2, sc.next());
		
		int rowUpadte=query.executeUpdate();
		System.out.println(rowUpadte+" data is update");
		
		et.commit();
		
	}
	public void deleteActorById() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<Actor> cd = cb.createCriteriaDelete(Actor.class);
		
		Root<Actor> root = cd.from(Actor.class);
		
		System.out.println("Enter the id : ");
		cd.where(cb.equal(root.get("actorId"), sc.nextInt()));
		
		
		Query query = em.createQuery(cd);
		int rowDeleted = query.executeUpdate();
		System.out.println(rowDeleted + " data deleted sucessfully.");
		et.commit();
		em.close();
	}
	
	
}
