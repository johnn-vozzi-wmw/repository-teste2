package br.com.wmw.lavenderepda.business.domain;


public class StatusRequisicaoServ extends LavendereBaseDomain {
	
	public String dsStatus;
	public String flStatus;
	
    public static final String FLSTATUSPENDENTE = "P";
    public static final String FLSTATUSANDAMENTO = "A";
    public static final String FLSTATUSENCERRADO = "E";
    public static final String FLSTATUSCANCELADO = "C";
	
	public StatusRequisicaoServ(String flStatus, String dsStatus) {
		this.dsStatus = dsStatus;
		this.flStatus = flStatus;
	}
	
	@Override
	public String getCdDomain() {
		return flStatus;
	}

	@Override
	public String getDsDomain() {
		return dsStatus;
	}

	@Override
	public String getPrimaryKey() {
		return flStatus;
	}

}
