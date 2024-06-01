package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import totalcross.util.Date;

public class RequisicaoServResp extends BaseDomain  {
	
    public static String TABLE_NAME = "TBLVPREQUISICAOSERVRESP";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdRequisicaoServ;
    public String cdRequisicaoServResp;
    public Date   dtRequisicaoServResp;
    public String hrRequisicaoServResp;
    public String dsObservacao;
    public String cdUsuarioCriacao;
    public String nmUsuarioCriacao;
    
    //Não persistente
    public RequisicaoServMotivo motivoServ;
    
    public RequisicaoServResp() {
    	motivoServ = new RequisicaoServMotivo();
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdRequisicaoServ);
        primaryKey.append(";");
        primaryKey.append(cdRequisicaoServResp);
        return primaryKey.toString();
    }
 
}
