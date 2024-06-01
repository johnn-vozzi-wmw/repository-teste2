package br.com.wmw.lavenderepda.business.domain.dto;

public class RetValidaSenhaDTO {
	
	private boolean loginValido;
	private boolean bloqueadoLogin;
	private String nmUsuario;


	public boolean isLoginValido() {
		return loginValido;
	}

	public void setLoginValido(boolean loginValido) {
		this.loginValido = loginValido;
	}

	public String getNmUsuario() {
		return nmUsuario;
	}

	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}

	public boolean isBloqueadoLogin() {
		return bloqueadoLogin;
	}

	public void setBloqueadoLogin(boolean bloqueadoLogin) {
		this.bloqueadoLogin = bloqueadoLogin;
	}
}
