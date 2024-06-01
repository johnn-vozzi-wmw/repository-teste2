package br.com.wmw.lavenderepda.business.domain;

import totalcross.util.Date;

public class DapLaudoAtua extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPDAPLAUDOATUA";
	
	public String cdEmpresa;
	public String cdCliente;
	public String cdDapMatricula;
	public String cdSafra;
	public String cdDapCultura;
	public String cdDapLaudo;
	public String cdDapLaudoAtua;
	public int nuSeqLaudo;
	public String flStatusLaudo;
	public double qtArea;
	public Date dtEmissao;
	public String dsAspectoCultura;
	public String dsRecomendacoes;
	public String nmImgAssTecnico;
	public String nmImgAssCliente;
	public double cdLatitude;
	public double cdLongitude;
	public String dsObservacao;

	//não persistente
	public boolean isDeleteLaudosAtuaNaoEnviados;
	public boolean somenteLaudosNaoEnviados;

	public DapLaudoAtua() {
	}

	public DapLaudoAtua(DapLaudo dapLaudo) {
		this.cdEmpresa = dapLaudo.cdEmpresa;
		this.cdCliente = dapLaudo.cdCliente;
		this.cdDapMatricula = dapLaudo.cdDapMatricula;
		this.cdSafra = dapLaudo.cdSafra;
		this.cdDapCultura = dapLaudo.cdDapCultura;
		this.cdDapLaudo = dapLaudo.cdDapLaudo;
		this.nuSeqLaudo = dapLaudo.nuSeqLaudo;
		this.flStatusLaudo = dapLaudo.flStatusLaudo;
		this.qtArea = dapLaudo.qtArea;
		this.dtEmissao = dapLaudo.dtEmissao;
		this.dsAspectoCultura = dapLaudo.dsAspectoCultura;
		this.dsRecomendacoes = dapLaudo.dsRecomendacoes;
		this.nmImgAssTecnico = dapLaudo.getNmImgAssinaturaTec();
		this.nmImgAssCliente = dapLaudo.getNmImgAssinaturaCli();
		this.cdLatitude = dapLaudo.cdLatitude;
		this.cdLongitude = dapLaudo.cdLongitude;
		this.dsObservacao = dapLaudo.dsObservacao;
	}
	
	@Override
	public String getCdDomain() {
		return cdDapLaudo;
	}
	
	@Override
	public String getDsDomain() {
		return dsRecomendacoes;
	}
	
	@Override
	public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdSafra);
        primaryKey.append(";");
        primaryKey.append(cdDapMatricula);
        primaryKey.append(";");
        primaryKey.append(cdDapCultura);
        primaryKey.append(";");
        primaryKey.append(cdDapLaudo);
        primaryKey.append(";");
        primaryKey.append(cdDapLaudoAtua);
        return primaryKey.toString();
	}

}
