package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;
import totalcross.util.Hashtable;

public class ClienteAtua extends BasePersonDomain {

    public static String TABLE_NAME = "TBLVPCLIENTEATUA";
    public Hashtable hashValuesDinamicosOriginal = null;

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdRegistro;
    public String flOrigemAtualizacao;
    public Date dtAtualizacao;
    public String nuCnpj;
    public String flTipoPessoa;
    public String flAtualizaCadastroWmw;
    public String cdUsuarioAlteracao;

    //Náo Persistente
    public Date dtAtualizacaoLimite;
    public Date dtAtualizacaoMaxima;
    public boolean houveAlteracaoCpfCnpj;

    public ClienteAtua() {
		super(TABLE_NAME);
		hashValuesDinamicosOriginal = new Hashtable(0);
	}

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ClienteAtua) {
            ClienteAtua clienteAtua = (ClienteAtua) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, clienteAtua.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, clienteAtua.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, clienteAtua.cdCliente) &&
                ValueUtil.valueEquals(cdRegistro, clienteAtua.cdRegistro) &&
            	ValueUtil.valueEquals(flOrigemAtualizacao, clienteAtua.flOrigemAtualizacao);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdRegistro);
        primaryKey.append(";");
        primaryKey.append(flOrigemAtualizacao);
        return primaryKey.toString();
    }

}