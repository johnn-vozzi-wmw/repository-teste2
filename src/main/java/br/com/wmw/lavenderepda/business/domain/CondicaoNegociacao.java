package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class CondicaoNegociacao extends LavendereBasePersonDomain {
	
	public CondicaoNegociacao(String dsTabela) {
		super(dsTabela);
	}
	
	public CondicaoNegociacao() {
		super(TABLE_NAME);
	}

	public static String TABLE_NAME = "TBLVPCONDICAONEGOCIACAO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoNegociacao;
    public String dsCondicaoNegociacao;
    public String cdLocalEstoque;
    public double vlPctEstoque;
    public Date dtAlteracao;
    public String hrAlteracao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondicaoNegociacao) {
            CondicaoNegociacao condicaonegociacao = (CondicaoNegociacao) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condicaonegociacao.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, condicaonegociacao.cdRepresentante) && 
                ValueUtil.valueEquals(cdCondicaoNegociacao, condicaonegociacao.cdCondicaoNegociacao);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdCondicaoNegociacao);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdCondicaoNegociacao;
	}

	@Override
	public String getDsDomain() {
		return dsCondicaoNegociacao;
	}

}