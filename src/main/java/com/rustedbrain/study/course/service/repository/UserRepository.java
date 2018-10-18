package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.authorization.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@NoRepositoryBean
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

	@Query("select u from #{#entityName} as u where u.email = ?1 ")
	T findByEmail(String email);

	@Query("select u from #{#entityName} as u where u.login = ?1 ")
	T findByLogin(String login);

	@Transactional
	@Modifying
	@Query("update #{#entityName} entity set entity.name = ?2 where entity.id = ?1 ")
	void changeUserName(long userId, String newName);

	@Transactional
	@Modifying
	@Query("update #{#entityName} entity set entity.login = ?2 where entity.id = ?1 ")
	void changeUserLogin(long userId, String newLogin);

	@Transactional
	@Modifying
	@Query("update #{#entityName} entity set entity.password = ?2 where entity.id = ?1 ")
	void changeUserPassword(long userId, String newPassword);

	@Transactional
	@Modifying
	@Query("update #{#entityName} entity set entity.email = ?2 where entity.id = ?1 ")
	void changeUserMail(long userId, String newEmail);

	@Transactional
	@Modifying
	@Query("update #{#entityName} entity set entity.surname = ?2 where entity.id = ?1 ")
	void changeUserSurname(long userId, String newSurname);

	@Transactional
	@Modifying
	@Query("update #{#entityName} entity set entity.birthday = ?2 where entity.id = ?1 ")
	void changeUserBirthday(long userId, Date birthday);

	@Transactional
	@Modifying
	@Query("update #{#entityName} entity set entity.city = (select c from City c where c.id = ?2) where entity.id = ?1 ")
	void changeUserCity(long userId, long cityId);

	@Transactional
	@Modifying
	@Query("update #{#entityName} entity set entity.blockPeriod = ?2, entity.blockDescription = ?3 where entity.id = ?1 ")
	void changeUserBlockDate(long id, Date date, String description);
}
