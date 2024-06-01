package br.com.wmw.lavenderepda.business.domain;


import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import totalcross.util.Date;

public class NfDevolucao extends BasePersonDomain {
	
	public static final int CONFIRMAR_CANCELAR = 0;
	public static final int REPROVAR = 1;
	public static final int APROVAR = 2;
	public static final String NMCOLUNA_DSOBSERVACAO = "DSOBSERVACAO";
	public static String TABLE_NAME = "TBLVPNFDEVOLUCAO";
	public static String NFDEVOLUCAO_PENDENTE = "P";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdSerie;
	public String nuNfDevolucao;
	public String nuPedido;
	public String flOrigemPedido;
	public String cdCliente;
	public Double vlNfDevolucao;
	public Date dtEmissao;
	public String cdTransportadora;
	public String dsObservacao;
	public String flAprovacao;
    public Date dtAlteracao;
    public String hrAlteracao;

	public NfDevolucao() {
		super(TABLE_NAME);
	}

	public NfDevolucao(String cdEmpresa, String cdRepresentante, String nuPedido) {
		super(TABLE_NAME);
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.nuPedido = nuPedido;
	}

	@Override
	public String getPrimaryKey() {
		StringBuilder strBuilder = new StringBuilder();
    	strBuilder.append(cdEmpresa);
    	strBuilder.append(";");
    	strBuilder.append(nuNfDevolucao);
    	strBuilder.append(";");
    	strBuilder.append(cdSerie);
    	strBuilder.append(";");
    	strBuilder.append(cdRepresentante);
        return strBuilder.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NfDevolucao) {
			NfDevolucao nfDevolucao = (NfDevolucao) obj;
			return
			ValueUtil.valueEquals(cdEmpresa, nfDevolucao.cdEmpresa)
				&& ValueUtil.valueEquals(nuNfDevolucao, nfDevolucao.cdRepresentante)
				&& ValueUtil.valueEquals(nuNfDevolucao, nfDevolucao.nuNfDevolucao)
				&& ValueUtil.valueEquals(cdSerie, nfDevolucao.cdSerie);
	    }
	    return false;
	}
	
	public static NfDevolucao getNfDevolucao() {
		NfDevolucao nfDevolucao = new NfDevolucao();
		nfDevolucao.cdEmpresa = SessionLavenderePda.getCliente().cdEmpresa;
		nfDevolucao.cdRepresentante = SessionLavenderePda.getCliente().cdRepresentante;
		nfDevolucao.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		nfDevolucao.flAprovacao = NfDevolucao.NFDEVOLUCAO_PENDENTE;
		return nfDevolucao;
	}
}
