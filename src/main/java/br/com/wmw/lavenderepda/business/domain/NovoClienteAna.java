package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class NovoClienteAna extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPNOVOCLIENTEANA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemNovoCliente;
    public String cdNovoCliente;
    public String cdTipoAnalise;
    public String cdStatusAnalise;
    public String cdMotivoReprovacao;
    public double vlLimiteAprovado;
    public String cdCondicaoComercial;
    public String dsObservacao;
    public String cdUsuarioEdicao;
    public String nmUsuarioEdicao;
    public Date dtEdicaoUsuario;
    public String hrEdicaoUsuario;
    public Date dtTransmissaoPda;
    public String hrTransmissaoPda;
    public Date dtCadastro;
    public String hrCadastro;
    public String flCnpjRecorrente;
    public String flNaoPossuiCobertura;
    public Date dtAlteracao;
    public String hrAlteracao;
    public String nuCnpj;
    public String nmRazaoSocial;
    public String flTipoPessoa;
    public String flPermiteMultiplosEnderecos;
    //
    public boolean ckNaoFinalizados;
    public String dsFiltro;
    public Date dtEdicaoUsuarioInicial;
    public Date dtEdicaoUsuarioFinal;
	public boolean ckApenasPendencias;

    //Override
	public boolean equals(Object obj) {
		if (obj instanceof NovoClienteAna) {
			NovoClienteAna novoClienteAna = (NovoClienteAna) obj;
			return ValueUtil.valueEquals(cdEmpresa, novoClienteAna.cdEmpresa)
					&& ValueUtil.valueEquals(cdRepresentante, novoClienteAna.cdRepresentante)
					&& ValueUtil.valueEquals(flOrigemNovoCliente, novoClienteAna.flOrigemNovoCliente)
					&& ValueUtil.valueEquals(cdNovoCliente, novoClienteAna.cdNovoCliente);
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
        primaryKey.append(flOrigemNovoCliente);
        primaryKey.append(";");
        primaryKey.append(cdNovoCliente);
        return primaryKey.toString();
    }

    
    public boolean isCnpjRecorrente() {
    	return ValueUtil.getBooleanValue(flCnpjRecorrente);
    }

    public boolean isNaoPossuiCobertura() {
    	return ValueUtil.getBooleanValue(flNaoPossuiCobertura);
    }
    
}