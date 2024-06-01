package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ColecaoStatus extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPCOLECAOSTATUS";

    public String cdEmpresa;
    public String cdStatusColecao;
    public String dsStatusColecao;
    
    public ColecaoStatus() {
    	super();
    }
    
    public ColecaoStatus(Produto produto) {
    	super();
    	this.cdEmpresa = produto.cdEmpresa;
    	this.cdStatusColecao = produto.cdStatusColecao;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ColecaoStatus) {
            ColecaoStatus colecaoStatus = (ColecaoStatus) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, colecaoStatus.cdEmpresa) && 
                ValueUtil.valueEquals(cdStatusColecao, colecaoStatus.cdStatusColecao);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdStatusColecao);
        return primaryKey.toString();
    }
    
    @Override
    public String toString() {
    	StringBuffer descricao = new StringBuffer();
    	descricao.append("[").append(cdStatusColecao).append("] ");
    	descricao.append(dsStatusColecao);
    	return descricao.toString();
    }
    
    
}