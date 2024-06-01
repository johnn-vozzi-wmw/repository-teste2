package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Categoria extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPCATEGORIA";
    
    public static final String TIPO_CATEGORIA_ATACADO = "A";
    public static final String TIPO_CATEGORIA_ESPECIAL = "E";
    public static final String TIPO_CATEGORIA_VAREJO = "V";
    
    public static final String TEXT_SEGUNDO_DESC_CAT = "segundo";
    public static final String TEXT_TERCEIRO_DESC_CAT = "terceiro";

    public String cdEmpresa;
    public String cdCategoria;
    public String dsCategoria;
    public double vlMinPedido;
    public String flDivideVlMin;
    public String flTipoCategoria;
    public double vlPctDescEspecial;
    public double vlPctDescAtacado;
    public double vlMinPedidoEspecial;
    public double vlMinPedidoAtacado;
    public double qtMinVendidoBonifTroca;
    public String cdCategoriaLeads;
    
    public boolean buscaCategoriaLeads;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Categoria) {
            Categoria categoria = (Categoria) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, categoria.cdEmpresa) &&
                ValueUtil.valueEquals(cdCategoria, categoria.cdCategoria);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdCategoria);
        return  strBuffer.toString();
    }

    @Override
	public String getCdDomain() {
		return cdCategoria;
	}

    @Override
	public String getDsDomain() {
		return dsCategoria;
	}

	public boolean isDivideVlMin() {
		return ValueUtil.VALOR_SIM.equals(flDivideVlMin);
	}
	
	public boolean isCategoriaEspecial() {
		return TIPO_CATEGORIA_ESPECIAL.equals(flTipoCategoria);
	}
	
	public boolean isCategoriaAtacado() {
		return TIPO_CATEGORIA_ATACADO.equals(flTipoCategoria);
	}

	public boolean isBuscaCategoriaLeadas() {
		return buscaCategoriaLeads;
	}

}