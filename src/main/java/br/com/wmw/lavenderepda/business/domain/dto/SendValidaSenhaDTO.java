package br.com.wmw.lavenderepda.business.domain.dto;

public class SendValidaSenhaDTO {
	
	private String cdUsuario;
	private String dsSenha;
	private boolean sendEmailBloqueadoLogin;

	public String getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(String cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getDsSenha() {
		return dsSenha;
	}

	public void setDsSenha(String dsSenha) {
		this.dsSenha = dsSenha;
	}

	public boolean isSendEmailBloqueadoLogin() {
		return sendEmailBloqueadoLogin;
	}

	public void setSendEmailBloqueadoLogin(boolean sendEmailBloqueadoLogin) {
		this.sendEmailBloqueadoLogin = sendEmailBloqueadoLogin;
	}
}
