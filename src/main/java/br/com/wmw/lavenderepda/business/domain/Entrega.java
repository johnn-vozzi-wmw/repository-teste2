package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Entrega extends LavendereBaseDomain {

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdEntrega;
    public String dsEntrega;
    public String cdTabelaPreco;
    public double vlMinPedido;
    public double qtMinProduto;
    public Date dtVigenciaInicial;
    public Date dtVigenciaFinal;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    //Nao persistente
    public boolean ignoraValidacao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Entrega) {
            Entrega entrega = (Entrega) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, entrega.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, entrega.cdRepresentante) && 
                ValueUtil.valueEquals(cdEntrega, entrega.cdEntrega);
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
        primaryKey.append(cdEntrega);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdEntrega;
	}

	@Override
	public String getDsDomain() {
		return dsEntrega;
	}

}