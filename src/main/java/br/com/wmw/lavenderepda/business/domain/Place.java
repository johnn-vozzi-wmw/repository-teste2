package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Place extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPPLACES";
	public static final String TIPO_QUALIFICADO = "Q";
	public static final String TIPO_EM_ANALISE = "A";
	public static final String TIPO_NAO_QUALIFICADO = "N";
	
	public static final String NMCOLUNA_NUORDEM = "NUORDEM";
	public static final String NMCOLUNA_NMPLACE = "NMPLACE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdPlaceId;
    public String nmPlace;
    public String dsEndereco;
    public String dsTelefone;
    public String dsUrl;
    public double vlRating;
    public double cdLatitude;
    public double cdLongitude;
    public String cdLista;
    public String cdCategoria;
    public String flQualificado;
    public String dsObservacao;
    public Date dtInativacao;
    public int nuOrdem;
    
    //não persistente
    public String dsFiltro;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Place) {
            Place place = (Place) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, place.cdEmpresa) 
                && ValueUtil.valueEquals(cdRepresentante, place.cdRepresentante) 
                && ValueUtil.valueEquals(cdPlaceId, place.cdPlaceId)
                && ValueUtil.valueEquals(cdLista, place.cdLista);
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
    	strBuffer.append(cdPlaceId);
    	strBuffer.append(";");
    	strBuffer.append(cdLista);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdLista;
	}

	public String getDsDomain() {
		return nmPlace;
	}

}