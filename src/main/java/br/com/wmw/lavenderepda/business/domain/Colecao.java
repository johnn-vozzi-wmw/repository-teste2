package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class Colecao extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPCOLECAO";

    public String cdEmpresa;
    public String cdMarca;
    public String cdColecao;
    public String dsColecao;
    
    
    
    public Colecao() {
    	super();
    }
    
    public Colecao(Produto produto) {
		super();
		this.cdEmpresa = produto.cdEmpresa;
    	this.cdMarca = produto.cdMarca;
    	this.cdColecao = produto.cdColecao;
	}


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Colecao) {
            Colecao colecao = (Colecao) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, colecao.cdEmpresa) && 
                ValueUtil.valueEquals(cdMarca, colecao.cdMarca) && 
                ValueUtil.valueEquals(cdColecao, colecao.cdColecao);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdMarca);
        primaryKey.append(";");
        primaryKey.append(cdColecao);
        return primaryKey.toString();
    }

}