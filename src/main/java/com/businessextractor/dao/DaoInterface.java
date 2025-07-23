package com.businessextractor.dao;

import java.util.List;

/**
 * Top level Data Access Object interface specifying common entity operation
 * methods.
 * @author Bogdan Vlad
 *
 * @param <Entity> The entity parameter that will be operated on
 */
public interface DaoInterface<Entity> {

	/**
	 * Used to store an entity to persistent storage.
	 * @param entity The entity to be saved.
	 */
	public void save (Entity entity);
	
	/**
	 * Used to update an entity to persistent storage
	 * @param entity The entity to be persisted
	 */
	public void update (Entity entity);
	
	/**
	 * Used to remove an entity from persistent storage
	 * @param entity The entity to be removed.
	 */
	public void delete (Entity entity);
	
	/**
	 * Loads an entity by its primary key from persistent storage
	 * @param id The primary key
	 * @return The entity loaded
	 */
	public Entity loadById (Long id);
	
}
