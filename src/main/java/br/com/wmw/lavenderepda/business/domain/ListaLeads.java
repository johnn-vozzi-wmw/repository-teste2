package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListaLeads extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPLISTALEADS";
    
    public static final String LEADS_TIPO_WEB = "W";
    public static final String LEADS_TIPO_PDA = "P";
    
    public static final String NMCOLUNA_CDLISTA = "CDLISTA";
    public static final String NMCOLUNA_DTCRIACAO = "DTCRIACAO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdLista;
    public String nmLista;
    public String flTpCadastro;
    public Date dtCriacao;
    
    //não persistente
    public Vector placesList;
    public Empresa empresa;
    public Representante representante;
    public Date dtInicioFiltro;
    public Date dtFimFiltro;
    public String dsFiltro;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ListaLeads) {
            ListaLeads listaLeads = (ListaLeads) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, listaLeads.cdEmpresa) 
                && ValueUtil.valueEquals(cdRepresentante, listaLeads.cdRepresentante) 
                && ValueUtil.valueEquals(cdLista, listaLeads.cdLista);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdLista);
        return  strBuffer.toString();
    }

	public String getCdDomain() {
		return cdLista;
	}

	public String getDsDomain() {
		return nmLista;
	}
	

}