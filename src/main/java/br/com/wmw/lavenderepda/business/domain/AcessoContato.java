package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class AcessoContato extends BaseDomain {

	public static String TABLE_NAME = "TBLVPACESSOCONTATO";
	
	public String cdAcessoContato;
    public String dsEmpresa;
    public String dsNome;
    public String dsEmail;
    public Date dtCadastro;
    public String hrCadastro;
    public String nuFuncionarios;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AcessoContato) {
            AcessoContato acessoContato = (AcessoContato) obj;
            return
                ValueUtil.valueEquals(cdAcessoContato, acessoContato.cdAcessoContato);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdAcessoContato);
        return primaryKey.toString();
    }

}