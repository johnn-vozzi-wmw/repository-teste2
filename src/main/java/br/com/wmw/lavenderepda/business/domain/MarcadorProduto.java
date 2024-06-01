package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class MarcadorProduto extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPMARCADORPRODUTO";
	public static final String NMCOLUNA_CDMARCADOR = "CDMARCADOR";
	public static final int CDTIPOMARCADOR_CASADINHA = 6;
	public static final int CDTIPOMARCADOR_INCENTIVO = 7;

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdMarcador;
    public String cdCliente;
    public String flFiltroSelecionado;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MarcadorProduto) {
            MarcadorProduto marcadorProduto = (MarcadorProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, marcadorProduto.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, marcadorProduto.cdRepresentante) && 
                ValueUtil.valueEquals(cdProduto, marcadorProduto.cdProduto) && 
                ValueUtil.valueEquals(cdMarcador, marcadorProduto.cdMarcador) &&
                ValueUtil.valueEquals(cdCliente, marcadorProduto.cdCliente);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdMarcador);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

}