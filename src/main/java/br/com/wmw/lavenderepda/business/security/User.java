package br.com.wmw.lavenderepda.business.security;

import br.com.wmw.framework.business.domain.Usuario;
import br.com.wmw.framework.util.JsonFactory;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import totalcross.json.JSONObject;
import totalcross.sys.Time;

import java.sql.SQLException;

public class User {
	private long lastTry;
	private int nuTentativas;
	private String dsLogin;

	public static User get(String dsLogin) throws SQLException {
		User user;
		try {
			String jsonUser = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.CONTROLEFALHALOGIN, dsLogin);
			user = JsonFactory.parse(jsonUser, User.class);
			if (user == null) {
				user = createUser(dsLogin);
			}
		} catch (Exception e) {
			user = createUser(dsLogin);
		}
		return user;
	}

	public static User get(Usuario usuario) throws SQLException {
		return get(usuario.dsLogin);
	}

	private static User createUser(String dsLogin) throws SQLException {
		User user = new User();
		user.dsLogin = dsLogin;
		user.nuTentativas = 1;
		user.save();
		return user;
	}

	public void save() throws SQLException {
		String jsonUser = new JSONObject(this).toString();
		ConfigInternoService.getInstance().addValue(ConfigInterno.CONTROLEFALHALOGIN, dsLogin, jsonUser);
	}

	public long getLastTry() {
		return lastTry;
	}

	public void setLastTry(long lastTry) {
		this.lastTry = lastTry;
	}

	public int getNuTentativas() {
		return nuTentativas;
	}

	public void setNuTentativas(int nuTentativas) {
		this.nuTentativas = nuTentativas;
	}

	public String getDsLogin() {
		return dsLogin;
	}

	public void setDsLogin(String dsLogin) {
		this.dsLogin = dsLogin;
	}

	public void addNuTentativas() {
		nuTentativas++;
	}

	public void reset() throws SQLException {
		nuTentativas = 1;
		lastTry = 0;
		save();
	}

	public void setLastTryTime(Time time) {
		this.lastTry = time.getTimeLong();
	}
	
}
