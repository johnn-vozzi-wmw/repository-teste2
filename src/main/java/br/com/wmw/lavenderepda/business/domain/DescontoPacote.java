package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class DescontoPacote extends LavendereBasePersonDomain {
	
	public static String SIGLE_EXCEPTION = "DPG";

	public static final String TABLE_NAME = "TBLVPDESCONTOPACOTE";
	public static final String TABLE_NAME_WEB = "TBLVWDESCONTOPACOTE";
	
	public static final String CD_TABELA_PRECO_0 = "0";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdProduto;
	public String cdPacote;
	public String cdTabelaPreco;
	public double qtItem;
	public double vlPctDesconto;
	
    //Não persistente
	public Pacote pacote;
    public double vlDesconto;

	public DescontoPacote() {
		this(TABLE_NAME);
	}

	public DescontoPacote(String tableName) {
		super(tableName);
	}
	
	public DescontoPacote(final String cdEmpresa, final String cdRepresentante, final String cdProduto, final String cdTabelaPreco) {
		super(TABLE_NAME);
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdProduto = cdProduto;
		this.cdTabelaPreco = cdTabelaPreco;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DescontoPacote)) return false;
		DescontoPacote descontoPacote = (DescontoPacote) obj;
		return ValueUtil.valueEquals(cdEmpresa, descontoPacote.cdEmpresa) &&
			   ValueUtil.valueEquals(cdRepresentante, descontoPacote.cdRepresentante) &&
			   ValueUtil.valueEquals(cdProduto, descontoPacote.cdProduto) &&
			   ValueUtil.valueEquals(cdPacote, descontoPacote.cdPacote) &&
			   ValueUtil.valueEquals(cdTabelaPreco, descontoPacote.cdTabelaPreco) &&
			   ValueUtil.valueEquals(qtItem, descontoPacote.qtItem);
	}

	public String getCdDomain() {
		return cdPacote;
	}

	public String getDsDomain() {
		return cdProduto;
	}

    public String getPrimaryKey() {
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdPacote);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
    	strBuffer.append(";");
    	strBuffer.append(qtItem);
        return strBuffer.toString();
    }

	
}