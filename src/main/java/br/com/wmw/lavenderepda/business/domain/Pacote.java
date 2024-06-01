package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Pacote extends LavendereBasePersonDomain {

	public static final String TABLE_NAME = "TBLVPPACOTE";
	public static final String TABLE_NAME_WEB = "TBLVWPACOTE";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPacote;
	public String dsPacote;
	
	//-- Não persistente
	public String cdProdutoFilter;
	public String cdTabelaPrecoFilter;
	
	public Pacote() {
		this(TABLE_NAME);
	}

	public Pacote(String tableName) {
		super(tableName);
	}
	
	public Pacote(final String cdEmpresa, final String cdRepresentante) {
		super(TABLE_NAME);
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Pacote)) return false;
		Pacote pacote = (Pacote) obj;
		return ValueUtil.valueEquals(cdEmpresa, pacote.cdEmpresa) &&
			   ValueUtil.valueEquals(cdRepresentante, pacote.cdRepresentante) &&
			   ValueUtil.valueEquals(cdPacote, pacote.cdPacote);
	}

	public String getCdDomain() {
		return cdPacote;
	}

	public String getDsDomain() {
		return dsPacote;
	}

    public String getPrimaryKey() {
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdPacote);
        return strBuffer.toString();
    }

	
}