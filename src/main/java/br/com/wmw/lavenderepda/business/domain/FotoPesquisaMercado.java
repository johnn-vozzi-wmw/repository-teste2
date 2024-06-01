package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;

public class FotoPesquisaMercado extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPFOTOPESQUISAMERCADO";
    public static final String NM_COLUNA_NMFOTO = "nmFoto";
    
    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPesquisaMercado;
    public String cdPesquisaMercado;
    public String nmFoto;
    public int cdFoto;
    public String flEnviadoServidor;
        
    public int nuTamanho;
    
	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa).append(";");
        primaryKey.append(cdRepresentante).append(";");
        primaryKey.append(flOrigemPesquisaMercado).append(";");
        primaryKey.append(cdPesquisaMercado).append(";");
        primaryKey.append(nmFoto);
        return primaryKey.toString();
	}

	public boolean isFotoPesquisaMercadoEnviadaServidor() {
    	return ValueUtil.VALOR_SIM.equals(flEnviadoServidor);
	}
	
	public static String getPathImg() {
		return FotoUtil.getPathImg(FotoPesquisaMercado.class);
	}
	
}
