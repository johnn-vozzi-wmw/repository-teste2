package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class CargaPedido extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPCARGAPEDIDO";

	public String cdEmpresa;
    public String cdRepresentante;
    public String cdCargaPedido;
    public String cdRotaEntrega;
    public String dsCargaPedido;
    public Date dtCriacao;
    public String hrCriacao;
    public Date dtFechamento;
    public String hrFechamento;
    public String flPesoMenorLiberado;
    public String flCargaVencidaLiberada;
    public String flCargaFechada;
    public Date dtEntrega;
    //Nao persistentes
    public Date dtLimiteExclusao;
	public boolean forceFlTipoAlteracao;
    
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CargaPedido) {
            CargaPedido cargaPedido = (CargaPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, cargaPedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, cargaPedido.cdRepresentante) && 
                ValueUtil.valueEquals(cdCargaPedido, cargaPedido.cdCargaPedido);
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
        primaryKey.append(cdCargaPedido);
        return primaryKey.toString();
    }


    public String getCdDomain() {
		return StringUtil.getStringValue(cdCargaPedido);
	}

	public String getDsDomain() {
		return dsCargaPedido;
	}

	public boolean isCargaFechada() {
		return ValueUtil.VALOR_SIM.equals(flCargaFechada);
	}

	public boolean isLiberadoValidade() {
		return ValueUtil.VALOR_SIM.equals(flCargaVencidaLiberada);
	}

	public boolean isLiberadoPesoMin() {
		return ValueUtil.VALOR_SIM.equals(flPesoMenorLiberado);
	}
	
	@Override
	public boolean isEnviadoServidor() {
		return FLTIPOALTERACAO_ORIGINAL.equals(flTipoAlteracao) && isCargaFechada();
	}
    
}