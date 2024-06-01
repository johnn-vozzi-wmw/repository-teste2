package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;
import totalcross.util.Vector;

public class Prospect extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPPROSPECT";
	
	public static final String NMCOLUNA_NMRAZAOSOCIAL = "NMRAZAOSOCIAL";
	public static final String NMCOLUNA_DSCEPCOMERCIAL = "DSCEPCOMERCIAL";
	public static final String NMCOLUNA_DSLOGRADOUROCOMERCIAL = "DSLOGRADOUROCOMERCIAL";
	public static final String NMCOLUNA_DSCIDADECOMERCIAL = "DSCIDADECOMERCIAL";
	public static final String NMCOLUNA_DSESTADOCOMERCIAL = "DSESTADOCOMERCIAL";
	public static final String NMCOLUNA_DSBAIRROCOMERCIAL = "DSBAIRROCOMERCIAL";
	public static final String NMCOLUNA_CDUFCOMERCIAL = "CDUFCOMERCIAL";
	public static final String NMCOLUNA_DSTIPOLOGRADOURTOCOMERCIAL = "DSTIPOLOGRADOUROCOMERCIAL";
	public static final String NMCOLUNA_DSNUMEROLOGRADOUROCOMERCIAL = "DSNUMEROLOGRADOUROCOMERCIAL";
	public static final String NMCOLUNA_CDCIDADECOMERCIAL = "CDCIDADECOMERCIAL";
	public static final String NMCOLUNA_CDESTADOCOMERCIAL = "CDESTADOCOMERCIAL";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemProspect;
    public String cdProspect;
    public String nuCnpj;
    public Date dtCadastro;
    public String cdTipoPropesct;
    public String flTipoPessoa;
    public double cdLatitude;
    public double cdLongitude;
    public String dsEmail;
    public String hrCadastro;
    public String cdUsuarioCriacao;
    
    //Nao persistente
    public Vector fotoProspectList;
    public Vector fotoProspectExcluidaList;
    public NovoClienteAna novoClienteAna;
    
    public Prospect() {
		super(TABLE_NAME);
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Prospect) {
            Prospect prospect = (Prospect) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, prospect.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, prospect.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemProspect, prospect.flOrigemProspect) && 
                ValueUtil.valueEquals(cdProspect, prospect.cdProspect);
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
        primaryKey.append(flOrigemProspect);
        primaryKey.append(";");
        primaryKey.append(cdProspect);
        return primaryKey.toString();
    }
    
    public static String getPathImg() {
    	return FotoUtil.getPathImg(Prospect.class);
    }

}