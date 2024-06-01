package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class UsuarioDesc extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPUSUARIODESC";
	public static final String USUARIODESC_ERP = "E";
	public static final String USUARIODESC_PDA = "P";
	public static final String NMCOLUNA_NUORDEMLIBERACAO = "NUORDEMLIBERACAO";
	public static final String NMCOLUNA_VLPCTMAXDESCONTO = "VLPCTMAXDESCONTO";
	public static final String NMCOLUNA_VLPCTMAXDESCPONDERADO = "VLPCTMAXDESCPONDERADO";
	public static final String NMCOLUNA_VLPCTFIMALCADA = "VLPCTFIMALCADA";

    public String flOrigemDesconto;
    public double vlTotalFaturado;
    public double vlTotalDesconto;
    public double vlPctMaxDesconto;
    public double vlPctMaxDescPonderado;
    public int nuOrdemLiberacao;
    public double vlPctInicioAlcada;
    public double vlPctFimAlcada;
    public double vlPctMaxDescUsu;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioDesc) {
            UsuarioDesc usuarioDesc = (UsuarioDesc) obj;
            return
                ValueUtil.valueEquals(flOrigemDesconto, usuarioDesc.flOrigemDesconto) && 
                ValueUtil.valueEquals(cdUsuario, usuarioDesc.cdUsuario);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(flOrigemDesconto);
        primaryKey.append(";");
        primaryKey.append(cdUsuario);
        return primaryKey.toString();
    }
    
    public boolean isOrigemDescontoErp() {
    	return USUARIODESC_ERP.equals(flOrigemDesconto);
    }

}