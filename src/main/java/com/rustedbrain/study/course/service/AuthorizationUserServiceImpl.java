package com.rustedbrain.study.course.service;

import java.math.BigInteger;
import java.security.AccessControlException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rustedbrain.study.course.model.dto.AuthUser;
import com.rustedbrain.study.course.model.dto.UserInfo;
import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.authorization.Administrator;
import com.rustedbrain.study.course.model.persistence.authorization.Manager;
import com.rustedbrain.study.course.model.persistence.authorization.Member;
import com.rustedbrain.study.course.model.persistence.authorization.Moderator;
import com.rustedbrain.study.course.model.persistence.authorization.Paymaster;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.service.repository.AdministratorRepository;
import com.rustedbrain.study.course.service.repository.ManagerRepository;
import com.rustedbrain.study.course.service.repository.MemberRepository;
import com.rustedbrain.study.course.service.repository.ModeratorRepository;
import com.rustedbrain.study.course.service.repository.PaymasterRepository;
import com.rustedbrain.study.course.service.util.Validator;

@Service
public class AuthorizationUserServiceImpl implements AuthorizationUserService {

	private AdministratorRepository administratorRepository;
	private ManagerRepository managerRepository;
	private MemberRepository memberRepository;
	private ModeratorRepository moderatorRepository;
	private PaymasterRepository paymasterRepository;

	private SecureRandom random = new SecureRandom();
	private Map<String, AuthUser> rememberedUsers = new HashMap<>();

	public UserPropertiesAccessor<?> getUserPropertiesAccessor(UserRole role) {
		switch (role) {
		case MODERATOR:
			return new UserPropertiesAccessorImpl<>(moderatorRepository);
		case MANAGER:
			return new UserPropertiesAccessorImpl<>(managerRepository);
		case ADMINISTRATOR:
			return new UserPropertiesAccessorImpl<>(administratorRepository);
		case MEMBER:
			return new UserPropertiesAccessorImpl<>(memberRepository);
		case PAYMASTER:
			return new UserPropertiesAccessorImpl<>(paymasterRepository);
		case NOT_AUTHORIZED:
			throw new IllegalArgumentException("User not authorised");
		default:
			throw new IllegalArgumentException("Role not predefined");
		}
	}

	@Override
	public User getUser(String login) {
		User user = memberRepository.findByLogin(login);
		if ( user != null ) {
			return user;
		}
		user = administratorRepository.findByLogin(login);
		if ( user != null ) {
			return user;
		}
		user = paymasterRepository.findByLogin(login);
		if ( user != null ) {
			return user;
		}
		user = moderatorRepository.findByLogin(login);
		if ( user != null ) {
			return user;
		}
		user = managerRepository.findByLogin(login);
		if ( user != null ) {
			return user;
		}
		return null;
	}

	@Override
	public Optional<UserInfo> getUserInfo(long userId) {
		Optional<Member> member = memberRepository.findById(userId);
		if ( member.isPresent() ) {
			return Optional.of(new UserInfo(member.get(), UserRole.MEMBER));
		}
		Optional<Administrator> optionalAdministrator = administratorRepository.findById(userId);
		if ( optionalAdministrator.isPresent() ) {
			return Optional.of(new UserInfo(optionalAdministrator.get(), UserRole.ADMINISTRATOR));
		}
		Optional<Manager> optionalManager = managerRepository.findById(userId);
		if ( optionalManager.isPresent() ) {
			return Optional.of(new UserInfo(optionalManager.get(), UserRole.MANAGER));
		}
		Optional<Moderator> optionalModerator = moderatorRepository.findById(userId);
		if ( optionalModerator.isPresent() ) {
			return Optional.of(new UserInfo(optionalModerator.get(), UserRole.MODERATOR));
		}
		Optional<Paymaster> paymaster = paymasterRepository.findById(userId);
		if ( paymaster.isPresent() ) {
			return Optional.of(new UserInfo(paymaster.get(), UserRole.PAYMASTER));
		}
		return Optional.empty();
	}

	@Autowired
	public void setAdministratorRepository(AdministratorRepository administratorRepository) {
		this.administratorRepository = administratorRepository;
	}

	@Autowired
	public void setManagerRepository(ManagerRepository managerRepository) {
		this.managerRepository = managerRepository;
	}

