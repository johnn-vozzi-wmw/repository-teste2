package br.com.wmw.lavenderepda.business.domain;


import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class NotaFiscal extends BasePersonDomain {
	
	public static String TABLE_NAME = "TBLVPNOTAFISCAL";

	public String cdEmpresa;
	public String nuNotaFiscal;
	public String cdSerie;
	public String nuPedido;
	public String flOrigemPedido;
	public String cdRepresentante;
	public String nmRepresentante;
	public String cdCliente;
	public String nmRazaoSocial;
	public Double vlNotaFiscal;
	public Date dtEmissao;
	public Date dtPrevisaoEntrega;
	public Date dtSaida;
	public String hrSaida;
	public String cdTransportadora;
	public String nmTransportadora;
	public String dsObservacao;

	public NotaFiscal() {
		super(TABLE_NAME);
	}

	public NotaFiscal(String cdEmpresa, String cdRepresentante, String nuPedido) {
		super(TABLE_NAME);
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.nuPedido = nuPedido;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(nuNotaFiscal);
    	strBuffer.append(";");
    	strBuffer.append(cdSerie);
        return strBuffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NotaFiscal) {
			NotaFiscal notaFiscal = (NotaFiscal) obj;
			return 
			ValueUtil.valueEquals(cdEmpresa, notaFiscal.cdEmpresa) &&
			ValueUtil.valueEquals(nuNotaFiscal, notaFiscal.nuNotaFiscal) &&
			ValueUtil.valueEquals(cdSerie, notaFiscal.cdSerie);
	    }
	    return false;
	}
}
