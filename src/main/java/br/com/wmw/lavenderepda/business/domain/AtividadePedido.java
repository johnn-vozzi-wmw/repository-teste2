package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class AtividadePedido extends BasePersonDomain {
	
	public AtividadePedido() {
		super(TABLE_NAME);
	}

	public static String TABLE_NAME = "TBLVPATIVIDADEPEDIDO";
	public static final String NMCOLUNA_NUSEQUENCIA = "NUSEQUENCIA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public int nuSequencia;
    public String cdStatusAtividade;
    public String cdUsuarioCriacao;
    public String nmUsuarioCriacao;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    public StatusAtividade statusAtividade;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AtividadePedido) {
            AtividadePedido atividadePedido = (AtividadePedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, atividadePedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, atividadePedido.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemPedido, atividadePedido.flOrigemPedido) && 
                ValueUtil.valueEquals(nuPedido, atividadePedido.nuPedido) && 
                ValueUtil.valueEquals(nuSequencia, atividadePedido.nuSequencia);
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
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(nuSequencia);
        return primaryKey.toString();
    }

}