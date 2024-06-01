package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ConfigValorMinimo extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPCONFIGVALORMINIMO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdConfigValorMinimo;
    public String cdTabelaPreco;
    public String cdGrupoValorMinPedido;
    public String cdCentroCusto;
    public String cdPlataformaVenda ;
    public String cdLinha;
    public double vlMinPedido;
    public double vlMinParcela;

	//Nao Persistentes
	public String dsLinha;
        
    public ConfigValorMinimo() {
	
    }
    
    public ConfigValorMinimo(Pedido pedido, String cdTabelaPreco, String cdGrupoValorMinPedido, String cdCentroCusto, String cdPlataforma, String cdLinha) {
		super();
		this.cdEmpresa = pedido.cdEmpresa;
		this.cdRepresentante = pedido.cdRepresentante;
		this.cdTabelaPreco = cdTabelaPreco;
		this.cdGrupoValorMinPedido = cdGrupoValorMinPedido;
		this.cdCentroCusto = cdCentroCusto;
		this.cdPlataformaVenda = cdPlataforma;
		this.cdLinha = cdLinha;
		limit = 1;
	}

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ConfigValorMinimo) {
            ConfigValorMinimo configValorMinimo = (ConfigValorMinimo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, configValorMinimo.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, configValorMinimo.cdRepresentante) && 
                ValueUtil.valueEquals(cdConfigValorMinimo, configValorMinimo.cdConfigValorMinimo);
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
        primaryKey.append(cdConfigValorMinimo);
        return primaryKey.toString();
    }

}