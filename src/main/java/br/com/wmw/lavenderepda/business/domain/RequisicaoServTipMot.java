package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class RequisicaoServTipMot extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPREQUISICAOSERVTIPMOT";
	
	public String cdRepresentante;
	public String cdEmpresa;
	public String cdRequisicaoServTipo;
	public String cdRequisicaoServMotivo;
	
	//Não persistente
	public boolean obrigaCliente;
	public boolean obrigaPedido;
	
    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdRequisicaoServTipo);
        primaryKey.append(";");
        primaryKey.append(cdRequisicaoServMotivo);
        return primaryKey.toString();
    }

}