	@Autowired
	public void setMemberRepository(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Autowired
	public void setModeratorRepository(ModeratorRepository moderatorRepository) {
		this.moderatorRepository = moderatorRepository;
	}

	@Autowired
	public void setPaymasterRepository(PaymasterRepository paymasterRepository) {
		this.paymasterRepository = paymasterRepository;
	}

	@Override
	public Optional<AuthUser> getIdentifiedAuthUser(String login, String password) {
		for (Validator validator : Validator.values()) {
			if ( validator.isValid(login) ) {
				switch (validator) {
				case MAIL_VALIDATOR: {
					return getAuthUserByMail(login, password);
				}
				case LOGIN_VALIDATOR: {
					return getAuthUserByLogin(login, password);
				}
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public String rememberUser(String login, UserRole role) {
		String randomId = new BigInteger(130, random).toString(32);
		rememberedUsers.put(randomId, new AuthUser(login, role));
		return randomId;
	}

	private Optional<AuthUser> getAuthUserByLogin(String login, String password) {
		Member member = memberRepository.findByLogin(login);
		if ( isNotNullPasswordIdentified(member, password) ) {
			return Optional.of(new AuthUser(member.getLogin(), UserRole.MEMBER));
		}
		Administrator optionalAdministrator = administratorRepository.findByLogin(login);
		if ( isNotNullPasswordIdentified(optionalAdministrator, password) ) {
			return Optional.of(new AuthUser(optionalAdministrator.getLogin(), UserRole.ADMINISTRATOR));
		}
		Manager optionalManager = managerRepository.findByLogin(login);
		if ( isNotNullPasswordIdentified(optionalManager, password) ) {
			return Optional.of(new AuthUser(optionalManager.getLogin(), UserRole.MANAGER));
		}
		Moderator optionalModerator = moderatorRepository.findByLogin(login);
		if ( isNotNullPasswordIdentified(optionalModerator, password) ) {
			return Optional.of(new AuthUser(optionalModerator.getLogin(), UserRole.MODERATOR));
		}
		Paymaster paymaster = paymasterRepository.findByLogin(login);
		if ( isNotNullPasswordIdentified(paymaster, password) ) {
			return Optional.of(new AuthUser(paymaster.getLogin(), UserRole.PAYMASTER));
		}
		return Optional.empty();
	}

	private boolean isNotNullPasswordIdentified(User user, String password) {
		boolean identified = user != null && user.getPassword().equals(password);
		if ( identified && user.getBlockPeriod() != null && user.getBlockPeriod().after(new Date()) ) {
			throw new AccessControlException("User is blocked and cannot use service until \"" + user.getBlockPeriod()
					+ "\", reason \"" + user.getBlockDescription() + "\"");
		} else {
			return identified;
		}
	}

	private Optional<AuthUser> getAuthUserByMail(String mail, String password) {
		Member member = memberRepository.findByEmail(mail);
		if ( isNotNullPasswordIdentified(member, password) ) {
			return Optional.of(new AuthUser(member.getLogin(), UserRole.MEMBER));
		}
		Administrator optionalAdministrator = administratorRepository.findByEmail(mail);
		if ( isNotNullPasswordIdentified(optionalAdministrator, password) ) {
			return Optional.of(new AuthUser(optionalAdministrator.getLogin(), UserRole.ADMINISTRATOR));
		}
		Manager optionalManager = managerRepository.findByEmail(mail);
		if ( isNotNullPasswordIdentified(optionalManager, password) ) {
			return Optional.of(new AuthUser(optionalManager.getLogin(), UserRole.MANAGER));
		}
		Moderator optionalModerator = moderatorRepository.findByEmail(mail);
		if ( isNotNullPasswordIdentified(optionalModerator, password) ) {
			return Optional.of(new AuthUser(optionalModerator.getLogin(), UserRole.MODERATOR));
		}
		Paymaster paymaster = paymasterRepository.findByEmail(mail);
		if ( isNotNullPasswordIdentified(paymaster, password) ) {
			return Optional.of(new AuthUser(paymaster.getLogin(), UserRole.PAYMASTER));
		}
		return Optional.empty();
	}

	@Override
	public AuthUser getRememberedUser(String id) {
		return rememberedUsers.get(id);
	}

	public void removeRememberedUser(String id) {
		rememberedUsers.remove(id);
	}

	@Override
	public boolean isValidCinemaManager(String login, long cinemaId) {
		return managerRepository.isCinemaManager(login, cinemaId);
	}

	@Override
	public Optional<Administrator> getAdministrator(String userLogin) {
		Optional<Administrator> optionalAdministrator =
				Optional.ofNullable(administratorRepository.findByLogin(userLogin));
		if ( !optionalAdministrator.isPresent() ) {
			optionalAdministrator = Optional.ofNullable(administratorRepository.findByEmail(userLogin));
		}
		return optionalAdministrator;
	}

	@Override
	public Optional<AuthUser> getAuthUserById(long id) {
		Optional<Member> member = memberRepository.findById(id);
		if ( member.isPresent() ) {
			return Optional.of(new AuthUser(member.get().getLogin(), UserRole.MEMBER));
		}
		Optional<Administrator> optionalAdministrator = administratorRepository.findById(id);
		if ( optionalAdministrator.isPresent() ) {
			return Optional.of(new AuthUser(optionalAdministrator.get().getLogin(), UserRole.ADMINISTRATOR));
		}
		Optional<Manager> optionalManager = managerRepository.findById(id);
		if ( optionalManager.isPresent() ) {
			return Optional.of(new AuthUser(optionalManager.get().getLogin(), UserRole.MANAGER));
		}
		Optional<Moderator> optionalModerator = moderatorRepository.findById(id);
		if ( optionalModerator.isPresent() ) {
			return Optional.of(new AuthUser(optionalModerator.get().getLogin(), UserRole.MODERATOR));
		}
		Optional<Paymaster> paymaster = paymasterRepository.findById(id);
		return paymaster.map(paymaster1 -> new AuthUser(paymaster1.getLogin(), UserRole.PAYMASTER));
	}

	@Override
	public List<Manager> getManagers() {
		return managerRepository.findAll();
	}
}
