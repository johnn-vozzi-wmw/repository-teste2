package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class PlataformaVendaCliFin extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPLATAFORMAVENDACLIFIN";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdPlataformaVenda;
    public String cdLinha;
    public double vlIndiceFinanceiro;

    public PlataformaVendaCliFin() { }
    
    public PlataformaVendaCliFin(Pedido pedido, Produto produto) {
    	if (pedido == null) return;
    	this.cdEmpresa = pedido.cdEmpresa;
    	this.cdRepresentante = pedido.cdRepresentante;
    	this.cdCliente = pedido.cdCliente;
    	this.cdPlataformaVenda = pedido.cdPlataformaVenda;
    	if (produto == null) return;
    	this.cdLinha = produto.cdLinha;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlataformaVendaCliFin) {
            PlataformaVendaCliFin plataformaVendaCliFin = (PlataformaVendaCliFin) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, plataformaVendaCliFin.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, plataformaVendaCliFin.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, plataformaVendaCliFin.cdCliente) && 
                ValueUtil.valueEquals(cdPlataformaVenda, plataformaVendaCliFin.cdPlataformaVenda) && 
                ValueUtil.valueEquals(cdLinha, plataformaVendaCliFin.cdLinha);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdPlataformaVenda);
        primaryKey.append(";");
        primaryKey.append(cdLinha);
        return primaryKey.toString();
    }

}